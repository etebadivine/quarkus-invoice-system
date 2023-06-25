package org.generis.service

import org.generis.config.RecurringInvoice
import org.generis.dto.CreateSubscriptionDto
import org.generis.entity.Subscription

interface SubscriptionService {

    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription

    fun getSubscription(id: String): Subscription?

    fun sendInvoice(subscription: Subscription, recurringInvoice: RecurringInvoice)

    fun cancelSubscription(id: String)

    fun reactivateSubscription(id: String)

    fun updateNextInvoiceDate(subscription: Subscription)

    fun getAllSubscriptions(): List<Subscription>

    fun updateSubscription(subscription: Subscription)

    fun getAllActiveSubscriptions(): List<Subscription>



}