package org.generis.controller

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.CreateCurrencyDto
import org.generis.entity.Currency
import org.generis.service.CurrencyService
import org.generis.util.wrapSuccessInResponse
import org.slf4j.LoggerFactory


@Path("currency")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class CurrencyController {

    @Inject
    lateinit var currencyService: CurrencyService

    private val logger = LoggerFactory.getLogger(CurrencyController::class.java)

    @POST
    fun create(@Valid createCurrencyDto: CreateCurrencyDto): ApiResponse<Currency> {
        logger.info("http request: create")

        val currency = currencyService.createCurrency(createCurrencyDto)

        val apiResponse = wrapSuccessInResponse(currency)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getCurrency(@PathParam("id") id: String): ApiResponse<Currency?> {
        val currency = currencyService?.getCurrency(id)
        return wrapSuccessInResponse(currency)
    }

}