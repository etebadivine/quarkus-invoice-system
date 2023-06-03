package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.ProductState


@Serializable
data class CreateProductDto (
    val productName: String,
    val description: String,
    val unitPrice: Double,
    val isRecurring: ProductState? = null,
    val recurringPeriod: Long? = null
)
