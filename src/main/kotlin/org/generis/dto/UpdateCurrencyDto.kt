package org.generis.dto

import lombok.Data


@Data
data class UpdateCurrencyDto(
    var exchangeRate: Double? = null
)