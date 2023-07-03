package org.generis.business.email.boundary.http


import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.generis.business.email.dto.EmailRequestDto
import org.generis.business.email.dto.EmailResponseDto
import org.generis.business.email.service.EmailService


@Path("send-mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EmailResourceImpl {

    @Inject
    lateinit var emailService: EmailService

    @POST
    fun sendEmail(requestDto: EmailRequestDto): Response {
            val responseDto: EmailResponseDto = emailService.sendEmailWithAttachment(requestDto)
            return Response.ok(responseDto).build()
        }
}
