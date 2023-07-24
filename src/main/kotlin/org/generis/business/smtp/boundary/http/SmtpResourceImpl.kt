package org.generis.business.smtp.boundary.http

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.smtp.boundary.SmtpResource
import org.generis.business.smtp.dto.CreateSmtpDto
import org.generis.business.smtp.dto.UpdateSmtpDto
import org.generis.business.smtp.repo.Smtp
import org.generis.business.smtp.service.SmtpService
import org.slf4j.LoggerFactory


@Authenticated
@Path("smtp")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class SmtpResourceImpl: SmtpResource {

    private val logger = LoggerFactory.getLogger(SmtpResourceImpl::class.java)

    @Inject
    lateinit var smtpService: SmtpService

    @POST
    override fun setupEmailConfiguration(createSmtpDto: CreateSmtpDto): ApiResponse<Smtp> {
        logger.info("http request: create")

        val server = smtpService.createMailServer(createSmtpDto)

        val apiResponse = wrapSuccessInResponse(server)

        logger.info("http response: create: {}", apiResponse)

        return apiResponse
    }

    @PUT
    @Path("/{id}")
    override fun updateEmailCredentials(@PathParam("id") id: String, updateSmtpDto: UpdateSmtpDto): ApiResponse<Smtp?> {
        val updatedMailServer = smtpService.updateMailServer(id, updateSmtpDto)
        return wrapSuccessInResponse(updatedMailServer)
    }
}