package org.generis.controller


import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.generis.config.RecurringEmail
import org.generis.dto.EmailRequestDto
import org.generis.dto.EmailResponseDto
import org.generis.service.EmailService


@Path("send-mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EmailController {

    @Inject
    lateinit var emailService: EmailService

    @POST
    fun sendEmail(requestDto: EmailRequestDto): Response {
            val responseDto: EmailResponseDto = emailService.sendEmailWithAttachment(requestDto)
            return Response.ok(responseDto).build()
        }
}
