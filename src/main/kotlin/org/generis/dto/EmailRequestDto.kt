package org.generis.dto


data class EmailRequestDto(
    var recipient: String,

    var subject: String,

    var body: String,

    var attachment: String
    )

data class EmailResponseDto(
    val id: String?,
    val recipient: String?,
    val subject: String?,
    val body: String?,
    val attachment: String?
)