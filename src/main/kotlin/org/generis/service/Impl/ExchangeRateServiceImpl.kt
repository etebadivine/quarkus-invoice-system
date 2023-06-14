//package org.generis.service.Impl
//
//import jakarta.enterprise.context.ApplicationScoped
//import jakarta.inject.Inject
//import org.eclipse.microprofile.rest.client.RestClientBuilder
//import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
//import org.generis.config.ExchangeRateResponse
//import org.generis.exception.ServiceException
//import org.generis.service.ExchangeRateService
//
//
//
//@ApplicationScoped
//class ExchangeRateServiceImpl: ExchangeRateService {
//
//    @Inject
//    lateinit var restClientBuilder: RestClientBuilder
//
//    override fun getExchangeRates(appId: String): ExchangeRateResponse {
//        val exchangeRateService = restClientBuilder.build(ExchangeRateService::class.java)
//
//        return exchangeRateService.getExchangeRates(appId)
//            ?: throw ServiceException(-4, "Could not get exchange rates")
//    }
//}