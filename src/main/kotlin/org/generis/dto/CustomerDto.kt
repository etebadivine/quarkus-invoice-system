package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.Currency


@Serializable
data class CustomerDto (
    var id: String?,
    var name: String?,
    var email: String?,
    var phoneNumber: String?,
    var country: String?,
    var city: String?,
    var taxNumber: String?,
    var currency: Currency?,
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}
