package org.generis.business.smtp.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.enums.LogAction
import org.generis.business.logs.service.JwtService
import org.generis.business.logs.service.LogService
import org.generis.business.smtp.dto.CreateSmtpDto
import org.generis.business.smtp.dto.UpdateSmtpDto
import org.generis.business.smtp.repo.Smtp
import org.modelmapper.ModelMapper
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


@Transactional
@ApplicationScoped
class SmtpServiceImpl: SmtpService {

    @Inject
    lateinit var entityManager: EntityManager

    private val modelMapper = ModelMapper()

    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var jwtService: JwtService

    override fun createMailServer(createSmtpDto: CreateSmtpDto): Smtp {
        val smtp = modelMapper.map(createSmtpDto, Smtp::class.java)
        smtp.persist()

        // Save the provided email configurations to the application.properties file
        val properties = Properties()
//        val inputStream = FileInputStream("invoice-system/src/main/resources/application.properties")
        val inputStream = FileInputStream("src/main/resources/application.properties")
        properties.load(inputStream)
        inputStream.close()

        properties.setProperty("quarkus.mailer.auth-methods", smtp.authMethods)
        properties.setProperty("quarkus.mailer.from", smtp.from)
        properties.setProperty("quarkus.mailer.host", smtp.host)
        properties.setProperty("quarkus.mailer.port", smtp.port.toString())
        properties.setProperty("quarkus.mailer.ssl", smtp.ssl.toString())
        properties.setProperty("quarkus.mailer.username", smtp.userName)
        properties.setProperty("quarkus.mailer.password", smtp.password)
        properties.setProperty("quarkus.mailer.mock", smtp.mock.toString())

        try {
            val outputStream = FileOutputStream("src/main/resources/application.properties")
            properties.store(outputStream, null)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            throw ServiceException(-1, "Failed to set up email configurations.")
        }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.CREATED_MAIL_SERVER,
            target = smtp.host!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return smtp
    }

    override fun updateMailServer(id: String?, updateSmtpDto: UpdateSmtpDto): Smtp {
        val smtp = entityManager.find(Smtp::class.java, id)
            ?:  throw ServiceException(-1, "No server found with id $id")

        updateSmtpDto.authMethods?.let { smtp.authMethods = it }
        updateSmtpDto.from?.let { smtp.from = it }
        updateSmtpDto.host?.let { smtp.host = it }
        updateSmtpDto.port?.let { smtp.port = it }
        updateSmtpDto.userName?.let { smtp.userName = it }
        updateSmtpDto.password?.let { smtp.password = it }
        updateSmtpDto.mock?.let { smtp.mock = it }
        updateSmtpDto.ssl?.let { smtp.ssl = it }

        val user =  jwtService.getUserInfo()

        val createLog = CreateLogDto(
            action = LogAction.UPDATED_MAIL_SERVER,
            target = smtp.host!!,
            userId = user.id
        )
        logService.createLog(createLog)

        return smtp
    }
}