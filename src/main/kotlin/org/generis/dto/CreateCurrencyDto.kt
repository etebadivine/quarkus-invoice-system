package org.generis.dto

data class CreateCurrencyDto(
    var currencyName: String,
    var exchangeRate: Double,
    var country: String,
)