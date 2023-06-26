package org.generis.dto

import org.generis.enums.ProductState

data class CreateProductDto (
    val productName: String,
    val description: String,
    val unitPrice: Double,
    val isRecurring: ProductState? = null
//    val recurringPeriod: Long? = null,
)
