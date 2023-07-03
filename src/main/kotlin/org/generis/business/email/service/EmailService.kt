package org.generis.business.email.service

import org.generis.business.email.dto.EmailRequestDto
import org.generis.business.email.dto.EmailResponseDto
import org.generis.business.email.repo.Email


interface EmailService {

    fun sendEmailWithAttachment(requestDto: EmailRequestDto): EmailResponseDto

    fun saveEmailToDatabase(requestDto: EmailRequestDto): Email

    fun sendEmail(requestDto: EmailRequestDto, emailId: String?)


}