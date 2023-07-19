package org.generis.business.subscription.dto


data class SubscriptionItemDto(
    var productId:String?,
    var quantity: Int?,
)
{
    constructor() : this(
        null,
        null
    )
}

