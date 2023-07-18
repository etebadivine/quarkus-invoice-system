package org.generis.business.country.dto


data class CreateCountryDto(
    var countryName: String,
    var currency: String,
    var exchangeRate: Double,
    var currencyCode: String,
)