package org.generis.dto

import com.fasterxml.jackson.annotation.JsonFormat
import kotlinx.serialization.Serializable
import org.generis.entity.SubscriptionItem
import org.generis.enums.SubscriptionFrequency
import org.generis.util.JacksonUtils
import org.generis.util.LocalDateSerializer
import java.time.LocalDate


@Serializable
data class SubscriptionDto (
    var id: String?,
    var customerId: String?,
    var items: List<SubscriptionItemDto>?,
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateSerializer::class)
    var startDate: LocalDate?,
    @JsonFormat(pattern = JacksonUtils.dateTimePattern)
    @Serializable(with = LocalDateSerializer::class)
    var endDate: LocalDate?,
    var frequency: Long?,
    var totalAmount: Double?
    )
{
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

}
