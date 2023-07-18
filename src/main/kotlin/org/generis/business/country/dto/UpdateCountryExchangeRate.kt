package org.generis.business.country.dto

import lombok.Data


@Data
data class UpdateCountryExchangeRate(
    var exchangeRate: Double? = null
)