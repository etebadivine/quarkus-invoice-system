package org.generis.base.integrations

import io.quarkus.scheduler.Scheduled
import jakarta.inject.Singleton
import org.generis.business.subscription.enums.SubscriptionState
import org.generis.business.subscription.service.SubscriptionService
import java.time.LocalDateTime


@Singleton
class RecurringEmail(
    private val subscriptionService: SubscriptionService,
    private val recurringMail: RecurringInvoice
) {

//    "*/3 * * * * ?"
//    "0 0 7 * * ?"
//    "0 50 14 * * ?"

    @Scheduled(cron =  "0 50 14 * * ?")
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
}




