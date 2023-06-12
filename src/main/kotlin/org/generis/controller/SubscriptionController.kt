package org.generis.controller

import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.*
import org.generis.entity.Subscription
import org.generis.service.SubscriptionService
import org.generis.util.wrapSuccessInResponse
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("subscriptions")
@Produces(MediaType.APPLICATION_JSON)
class SubscriptionController {

    private val logger = LoggerFactory.getLogger(SubscriptionController::class.java)
    private val modelMapper = ModelMapper()

    @Inject
    lateinit var subscriptionService: SubscriptionService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): ApiResponse<SubscriptionDto> {
        val subscription = subscriptionService.createSubscription(createSubscriptionDto)

        val subscriptionDto = modelMapper.map(subscription, SubscriptionDto::class.java)

        val apiResponse = wrapSuccessInResponse(subscriptionDto)
        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    fun getSubscription(@PathParam("id") id: String): ApiResponse<Subscription?> {
        val subscription = subscriptionService?.getSubscription(id)
        return wrapSuccessInResponse(subscription)
    }

    @GET
    fun getAllSubscriptions(): ApiResponse<List<SubscriptionDto>> {
        val subscriptions = subscriptionService.getAllSubscriptions()
        return wrapSuccessInResponse(subscriptions)
    }

//    @POST
//    @Path("/recurring")
//    @Consumes(MediaType.APPLICATION_JSON)
//    fun createRecurring(@Valid subscription: Subscription): ApiResponse<RecurringInvoiceDto> {
//        logger.info("http request: create")
//
//        val recurringInvoice = subscriptionService.createRecurringInvoiceForSubscription(subscription)
//
//        val recurringInvoiceDto = modelMapper.map(recurringInvoice, RecurringInvoiceDto::class.java)
//        val apiResponse = wrapSuccessInResponse(recurringInvoiceDto)
//
//        logger.info("http response: create: {}", apiResponse)
//
//        return apiResponse
//    }
}