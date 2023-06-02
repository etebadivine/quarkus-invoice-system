package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.ProductState
import java.time.LocalDateTime

@Serializable
data class ProductDto(
    var id: String?,
    var unitPrice: Double? = null,
    var productName: String? = null,
    var description: String? = null,
    var isRecurring: ProductState? = null,
    var recurringPeriod: Long? = null,
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