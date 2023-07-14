package org.generis.business.logs.boundary

import org.generis.base.domain.ApiResponse
import org.generis.business.logs.dto.LogResponseDto

interface LogResource {

    fun getAllLogs(): ApiResponse<List<LogResponseDto?>>

    fun getLogsByUserId(id: String) : ApiResponse<List<LogResponseDto?>>
}