package org.generis.business.smtp.boundary

import jakarta.validation.Valid
import jakarta.ws.rs.PathParam
import org.generis.base.domain.ApiResponse
import org.generis.business.smtp.dto.CreateSmtpDto
import org.generis.business.smtp.dto.UpdateSmtpDto
import org.generis.business.smtp.repo.Smtp


interface SmtpResource {

    fun setupEmailConfiguration(@Valid createSmtpDto: CreateSmtpDto): ApiResponse<Smtp>

    fun updateEmailCredentials(id: String, updateSmtpDto: UpdateSmtpDto): ApiResponse<Smtp?>
}