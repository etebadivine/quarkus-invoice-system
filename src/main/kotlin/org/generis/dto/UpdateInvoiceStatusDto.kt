package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.InvoiceStatus


@Serializable
data class UpdateInvoiceStatusDto (
    var invoiceId: String,
    var status: InvoiceStatus
    )
