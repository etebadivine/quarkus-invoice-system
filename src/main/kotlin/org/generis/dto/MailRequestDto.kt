package org.generis.dto

import kotlinx.serialization.Serializable


@Serializable
data class MailRequestDto (
    val subject: String,
    val message: String,
    val recipients: String,
    val signature: String,
    val senderName: String,
    val senderEmail: String? = "Banbo",
    val password: String?= "",
    )
{
    fun toRequestJsonString(): String {
        return  "{" +
                "subject: '$subject', " +
                "message: '$message'," +
                "recipients: '$recipients'," +
                "signature: '$signature'," +
                "sender_name:'$senderName', " +
                "sender_email: '$senderEmail'," +
                "password:'$password" +
                "}"
    }
}

@Serializable
data class MailResponseDto(
    val msg: String,
    val code: String
)