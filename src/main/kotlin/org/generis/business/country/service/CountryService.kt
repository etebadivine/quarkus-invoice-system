package org.generis.business.country.service

import org.generis.business.country.dto.CreateCountryDto
import org.generis.business.country.dto.UpdateCountryExchangeRate
import org.generis.business.country.repo.Country

interface CountryService {

    fun createCountry(createCountryDto: CreateCountryDto) : Country?

    fun getCountry(id : String) : Country?

    fun getAllCountries() : List<Country>

    fun updateCountry(id: String, updateCountryExchangeRate: UpdateCountryExchangeRate) : Country?
}