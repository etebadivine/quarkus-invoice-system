package org.generis.base.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun convertDateFromLongToString(date: Long): String {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(date), ZoneId.systemDefault()
    ).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
}