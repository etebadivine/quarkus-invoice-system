package org.generis.service

import org.generis.dto.CreateCurrencyDto
import org.generis.dto.UpdateCurrencyDto
import org.generis.entity.Currency

interface CurrencyService {

    fun createCurrency(createCurrencyDto: CreateCurrencyDto): Currency?

    fun getCurrency(id: String): Currency?
    fun getAllCurrencies(): List<Currency>

    fun updateExchangeRate(id: String?,updateCurrencyDto: UpdateCurrencyDto): Currency?

}