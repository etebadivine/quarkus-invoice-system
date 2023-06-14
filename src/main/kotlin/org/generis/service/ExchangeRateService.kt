//package org.generis.service
//
//import jakarta.ws.rs.GET
//import jakarta.ws.rs.Path
//import jakarta.ws.rs.Produces
//import jakarta.ws.rs.QueryParam
//import jakarta.ws.rs.core.MediaType
//import org.generis.config.ExchangeRateResponse
//
//
//@Path("/exchange-rates")
//@Produces(MediaType.APPLICATION_JSON)
//interface  ExchangeRateService {
//
//    @GET
//    @Path("/latest.json")
//    fun getExchangeRates(@QueryParam("app_id") appId: String): ExchangeRateResponse
//
//}