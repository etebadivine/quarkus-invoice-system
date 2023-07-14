package org.generis.business.logs.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.generis.base.exception.ServiceException
import org.generis.business.logs.dto.CreateLogDto
import org.generis.business.logs.dto.LogResponseDto
import org.generis.business.logs.repo.Log
import org.modelmapper.ModelMapper


@Transactional
@ApplicationScoped
class LogServiceImpl : LogService {

    @Inject
    lateinit var entityManager: EntityManager

    @Inject
    lateinit var userService: UserService

    private val modelMapper = ModelMapper()

    override fun createLog(createLogDto: CreateLogDto) {
        val log = modelMapper.map(createLogDto, Log::class.java)
         entityManager.merge(log)
    }

    override fun getLogsByUserId(id: String): List<LogResponseDto?> {
        val logs = entityManager.createQuery("SELECT l FROM Customer l WHERE l.id = :id", Log::class.java)
        logs.setParameter("id", id)

        return logs.resultList.map {
            LogResponseDto(
                it.id,
                it.action,
                userService.getUserById(it.userId),
                it.target,
                it.createdDate
            )
        }
    }

    override fun getAllLogs(): List<LogResponseDto?> {
        val logs = entityManager.createQuery("SELECT l FROM Log l", Log::class.java)
            ?: throw ServiceException(-1, "No logs found")

        return logs.resultList.map {
            LogResponseDto(
                it.id,
                it.action,
                userService.getUserById(it.userId),
                it.target,
                it.createdDate
            )
        }

    }
}