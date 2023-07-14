package org.generis.business.currency.boundary.http

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.currency.boundary.CurrencyResource
import org.generis.business.currency.dto.CreateCurrencyDto
import org.generis.business.currency.dto.UpdateCurrencyDto
import org.generis.business.currency.repo.Currency
import org.generis.business.currency.service.CurrencyService
import org.slf4j.LoggerFactory

@Authenticated
@Path("currency")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CurrencyResourceImpl: CurrencyResource {

    @Inject
    lateinit var currencyService: CurrencyService

    private val logger = LoggerFactory.getLogger(CurrencyResourceImpl::class.java)

    @POST
    override fun create(createCurrencyDto: CreateCurrencyDto): ApiResponse<Currency?> {
        logger.info("http request: create")

        val currency = currencyService.createCurrency(createCurrencyDto)

        val apiResponse = wrapSuccessInResponse(currency)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getCurrency(@PathParam("id") id: String): ApiResponse<Currency?> {
        val currency = currencyService?.getCurrency(id)
        return wrapSuccessInResponse(currency)
    }

    @GET
    override fun getAllCurrencies(): ApiResponse<List<Currency>> {
        val currencies = currencyService.getAllCurrencies()
        return wrapSuccessInResponse(currencies)
    }

    @PUT
    @Path("/{id}")
    override fun updateCurrency(
        @PathParam("id") id: String, updateCurrency: UpdateCurrencyDto
    ): ApiResponse<Currency?> {
        val updatedExchangeRate = currencyService.updateExchangeRate(id, updateCurrency)
        return wrapSuccessInResponse(updatedExchangeRate)
    }
}