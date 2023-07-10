package org.generis.business.subscription.dto


data class CreateSubscriptionDto(
    var customerId: String,
    var items: List<SubscriptionItemDto>,
    var startDate: String,
    var endDate: String,
    val recurringPeriod: Long,
)
