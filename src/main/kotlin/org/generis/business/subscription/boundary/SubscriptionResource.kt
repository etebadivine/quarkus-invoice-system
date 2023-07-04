package org.generis.business.subscription.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.subscription.dto.CreateSubscriptionDto
import org.generis.business.subscription.repo.Subscription

interface SubscriptionResource {

    fun createSubscription(@Valid createSubscriptionDto: CreateSubscriptionDto): ApiResponse<Subscription?>

    fun getSubscription(id: String): ApiResponse<Subscription?>

    fun getAllSubscriptions(): ApiResponse<List<Subscription>>

    fun getAllActiveSubscriptions(): ApiResponse<List<Subscription>>

    fun cancelSubscription(id: String): ApiResponse<String>

    fun reactivateSubscription(id: String): ApiResponse<String>
}