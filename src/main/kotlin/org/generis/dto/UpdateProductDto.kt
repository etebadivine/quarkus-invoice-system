package org.generis.dto

import kotlinx.serialization.Serializable
import lombok.Data
import org.generis.enums.ProductState

@Data
@Serializable
data class UpdateProductDto(
    var unitPrice: Double? = null,
    var productName: String? = null,
    var description: String? = null,
    var isRecurring: ProductState? = null,
    var recurringPeriod: Long? = null
)
