package org.generis.service.Impl

import io.quarkus.mailer.Attachment
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.dto.EmailRequestDto
import org.generis.dto.EmailResponseDto
import org.generis.entity.Email
import org.generis.service.EmailService
import java.io.ByteArrayInputStream
import java.io.File


@Singleton
@Transactional
class EmailServiceImpl: EmailService {
    @Inject
    lateinit var mailer: Mailer

    @Inject
    lateinit var entityManager: EntityManager

    override fun sendEmailWithAttachment(requestDto: EmailRequestDto): EmailResponseDto {
        val email = saveEmailToDatabase(requestDto)
        sendEmail(requestDto, email.id)
        return email.toDto()
    }


    override fun saveEmailToDatabase(requestDto: EmailRequestDto): Email {
        val email = Email()
        email.recipient = requestDto.recipient
        email.subject = requestDto.subject
        email. body = requestDto.body

        entityManager.persist(email)
        return email
    }

    override fun sendEmail(requestDto: EmailRequestDto, emailId: String?) {

        val attachmentBytes = File(requestDto.attachment).readBytes()

         mailer.send(Mail.withText(requestDto.recipient, requestDto.subject, requestDto.body)
             .addAttachment(requestDto.attachment, attachmentBytes, "text/plain"))
    }

    private fun Email.toDto(): EmailResponseDto {
        return EmailResponseDto(
            id = this.id,
            recipient = this.recipient,
            subject = this.subject,
            body = this.body,
            attachment = this.attachment,
        )
    }
}