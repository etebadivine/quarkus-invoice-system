package org.generis.business.invoice.dto

data class CreateInvoiceDto(
    var title: String? = null ,
    var subHeading: String? = null,
    var customerId: String,
    var items: List<InvoiceItemDto>,
    var company: String?,
    var country: String?,
    val useCustomerCurrency: Boolean = true,
    var useCompanyCurrency: Boolean = false,
    var dueDate: String,
    var tax: Double? = 0.00,
    var discount: Double? =0.00
)
