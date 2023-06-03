package org.generis.dto

import kotlinx.serialization.Serializable


@Serializable
data class InvoiceItemDto(
    var id:String? = null,
    var productId:String?,
    var quantity: Int? = null,
    var totalAmount: Double? = null
)
{
    constructor() : this(
        null,
        null,
        null,
        null,
    )
}
