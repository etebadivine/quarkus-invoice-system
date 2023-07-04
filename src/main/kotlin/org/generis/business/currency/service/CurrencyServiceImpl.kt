package org.generis.business.currency.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.currency.dto.CreateCurrencyDto
import org.generis.business.currency.dto.UpdateCurrencyDto
import org.generis.business.currency.repo.Currency
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class CurrencyServiceImpl: CurrencyService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

    override fun createCurrency(createCurrencyDto: CreateCurrencyDto): Currency? {
            val currency = modelMapper.map(createCurrencyDto, Currency::class.java)
            currency.persist()
            return currency
        }

    override fun getCurrency(id: String): Currency? {
        return entityManager.find(Currency::class.java, id) ?:
        throw ServiceException(-1, "No currency found with id $id")
    }

    override fun getAllCurrencies(): List<Currency> {
        val query = entityManager?.createQuery("SELECT c FROM Currency c", Currency::class.java)
        return query?.resultList ?: throw ServiceException(-1, "No currencies found")
    }

    override fun updateExchangeRate(id: String?, updateCurrencyDto: UpdateCurrencyDto): Currency? {
        val currency = entityManager.find(Currency::class.java, id)
            ?: throw ServiceException(-1, "No currency found with id $id")

        updateCurrencyDto.exchangeRate?.let { currency.exchangeRate = it }

        return currency
    }
}