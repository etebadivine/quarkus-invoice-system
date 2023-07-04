package org.generis.business.subscription.service

import org.generis.base.integrations.RecurringInvoice
import org.generis.business.subscription.dto.CreateSubscriptionDto
import org.generis.business.subscription.repo.Subscription

interface SubscriptionService {

    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription?

    fun getSubscription(id: String): Subscription?

    fun sendInvoice(subscription: Subscription, recurringInvoice: RecurringInvoice)

    fun cancelSubscription(id: String)

    fun reactivateSubscription(id: String)

    fun updateNextInvoiceDate(subscription: Subscription)

    fun getAllSubscriptions(): List<Subscription>

    fun updateSubscription(subscription: Subscription)

    fun getAllActiveSubscriptions(): List<Subscription>



}