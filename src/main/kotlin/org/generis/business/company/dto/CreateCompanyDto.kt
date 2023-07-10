package org.generis.business.company.dto


data class CreateCompanyDto(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var currency: String
)
