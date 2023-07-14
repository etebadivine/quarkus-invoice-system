package org.generis.business.subscription.boundary.http

import io.quarkus.security.Authenticated
import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.exception.ServiceException
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.subscription.boundary.SubscriptionResource
import org.generis.business.subscription.dto.CreateSubscriptionDto
import org.generis.business.subscription.repo.Subscription
import org.generis.business.subscription.service.SubscriptionService
import org.slf4j.LoggerFactory

//@RolesAllowed("ROLE_ADMIN")
@Authenticated
@Path("subscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SubscriptionResourceImpl: SubscriptionResource {

    private val logger = LoggerFactory.getLogger(SubscriptionResourceImpl::class.java)

    @Inject
    lateinit var subscriptionService: SubscriptionService

    @POST
    override fun createSubscription(createSubscriptionDto: CreateSubscriptionDto): ApiResponse<Subscription?> {
        val subscription = subscriptionService.createSubscription(createSubscriptionDto)

        val apiResponse = wrapSuccessInResponse(subscription)
        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @GET
    @Path("{id}")
    override fun getSubscription(@PathParam("id") id: String): ApiResponse<Subscription?> {
        val subscription = subscriptionService?.getSubscription(id)
        return wrapSuccessInResponse(subscription)
    }

    @GET
    override fun getAllSubscriptions(): ApiResponse<List<Subscription>> {
        val subscriptions = subscriptionService.getAllSubscriptions()
        return wrapSuccessInResponse(subscriptions)
    }

    @GET
    @Path("/active")
    override fun getAllActiveSubscriptions(): ApiResponse<List<Subscription>> {
        val activeSubscriptions = subscriptionService.getAllActiveSubscriptions()
        if (activeSubscriptions.isEmpty()) {
            throw ServiceException(-1, "No subscriptions found")
        }
        return wrapSuccessInResponse(activeSubscriptions)
    }

    @PUT
    @Path("/cancel/{id}")
    override fun cancelSubscription(@PathParam("id") id: String): ApiResponse<String> {
        subscriptionService.cancelSubscription(id)
        return wrapSuccessInResponse("Subscription canceled successfully.")
    }

    @PUT
    @Path("/reactivate/{id}")
    override fun reactivateSubscription(@PathParam("id") id: String): ApiResponse<String> {
        subscriptionService.reactivateSubscription(id)
        return wrapSuccessInResponse("Subscription reactivated successfully.")
    }
}