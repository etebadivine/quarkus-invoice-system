package org.generis.business.logs.service

import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.dto.LogResponseDto
import org.generis.business.logs.repo.Log

interface LogService {

    fun createLog(createLogDto : CreateLogDto)

    fun getLogsByUserId(id : String): List<LogResponseDto?>

    fun getAllLogs():List<LogResponseDto?>
}