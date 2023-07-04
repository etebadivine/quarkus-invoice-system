package org.generis.business.currency.dto

data class CreateCurrencyDto(
    var currencyName: String,
    var exchangeRate: Double,
    var country: String,
    var currencyCode: String
)