package org.generis.business.country.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.country.dto.CreateCountryDto
import org.generis.business.country.dto.UpdateCountryExchangeRate
import org.generis.business.country.repo.Country
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.enums.LogAction
import org.generis.business.logs.service.JwtService
import org.generis.business.logs.service.LogService
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class CountryServiceImpl: CountryService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var jwtService: JwtService

    override fun createCountry(createCountryDto: CreateCountryDto): Country? {
        val country = modelMapper.map(createCountryDto, Country::class.java)
        country.persist()

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.CREATED_COUNTRY,
            target = country.countryName!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return country
    }

    override fun getCountry(id: String): Country? {
        return entityManager.find(Country::class.java, id) ?:
        throw ServiceException(-1, "No country found with id $id")
    }

    override fun getAllCountries(): List<Country> {
        val query = entityManager.createQuery("SELECT c FROM Country c", Country::class.java)
        return query.resultList ?: throw ServiceException(-1, "No country found")
    }

    override fun updateCountry(id: String, updateCountryExchangeRate: UpdateCountryExchangeRate): Country? {
        val country = entityManager.find(Country::class.java, id)
            ?: throw ServiceException(-1, "No country found with id $id")

        updateCountryExchangeRate.exchangeRate?.let { country.exchangeRate = it }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.UPDATED_COUNTRY_EXCHANGE_RATE,
            target = country.countryName!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return country
    }
}