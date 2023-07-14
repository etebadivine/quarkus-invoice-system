package org.generis.business.logs.dto

import org.generis.business.logs.enums.LogAction
import java.time.LocalDateTime

data class LogResponseDto (
    var id: String?,
    var action: LogAction?,
    var userId:UserResponseDto?,
    var target: String?,
    val createdDate: LocalDateTime?

)

data class UserResponseDto(
    val id: String,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdOn: String
)

data class UserInfo(
    val id: String,
    val username: String,
    val email: String,
    val role: List<String>,
    )