package org.generis.dto


data class SubscriptionItemDto(
    var id:String?= null,
    var productId:String?,
    var quantity: Int?,
    var totalAmount: Double? = null
)
{
    constructor() : this(
        null,
        null,
        null,
        null
    )
}

