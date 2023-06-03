package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.Currency


@Serializable
data class CreateInvoiceDto(
    var title: String? = null ,
    var subHeading: String? = null,
    var customerId: String,
    var items: List<InvoiceItemDto>,
    var currency: Currency,
    var dueDate: String,
    var tax: Double? = 0.00,
    var discount: Double? = 0.00,
    var totalAmount: Double? = null,
    var subTotal: Double? = null
)
