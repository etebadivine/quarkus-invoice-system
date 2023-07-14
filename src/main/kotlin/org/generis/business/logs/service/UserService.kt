package org.generis.business.logs.service

import org.generis.business.logs.dto.UserResponseDto


interface UserService {

    fun getUserById(userId: String?): UserResponseDto?

    fun getUserByEmail(email: String): UserResponseDto?

    fun getAllUsers(): List<UserResponseDto>
}