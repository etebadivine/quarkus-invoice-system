package org.generis.service

import jakarta.ws.rs.core.Response
import org.generis.config.RecurringMail
import org.generis.dto.CreateSubscriptionDto
import org.generis.dto.SubscriptionDto
import org.generis.entity.Subscription

interface SubscriptionService {

    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): Subscription

    fun getSubscription(id: String): Subscription?

    fun sendInvoice(subscription: Subscription, recurringMail: RecurringMail)


    fun updateNextInvoiceDate(subscription: Subscription)

    fun getAllSubscriptions(): List<Subscription>

    fun updateSubscription(subscription: Subscription)

    fun getAllActiveSubscriptions(): List<Subscription>



}