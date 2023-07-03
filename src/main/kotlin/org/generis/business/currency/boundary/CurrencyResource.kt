package org.generis.business.currency.boundary

import jakarta.validation.Valid
import org.generis.base.domain.ApiResponse
import org.generis.business.currency.dto.CreateCurrencyDto
import org.generis.business.currency.dto.UpdateCurrencyDto
import org.generis.business.currency.repo.Currency


interface CurrencyResource {

    fun create(@Valid createCurrencyDto: CreateCurrencyDto): ApiResponse<Currency?>

    fun getCurrency(id: String): ApiResponse<Currency?>

    fun getAllCurrencies(): ApiResponse<List<Currency>>

    fun updateCurrency(id: String, updateCurrency: UpdateCurrencyDto): ApiResponse<Currency?>


}