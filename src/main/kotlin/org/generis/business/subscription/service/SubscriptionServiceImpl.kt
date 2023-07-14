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
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.enums.LogAction
import org.generis.business.logs.service.JwtService
import org.generis.business.logs.service.LogService
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

    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var jwtService: JwtService

    override fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription? {

        val customer = entityManager.find(Customer::class.java, createSubscriptionDto.customerId)
            ?: throw IllegalArgumentException("Invalid customer id")

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

        var subTotal = 0.00
        for (itemDto in createSubscriptionDto.items) {
            val product = entityManager.find(Product::class.java, itemDto.productId)
                ?: throw IllegalArgumentException("Invalid productId")

            val startDate = subscription.startDate
            val total = if (createSubscriptionDto.recurringPeriod.toInt() == 30 && startDate!!.dayOfMonth >= 20) {
                product.unitPrice?.div(2)?.times(itemDto.quantity!!)
            } else {
                product.unitPrice?.times(itemDto.quantity!!)
            }

            // Create the SubscriptionItem instance
            val subscriptionItems = SubscriptionItem()
            subscriptionItems.productId = product
            subscriptionItems.quantity = itemDto.quantity
            subscriptionItems.totalAmount = total
            subscriptionItems.subscription = subscription

            // Add the subscription item to the subscription
            subscription.items.add(subscriptionItems)

            if (total != null) {
                subTotal += total
            }
        }

        subscription.totalAmount = subTotal.toBigDecimal().setScale(4, RoundingMode.UP).toDouble()

        entityManager.persist(subscription)

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.CREATED_SUBSCRIPTION,
            target = subscription.subscriptionNumber!!,
            userId = user.id
        )
        logService.createLog(createLog)

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

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.CANCEL_SUBSCRIPTION,
            target = subscription.subscriptionNumber!!,
            userId = user.id
        )
        logService.createLog(createLog)
    }

    override fun reactivateSubscription(id: String) {
        val subscription = entityManager.find(Subscription::class.java, id)
        subscription?.let {
            it.status = SubscriptionState.ACTIVE
        }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.REACTIVATED_SUBSCRIPTION,
            target = subscription.subscriptionNumber!!,
            userId = user.id
        )
        logService.createLog(createLog)
    }

    private fun sendMailAlert(subscription:Subscription){
        mailer.send(Mail.withText(subscription.customerId?.email,
            "Hello ${subscription.customerId?.name},thank You For Subscribing",
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

