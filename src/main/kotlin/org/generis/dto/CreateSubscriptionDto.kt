package org.generis.dto

data class CreateSubscriptionDto(
    var customerId: String,
    var items: List<SubscriptionItemDto>,
    var startDate: String,
    var endDate: String,
    val recurringPeriod: Long,
    var tax: Double? = null,
    var discount: Double? = null,
    var totalAmount: Double? = null
)
