package org.generis.service.Impl

import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.dto.*
import org.generis.entity.*
import org.generis.exception.ServiceException
import org.generis.service.SubscriptionService
import java.time.LocalDateTime


@Singleton
@Transactional
class SubscriptionServiceImpl: SubscriptionService {

    @Inject
    var entityManager: EntityManager? = null

    override fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription {
        val customer = entityManager?.find(Customer::class.java, createSubscriptionDto.customerId)
            ?: throw IllegalArgumentException("Invalid customerId")

        val subscription = Subscription()
        subscription.customerId = customer
        subscription.startDate = createSubscriptionDto.startDate
        subscription.endDate = createSubscriptionDto.endDate
        subscription.frequency = createSubscriptionDto.frequency
        subscription.totalAmount = 0.00

        for (itemDto in createSubscriptionDto.items) {
            val product = entityManager?.find(Product::class.java, itemDto.productId)
                ?: throw IllegalArgumentException("Invalid productId")

            // Calculate the total for the invoice item based on the product price and quantity
            val itemTotal = product.unitPrice?.times(itemDto.quantity!!)?.times(customer.currency!!.exchangeRate)

            // Create the InvoiceItem instance
            val subscriptionItems = SubscriptionItem()
            subscriptionItems.productId = product
            subscriptionItems.quantity = itemDto.quantity
            subscriptionItems.totalAmount = itemTotal
            subscriptionItems.subscription = subscription

            // Add the invoice item to the invoice
            subscription.items.add(subscriptionItems)

        }
        entityManager?.persist(subscription)

        return subscription
    }

    override fun getSubscription(id: String): Subscription? {
        return entityManager?.find(Subscription::class.java, id)
    }

    override fun getAllSubscriptions(): List<SubscriptionDto> {
        val recurringInvoices =
            entityManager?.createQuery("SELECT s FROM Subscription s", Subscription::class.java)?.resultList
        return recurringInvoices?.map { it.toDto() } ?: throw ServiceException(-1, "No subscriptions found")
    }


    private fun Subscription.toDto(): SubscriptionDto {
        return SubscriptionDto(
            id = this.id,
            customerId = this.customerId?.id,
            items = this.items.map { it.toDto() },
            startDate = this.startDate,
            endDate = this.endDate,
            frequency = this.frequency,
            totalAmount = this.totalAmount
        )
    }

    private fun SubscriptionItem.toDto(): SubscriptionItemDto {
        return SubscriptionItemDto(
            id = this.id,
            productId = this.productId?.id,
            quantity = this.quantity,
            totalAmount = this.totalAmount
        )
    }
}

