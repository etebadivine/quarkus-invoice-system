package org.generis.business.invoice.dto

import org.generis.business.invoice.enums.InvoiceStatus


data class UpdateInvoiceStatusDto (
    var invoiceId: String,
    var status: InvoiceStatus
    )
