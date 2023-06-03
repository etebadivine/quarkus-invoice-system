package org.generis.dto

import kotlinx.serialization.Serializable


@Serializable
data class CreateInvoiceItemDto (
    val productId: String,
    val quantity: Int,
    val totalAmount: Double? = null,
)
