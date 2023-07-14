package org.generis.business.logs.service

import org.generis.business.logs.dto.UserInfo

interface JwtService {

    fun getUserInfo(): UserInfo
}