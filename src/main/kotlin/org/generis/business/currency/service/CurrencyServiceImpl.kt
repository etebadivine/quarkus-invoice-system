package org.generis.business.currency.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.currency.dto.CreateCurrencyDto
import org.generis.business.currency.dto.UpdateCurrencyDto
import org.generis.business.currency.repo.Currency
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.enums.LogAction
import org.generis.business.logs.service.JwtService
import org.generis.business.logs.service.LogService
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class CurrencyServiceImpl: CurrencyService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var jwtService: JwtService


    override fun createCurrency(createCurrencyDto: CreateCurrencyDto): Currency? {
         val currency = modelMapper.map(createCurrencyDto, Currency::class.java)
             currency.persist()

         val user =  jwtService.getUserInfo()

         val createLog = CreateLogDto(
            action = LogAction.CREATED_CURRENCY,
            target = currency.currencyName!!,
            userId = user.id
        )
        logService.createLog(createLog)

            return currency
        }

    override fun getCurrency(id: String): Currency? {
        return entityManager.find(Currency::class.java, id) ?:
        throw ServiceException(-1, "No currency found with id $id")
    }

    override fun getAllCurrencies(): List<Currency> {
        val query = entityManager.createQuery("SELECT c FROM Currency c", Currency::class.java)
        return query.resultList ?: throw ServiceException(-1, "No currencies found")
    }

    override fun updateExchangeRate(id: String?, updateCurrencyDto: UpdateCurrencyDto): Currency? {
        val currency = entityManager.find(Currency::class.java, id)
            ?: throw ServiceException(-1, "No currency found with id $id")

        updateCurrencyDto.exchangeRate?.let { currency.exchangeRate = it }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.UPDATED_CURRENCY_EXCHANGE_RATE,
            target = currency.currencyName!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return currency
    }
}