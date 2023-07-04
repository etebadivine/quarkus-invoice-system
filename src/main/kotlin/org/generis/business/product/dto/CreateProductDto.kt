package org.generis.business.product.dto

import org.generis.business.product.enums.ProductState


data class CreateProductDto (
    val productName: String,
    val description: String,
    val unitPrice: Double,
    var productState: ProductState? = null
)
