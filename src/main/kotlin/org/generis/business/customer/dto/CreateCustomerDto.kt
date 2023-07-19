package org.generis.business.customer.dto


data class CreateCustomerDto(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var country: String? = null,
    var city: String? = null,
)
