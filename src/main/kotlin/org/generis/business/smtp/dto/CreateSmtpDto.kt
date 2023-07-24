package org.generis.business.smtp.dto

data class CreateSmtpDto(
    val authMethods: String,
    val from: String,
    val host: String,
    val port: Int,
    val ssl: Boolean,
    val userName: String,
    val password: String,
    val mock: Boolean
)
