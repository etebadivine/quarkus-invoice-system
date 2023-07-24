package org.generis.business.smtp.service

import org.generis.business.smtp.dto.CreateSmtpDto
import org.generis.business.smtp.dto.UpdateSmtpDto
import org.generis.business.smtp.repo.Smtp

interface SmtpService {

    fun createMailServer(createSmtpDto: CreateSmtpDto) : Smtp

    fun updateMailServer(id: String?, updateSmtpDto: UpdateSmtpDto) : Smtp

}