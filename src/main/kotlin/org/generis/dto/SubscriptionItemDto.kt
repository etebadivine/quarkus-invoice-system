package org.generis.dto

import kotlinx.serialization.Serializable


@Serializable
data class SubscriptionItemDto(
    var id:String?= null,
    var productId:String?,
    var quantity: Int?,
    var totalAmount: Double? = null
)
{
//    constructor() : this(
//        null,
//        null,
//        null,
//        null
//    )
}

