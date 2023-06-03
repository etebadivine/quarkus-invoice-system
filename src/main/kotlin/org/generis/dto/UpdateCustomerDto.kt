package org.generis.dto

import kotlinx.serialization.Serializable
import lombok.Data
import org.generis.enums.Currency

@Data
@Serializable
data class UpdateCustomerDto(
    var name: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var country: String? = null,
    var city: String? = null,
    var taxNumber: String? = null,
    var currency: Currency? = null
)