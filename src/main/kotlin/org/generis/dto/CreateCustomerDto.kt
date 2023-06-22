package org.generis.dto

import org.generis.enums.Currency


data class CreateCustomerDto(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var country: String,
    var city: String,
    var taxNumber: String,
    var currency: Currency
)
