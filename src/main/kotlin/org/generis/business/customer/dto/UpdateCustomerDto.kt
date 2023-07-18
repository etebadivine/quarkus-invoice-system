package org.generis.business.customer.dto

import lombok.Data

@Data
data class UpdateCustomerDto(
    var name: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var country: String? = null,
    var city: String? = null,
)