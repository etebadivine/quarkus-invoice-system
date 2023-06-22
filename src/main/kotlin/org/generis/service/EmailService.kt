package org.generis.service

import org.generis.dto.EmailRequestDto
import org.generis.dto.EmailResponseDto
import org.generis.entity.Email

interface EmailService {

    fun sendEmailWithAttachment(requestDto: EmailRequestDto): EmailResponseDto

    fun saveEmailToDatabase(requestDto: EmailRequestDto): Email

    fun sendEmail(requestDto: EmailRequestDto, emailId: String?)


}