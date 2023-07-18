package org.generis.business.country.boundary.http

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.country.boundary.CountryResource
import org.generis.business.country.dto.CreateCountryDto
import org.generis.business.country.dto.UpdateCountryExchangeRate
import org.generis.business.country.repo.Country
import org.generis.business.country.service.CountryService
import org.slf4j.LoggerFactory


@Authenticated
@Path("country")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CountryResourceImpl: CountryResource {

    @Inject
    lateinit var countryService: CountryService

    private val logger = LoggerFactory.getLogger(CountryResourceImpl::class.java)

    @POST
    override fun createCountry(createCountryDto: CreateCountryDto): ApiResponse<Country?> {
        logger.info("http request: create")

        val country = countryService.createCountry(createCountryDto)

        val apiResponse = wrapSuccessInResponse(country)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getCountry(@PathParam("id") id: String): ApiResponse<Country?> {
        val country = countryService.getCountry(id)
        return wrapSuccessInResponse(country)
    }

    @GET
    override fun getAllCountries(): ApiResponse<List<Country>> {
        val countries = countryService.getAllCountries()
        return wrapSuccessInResponse(countries)
    }

    @PUT
    @Path("/{id}")
    override fun updateCountry(@PathParam("id") id: String,
        updateCountryExchangeRate: UpdateCountryExchangeRate
    ): ApiResponse<Country?> {
        val updatedExchangeRate = countryService.updateCountry(id, updateCountryExchangeRate)
        return wrapSuccessInResponse(updatedExchangeRate)
    }
}