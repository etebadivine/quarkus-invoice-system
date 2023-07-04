package org.generis.base.util

import org.generis.base.domain.ApiResponse
import org.generis.base.domain.UserRequestContext


fun <T> wrapSuccessInResponse(data: T): ApiResponse<T> {
        if (UserRequestContext.getCurrentLanguage() == "fr")
            return ApiResponse(
                org.generis.base.domain.CODE_SERVICE_SUCCESS,
                org.generis.base.domain.CODE_SUCCESS, "Success", data)

        return ApiResponse(
            org.generis.base.domain.CODE_SERVICE_SUCCESS,
            org.generis.base.domain.CODE_SUCCESS, "Success", data)
    }

    fun <T> wrapFailureInResponse(message: String): ApiResponse<List<T>> {
        return ApiResponse(
            org.generis.base.domain.CODE_SERVICE_FAILURE,
            org.generis.base.domain.CODE_FAILURE, message, listOf())
    }