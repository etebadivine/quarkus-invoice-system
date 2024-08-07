package org.generis.business.company.dto

import lombok.Data

@Data
data class UpdateCompanyDto(
    var name: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var address: String? = null,
    var country: String? = null
)