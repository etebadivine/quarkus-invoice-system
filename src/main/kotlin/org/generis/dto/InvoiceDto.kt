package org.generis.dto

import org.generis.enums.Currency
import org.generis.enums.InvoiceStatus
import java.time.LocalDate
import java.time.LocalDateTime


data class InvoiceDto (
    var id: String?,
    var invoiceNumber: String?,
    var title: String?,
    var subHeading: String?,
    var customerId: String?,
    var items:List<InvoiceItemDto>?,
    var currency: Currency?,
    var dueDate: LocalDate?,
    var status: InvoiceStatus?,
    var createdDate: LocalDateTime? = LocalDateTime.now(),
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
        null,
    )

}
