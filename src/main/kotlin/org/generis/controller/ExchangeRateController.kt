//package org.generis.controller
//
//import jakarta.inject.Inject
//import jakarta.ws.rs.GET
//import jakarta.ws.rs.Path
//import jakarta.ws.rs.Produces
//import jakarta.ws.rs.core.MediaType
//import org.generis.service.ExchangeRateService
//
//
//@Path("/exchange-rates")
//@Produces(MediaType.APPLICATION_JSON)
//class ExchangeRateController {
//
//    @Inject
//    lateinit var exchangeRateService: ExchangeRateService
//
//    @GET
//    @Path("/current")
//    fun getCurrentExchangeRates(): Map<String, Double> {
//        val appId = "1a914650457b40a2938d20cc426a8e0f"
//        val response = exchangeRateService.getExchangeRates(appId)
//        return response.rates
//    }
//}