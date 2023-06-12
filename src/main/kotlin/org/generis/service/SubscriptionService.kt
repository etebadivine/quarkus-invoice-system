package org.generis.service

import org.generis.dto.CreateSubscriptionDto
import org.generis.dto.SubscriptionDto
import org.generis.entity.RecurringInvoice
import org.generis.entity.Subscription

interface SubscriptionService {

    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription

    fun getSubscription(id: String): Subscription?

    fun getAllSubscriptions(): List<SubscriptionDto>


}