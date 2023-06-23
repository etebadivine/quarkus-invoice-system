package org.generis.service.Impl

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import org.generis.config.RecurringMail
import org.generis.dto.CreateSubscriptionDto
import org.generis.entity.Customer
import org.generis.entity.Product
import org.generis.entity.Subscription
import org.generis.entity.SubscriptionItem
import org.generis.exception.ServiceException
import org.generis.service.SubscriptionService
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Singleton
@Transactional
class SubscriptionServiceImpl: SubscriptionService{

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var mailer: Mailer

    override fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription {

        val customer = entityManager.find(Customer::class.java, createSubscriptionDto.customerId)
            ?: throw IllegalArgumentException("Invalid customerId")

        val subscription = Subscription()
        subscription.subscriptionNumber = subscription.generateSubscriptionNumber()
        subscription.customerId = customer
        subscription.startDate = LocalDate.parse(createSubscriptionDto.startDate,
            DateTimeFormatter.ofPattern("dd-MM-yyyy" ))
        subscription.recurringPeriod = createSubscriptionDto.recurringPeriod
        subscription.nextInvoiceDate = LocalDate.now()
        subscription.tax = createSubscriptionDto.tax
        subscription.discount = createSubscriptionDto.discount
        subscription.totalAmount = 0.00

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
            val discountAmount = itemTotal?.times((discountPercent?.div(100.0)!!))

            val taxPercent = createSubscriptionDto.tax
            val taxAmount = itemTotal?.times((taxPercent?.div(100.0)!!))

            // Apply tax and discount to calculate the total
            if (itemTotal != null) {
                subscription.totalAmount = itemTotal + taxAmount!! - discountAmount!!
            }

            subscription.totalAmount = itemTotal
        }

        entityManager.persist(subscription)
        updateNextInvoiceDate(subscription)
        updateSubscription(subscription)
        sendMailAlert(subscription)

        return subscription
    }

    override fun sendInvoice(subscription: Subscription, recurringMail: RecurringMail) {
        val customerEmail = subscription.customerId?.email

        val invoiceHtml = recurringMail.generateInvoiceHtml(subscription)

        mailer.send(
            Mail.withHtml(customerEmail, "Invoice", "")
                .setHtml(invoiceHtml)
        )
    }

    private fun sendMailAlert(subscription:Subscription){
        mailer.send(Mail.withText(subscription.customerId?.email,
            "Dear ${subscription.customerId?.name},Thank You For Subscribing,",
            "Your first payment is due on ${subscription.nextInvoiceDate}"))
    }

    override fun updateNextInvoiceDate(subscription: Subscription) {
        val nextInvoiceDate = subscription.nextInvoiceDate
        val recurringPeriod = subscription?.recurringPeriod

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

