package org.generis.business.currency.service

import org.generis.business.currency.dto.CreateCurrencyDto
import org.generis.business.currency.dto.UpdateCurrencyDto
import org.generis.business.currency.repo.Currency


interface CurrencyService {

    fun createCurrency(createCurrencyDto: CreateCurrencyDto): Currency?

    fun getCurrency(id: String): Currency?
    fun getAllCurrencies(): List<Currency>

    fun updateExchangeRate(id: String?,updateCurrencyDto: UpdateCurrencyDto): Currency?

}