package org.generis.business.logs.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.generis.base.util.convertDateFromLongToString
import org.generis.business.logs.dto.UserResponseDto
import org.hibernate.service.spi.ServiceException
import org.keycloak.admin.client.Keycloak


@ApplicationScoped
class UserServiceImpl: UserService {

    @Inject
    lateinit var keycloak: Keycloak

    override fun getUserById(userId: String?): UserResponseDto? {
        val user =
            keycloak.realm("fpi").users()[userId].toRepresentation()
                ?: throw ServiceException("Users table is empty")

        return UserResponseDto(
            id = user.id,
            userName = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            createdOn = convertDateFromLongToString(user.createdTimestamp),
        )
    }

    override fun getUserByEmail(email: String): UserResponseDto? {
        val user =
            keycloak.realm("fpi").users().searchByEmail(email, true)[0]
                ?: throw ServiceException("Users table is empty")

        return UserResponseDto(
            id = user.id,
            userName = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            createdOn = convertDateFromLongToString(user.createdTimestamp)
        )
    }

    override fun getAllUsers(): List<UserResponseDto> {
        val users =
            keycloak.realm("fpi").users().list()

        val userResponseDtoList =
            users.map {
                UserResponseDto(
                    id = it.id,
                    userName = it.username,
                    email = it.email,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    createdOn = convertDateFromLongToString(it.createdTimestamp),
                )
            }
        return userResponseDtoList
    }
}