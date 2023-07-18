package org.generis.business.country.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.country.dto.CreateCountryDto
import org.generis.business.country.dto.UpdateCountryExchangeRate
import org.generis.business.country.repo.Country

interface CountryResource {

    fun createCountry(@Valid createCountryDto: CreateCountryDto): ApiResponse<Country?>

    fun getCountry(id: String): ApiResponse<Country?>

    fun getAllCountries(): ApiResponse<List<Country>>

    fun updateCountry(id: String, updateCountryExchangeRate: UpdateCountryExchangeRate): ApiResponse<Country?>
}