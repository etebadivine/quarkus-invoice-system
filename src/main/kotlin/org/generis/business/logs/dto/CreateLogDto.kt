package org.generis.business.logs.dto

import org.generis.business.logs.enums.LogAction

data class CreateLogDto (
    var action: LogAction,
    var target: String,
    var userId: String
)