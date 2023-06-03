package org.generis.dto

import kotlinx.serialization.Serializable
import org.generis.enums.Currency


@Serializable
data class CreateCustomerDto(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var country: String,
    var city: String,
    var taxNumber: String,
    var currency: Currency
)
