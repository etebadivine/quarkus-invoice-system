package org.generis.dto


data class CreateCustomerDto(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var country: String,
    var city: String,
    var taxNumber: String,
    var currency: String
)
