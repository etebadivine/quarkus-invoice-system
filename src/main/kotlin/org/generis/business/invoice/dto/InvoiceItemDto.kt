package org.generis.business.invoice.dto


data class InvoiceItemDto(
    var productId:String?,
    var quantity: Int?,
)
{
    constructor() : this(
        null,
        null,
    )
}

