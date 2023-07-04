package org.generis.business.subscription.service

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.base.integrations.RecurringInvoice
import org.generis.business.customer.repo.Customer
import org.generis.business.product.repo.Product
import org.generis.business.subscription.dto.CreateSubscriptionDto
import org.generis.business.subscription.enums.SubscriptionState
import org.generis.business.subscription.repo.Subscription
import org.generis.business.subscription.repo.SubscriptionItem
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Transactional
@ApplicationScoped
class SubscriptionServiceImpl: SubscriptionService{

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var mailer: Mailer

    override fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription? {

        val customer = entityManager.find(Customer::class.java, createSubscriptionDto.customerId)
            ?: throw IllegalArgumentException("Invalid customerId")

        val subscription = Subscription()
        subscription.subscriptionNumber = subscription.generateSubscriptionNumber()
        subscription.customerId = customer
        subscription.startDate = LocalDate.parse(createSubscriptionDto.startDate,
            DateTimeFormatter.ofPattern("dd-MM-yyyy" ))
        subscription.endDate = LocalDate.parse(createSubscriptionDto.endDate,
            DateTimeFormatter.ofPattern("dd-MM-yyyy" ))
        subscription.recurringPeriod = createSubscriptionDto.recurringPeriod
        subscription.nextInvoiceDate = LocalDate.now()
        subscription.createdDate = LocalDateTime.now()
        subscription.tax = createSubscriptionDto.tax
        subscription.discount = createSubscriptionDto.discount

        for (itemDto in createSubscriptionDto.items) {
            val product = entityManager.find(Product::class.java, itemDto.productId)
                ?: throw IllegalArgumentException("Invalid productId")

            // Calculate the total for the invoice item based on the product price and quantity
            val itemTotal = product.unitPrice?.times(itemDto.quantity!!)?.times(customer.currency!!.exchangeRate!!)

            // Create the InvoiceItem instance
            val subscriptionItems = SubscriptionItem()
            subscriptionItems.productId = product
            subscriptionItems.quantity = itemDto.quantity
            subscriptionItems.totalAmount = itemTotal
            subscriptionItems.subscription = subscription

            // Add the invoice item to the invoice
            subscription.items.add(subscriptionItems)

            // Apply tax and discount to calculate the total
            val discountPercent = createSubscriptionDto.discount
            val discountAmount = itemTotal?.times((discountPercent?.div(100.0)?: 0.0))

            val taxPercent = createSubscriptionDto.tax
            val taxAmount = itemTotal?.times((taxPercent?.div(100.0)?: 0.0))

            // Apply tax and discount to calculate the total
            if (itemTotal != null) {
               val totalAmount = itemTotal + taxAmount!! - discountAmount!!
                subscription.totalAmount = totalAmount.toBigDecimal().setScale(4, RoundingMode.UP).toDouble()
            }

            subscription.totalAmount = itemTotal
        }

        entityManager.persist(subscription)
        updateNextInvoiceDate(subscription)
        updateSubscription(subscription)
        sendMailAlert(subscription)

        return subscription
    }

    override fun sendInvoice(subscription: Subscription, recurringInvoice: RecurringInvoice) {
        val customerEmail = subscription.customerId?.email

        val invoiceHtml = recurringInvoice.generateInvoiceHtml(subscription)

        mailer.send(
            Mail.withHtml(customerEmail, "Invoice", "")
                .setHtml(invoiceHtml)
        )
    }

    override fun cancelSubscription(id: String) {
        val subscription = entityManager.find(Subscription::class.java, id)
        subscription?.let {
            it.status = SubscriptionState.CANCELED
        }
    }

    override fun reactivateSubscription(id: String) {
        val subscription = entityManager.find(Subscription::class.java, id)
        subscription?.let {
            it.status = SubscriptionState.ACTIVE
        }
    }

    private fun sendMailAlert(subscription:Subscription){
        mailer.send(Mail.withText(subscription.customerId?.email,
            "Dear ${subscription.customerId?.name},Thank You For Subscribing,",
            "Your first payment is due on ${subscription.nextInvoiceDate}"))
    }

    override fun updateNextInvoiceDate(subscription: Subscription) {
        val nextInvoiceDate = subscription.nextInvoiceDate
        val recurringPeriod = subscription.recurringPeriod

        if (nextInvoiceDate != null && recurringPeriod != null) {
            subscription.nextInvoiceDate = nextInvoiceDate.plusDays(recurringPeriod.toLong())
        } else {
            throw IllegalArgumentException("Invalid nextInvoiceDate or recurringPeriod")
        }
    }

    override fun updateSubscription(subscription: Subscription) {
        val subscriptionId = subscription.id
        if (subscriptionId.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid subscription id")
        }

        val subscriptionEntity = entityManager.find(Subscription::class.java, subscriptionId)
            ?: throw IllegalArgumentException("Subscription not found for id: $subscriptionId")

        subscriptionEntity.nextInvoiceDate = subscription.nextInvoiceDate

        entityManager.merge(subscriptionEntity)
    }

    override fun getSubscription(id: String): Subscription? {
        return entityManager.find(Subscription::class.java, id)
    }

    override fun getAllSubscriptions(): List<Subscription> {
        val recurringInvoices =
            entityManager.createQuery("SELECT s FROM Subscription s", Subscription::class.java)?.resultList
        return recurringInvoices ?: throw ServiceException(-1, "No subscriptions found")
    }

    override fun getAllActiveSubscriptions(): List<Subscription> {
        val currentDate = LocalDate.now()
        val query: TypedQuery<Subscription> = entityManager.createQuery(
            "SELECT s FROM Subscription s WHERE s.nextInvoiceDate = :currentDate",
            Subscription::class.java
        )
        query.setParameter("currentDate", currentDate)
        return query.resultList ?: throw ServiceException(-1, "No subscriptions found")
    }
}

