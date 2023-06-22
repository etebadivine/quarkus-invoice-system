package org.generis.dto

import org.generis.enums.Currency
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime


data class CustomerDto (
    var id: String?,
    var name: String?,
    var email: String?,
    var phoneNumber: String?,
    var country: String?,
    var city: String?,
    var taxNumber: String?,
    var currency: Currency?,
    var createdDate: LocalDateTime?
) {
    constructor() : this(
        null,
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
