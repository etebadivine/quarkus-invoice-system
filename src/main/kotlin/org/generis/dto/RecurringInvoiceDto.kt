package org.generis.dto

import com.fasterxml.jackson.annotation.JsonFormat
import kotlinx.serialization.Serializable
import org.generis.entity.Subscription
import org.generis.enums.Currency
import org.generis.enums.InvoiceStatus
import org.generis.enums.RecurringPeriod
import org.generis.util.JacksonUtils
import org.generis.util.LocalDateSerializer
import org.generis.util.LocalDateTimeSerializer
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class RecurringInvoiceDto (
    var id: String?,
    var invoiceNumber: String?,
    var title: String?,
    var subHeading: String?,
    var customerId: String?,
    var subscription: Subscription?,
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateSerializer::class)
    var dueDate: LocalDate?,
    var status: InvoiceStatus?,
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateTimeSerializer::class)
    var createdDate: LocalDateTime? = LocalDateTime.now(),
    var recurringPeriod: RecurringPeriod?,
    var tax: Double?,
    var discount: Double?,
    var subTotal: Double?,
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
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null

    )

}