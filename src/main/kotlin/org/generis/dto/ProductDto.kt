package org.generis.dto

import org.generis.enums.ProductState
import java.time.LocalDateTime

data class ProductDto(
    var id: String?,
    var unitPrice: Double?,
    var productName: String?,
    var description: String?,
    var isRecurring: ProductState?,
    var createdDate: LocalDateTime? = LocalDateTime.now()
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