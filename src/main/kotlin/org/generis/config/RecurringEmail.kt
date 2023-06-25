package org.generis.config

import io.quarkus.scheduler.Scheduled
import jakarta.inject.Singleton
import org.generis.enums.SubscriptionState
import org.generis.service.SubscriptionService
import java.time.LocalDateTime


@Singleton
class RecurringEmail(
    private val subscriptionService: SubscriptionService,
    private val recurringMail: RecurringInvoice
) {

//    "*/3 * * * * ?"
//    "0 0 7 * * ?"
//    "0 50 14 * * ?"


    @Scheduled(cron = "0 50 14 * * ?")
    fun sendRecurringInvoices() {
        val currentDateTime = LocalDateTime.now()
        val currentDate = currentDateTime.toLocalDate()

        val subscriptions = subscriptionService.getAllSubscriptions()

        for (subscription in subscriptions) {
            val nextInvoiceDate = subscription.nextInvoiceDate
            val endDate = subscription.endDate

            if (nextInvoiceDate!!.isEqual(currentDate).and(subscription.status == SubscriptionState.ACTIVE)) {
                subscriptionService.sendInvoice(subscription, recurringMail)
                subscriptionService.updateNextInvoiceDate(subscription)
                subscriptionService.updateSubscription(subscription)
            }
            else if (endDate != null && endDate.isEqual(currentDate) && subscription.status == SubscriptionState.ACTIVE) {
                subscription.status = SubscriptionState.CANCELED
                subscriptionService.updateSubscription(subscription)
            }
        }
    }





































//    private fun addSubscription(subscription: SubscriptionDto) {
//        subscriptions.add(subscription)
//    }

//    @Scheduled(cron = "0 0 9 ? * MON") // Schedule to run every Monday at 9 AM
//    @Scheduled(cron = "*/1 * * * * ?") // Schedule to run every Monday at 9 AM
//    fun sendRecurringEmails() {
//        val currentDate = LocalDate.now()
//
//        subscriptions.forEach { subscription ->
//            val startDate = subscription.startDate
//            val endDate = subscription.nextInvoiceDate
//            val recurringPeriod = subscription.recurringPeriod
//
//            if (startDate != null && endDate != null && recurringPeriod != null) {
//                if (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)) {
//                    val customerEmail = subscription.customerId
//                    val customer = customerService.getCustomer(customerEmail!!)
//
//                    val productName = subscription.items
//                    val product = productService.getProduct(productName!!.toString())
//
//                    if (customer != null && product != null) {
//                        val emailContent =
//                            "Dear customer, thank you for your subscription to ${product.productName}.\\n\\n\" +" +
//                                    "The total amount due is ${subscription.totalAmount}."
//                        mailer.send(Mail.withText(customer.email, emailContent, "Your Invoice."));
//
//                        val nextRecurringDate = currentDate.plusDays(recurringPeriod)
//                        if (nextRecurringDate.isBefore(endDate)) {
//                            subscription.startDate = nextRecurringDate
//                            addSubscription(subscription)
//                        }
//                    }
//                }
//            }
//        }
//    }
}




