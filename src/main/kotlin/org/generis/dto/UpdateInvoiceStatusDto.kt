package org.generis.dto

import org.generis.enums.InvoiceStatus


data class UpdateInvoiceStatusDto (
    var invoiceId: String,
    var status: InvoiceStatus
    )
