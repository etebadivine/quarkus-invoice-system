package org.generis.business.company.dto



data class CreateCompanyDto(
    var name: String,
    var email: String,
    var phoneNumber: String,
    var staff: List<CompanyStaffDto>,
    var address: String,
    var country: String
)
