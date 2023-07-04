package org.generis.business.currency.dto

import lombok.Data


@Data
data class UpdateCurrencyDto(
    var exchangeRate: Double? = null
)