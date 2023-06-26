package org.generis.service.Impl

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.dto.CreateCurrencyDto
import org.generis.dto.UpdateCurrencyDto
import org.generis.entity.Currency
import org.generis.exception.ServiceException
import org.generis.service.CurrencyService
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