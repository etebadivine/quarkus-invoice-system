package org.generis.controller


import io.quarkus.mailer.Attachment
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.quarkus.scheduler.Scheduled
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.generis.config.RecurringEmail
import org.generis.dto.EmailRequestDto
import org.generis.dto.EmailResponseDto
import org.generis.dto.SubscriptionDto
import org.generis.service.EmailService


@Path("send-mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EmailController {

    @Inject
    lateinit var emailService: EmailService

    @Inject
    lateinit var recurringEmail: RecurringEmail

    @POST
    fun sendEmail(requestDto: EmailRequestDto): Response {
            val responseDto: EmailResponseDto = emailService.sendEmailWithAttachment(requestDto)
            return Response.ok(responseDto).build()
        }
    @POST
    @Path("/recurring")
    fun sendRecurringEmails(){
        recurringEmail.sendRecurringInvoices()
    }
}
