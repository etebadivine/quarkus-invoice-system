package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.ProductState

@Serializable
data class ProductDto(
    var id: String?,
    var unitPrice: Double?,
    var productName: String?,
    var description: String?,
    var isRecurring: ProductState?,
    var recurringPeriod: Long?,
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null
    )
}