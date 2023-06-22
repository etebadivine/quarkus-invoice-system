package org.generis.dto

import java.time.LocalDate


data class SubscriptionDto (
    var id: String?,
    var customerId: String?,
    var items: List<SubscriptionItemDto>?,
    var startDate: LocalDate?,
    var nextInvoiceDate: LocalDate?,
    var recurringPeriod: Long?,
    var totalAmount: Double?
    )
{
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

}
