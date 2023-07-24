package org.generis.business.smtp.dto

data class UpdateSmtpDto (
    val authMethods: String? = null ,
    val from: String? = null,
    val host: String? = null,
    val port: Int? = null,
    val ssl: Boolean? = null,
    val userName: String? = null,
    val password: String? = null,
    val mock: Boolean? = null
)