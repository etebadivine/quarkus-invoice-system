package org.generis.dto

data class CreateSubscriptionDto(
    var customerId: String,
    var items: List<SubscriptionItemDto>,
    var startDate: String,
    var recurringPeriod: Long?,
    var tax: Double? = 0.00,
    var discount: Double? = 0.00,
    var totalAmount: Double? = null
)
