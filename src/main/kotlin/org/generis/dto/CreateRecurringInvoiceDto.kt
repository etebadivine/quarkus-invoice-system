package org.generis.dto

import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import kotlinx.serialization.Serializable
import org.generis.enums.Currency


@Serializable
data class CreateRecurringInvoiceDto(
    var title: String? = null ,
    var subHeading: String? = null,
    var customerId: String,
    var subscription: String?,
    var currency: Currency?,
    var dueDate: String,
    var tax: Double? = 0.00,
    var discount: Double? = 0.00,
    var subTotal: Double? = null,
    var totalAmount: Double? = null,
)
