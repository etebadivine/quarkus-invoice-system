package org.generis.controller

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.domain.ApiResponse
import org.generis.dto.*
import org.generis.entity.Subscription
import org.generis.exception.ServiceException
import org.generis.service.SubscriptionService
import org.generis.util.wrapSuccessInResponse
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory


@Path("subscriptions")
@Produces(MediaType.APPLICATION_JSON)
class SubscriptionController {

    private val logger = LoggerFactory.getLogger(SubscriptionController::class.java)

    @Inject
    lateinit var subscriptionService: SubscriptionService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): ApiResponse<Subscription> {
        val subscription = subscriptionService.createSubscription(createSubscriptionDto)

        val apiResponse = wrapSuccessInResponse(subscription)
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
    fun getAllSubscriptions(): ApiResponse<List<Subscription>> {
        val subscriptions = subscriptionService.getAllSubscriptions()
        return wrapSuccessInResponse(subscriptions)
    }

    @GET
    @Path("/active")
    fun getAllActiveSubscriptions(): ApiResponse<List<Subscription>> {
        val activeSubscriptions = subscriptionService.getAllActiveSubscriptions()
        if (activeSubscriptions.isEmpty()) {
            throw ServiceException(-1, "No subscriptions found")
        }
        return wrapSuccessInResponse(activeSubscriptions)
    }

    @PUT
    @Path("/cancel/{id}")
    fun cancelSubscription(@PathParam("id") id: String): ApiResponse<String> {
        subscriptionService.cancelSubscription(id)
        return wrapSuccessInResponse("Subscription canceled successfully.")
    }

    @PUT
    @Path("/reactivate/{id}")
    fun reactivateSubscription(@PathParam("id") id: String): ApiResponse<String> {
        subscriptionService.cancelSubscription(id)
        return wrapSuccessInResponse("Subscription reactivated successfully.")
    }
}