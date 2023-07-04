package org.generis.business.product.dto

import lombok.Data
import org.generis.business.product.enums.ProductState

@Data
data class UpdateProductDto(
    var unitPrice: Double? = null,
    var productName: String? = null,
    var description: String? = null,
    var productState: ProductState? = null,
)
