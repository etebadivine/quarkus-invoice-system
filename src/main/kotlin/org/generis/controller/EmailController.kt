package org.generis.controller

import io.quarkus.scheduler.Scheduled
import io.quarkus.scheduler.Scheduler
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.generis.config.RecurringEmail
import org.generis.domain.ApiResponse
import org.generis.util.wrapSuccessInResponse


@Path("email")
@Produces(MediaType.APPLICATION_JSON)
class EmailController {

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    lateinit var recurringEmail: RecurringEmail

    @GET
    @Path("/send")
    @Produces(MediaType.TEXT_PLAIN)
    fun generateAndSendInvoices(): ApiResponse<Boolean> {
        recurringEmail.generateAndSendInvoices()

        return wrapSuccessInResponse(true)
    }
}