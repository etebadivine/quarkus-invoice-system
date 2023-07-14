package org.generis.business.logs.boundary.http

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.generis.base.domain.ApiResponse
import org.generis.base.util.wrapSuccessInResponse
import org.generis.business.logs.boundary.LogResource
import org.generis.business.logs.dto.LogResponseDto
import org.generis.business.logs.service.LogService


@Path("logs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class LogResourceImpl : LogResource {

    @Inject
    lateinit var logService: LogService

    @GET
    override fun getAllLogs(): ApiResponse<List<LogResponseDto?>> {
        val logs = logService.getAllLogs()
        return wrapSuccessInResponse(logs)
    }

    @GET
    @Path("{id}")
    override fun getLogsByUserId(@PathParam("id")id: String): ApiResponse<List<LogResponseDto?>> {
        val logs = logService.getLogsByUserId(id)
        return wrapSuccessInResponse(logs)
    }
}