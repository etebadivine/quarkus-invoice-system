package org.generis.dto

data class CreateInvoiceItemDto (
    val productId: String,
    val quantity: Int,
    val totalAmount: Double? = null,
)
