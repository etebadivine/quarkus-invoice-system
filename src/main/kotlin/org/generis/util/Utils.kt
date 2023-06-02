package org.generis.util

import org.generis.domain.*


    fun <T> wrapSuccessInResponse(data: T): ApiResponse<T> {
        if (UserRequestContext.getCurrentLanguage() == "fr")
            return ApiResponse(CODE_SERVICE_SUCCESS, CODE_SUCCESS, "Success", data)

        return ApiResponse(CODE_SERVICE_SUCCESS, CODE_SUCCESS, "Success", data)
    }

    fun <T> wrapFailureInResponse(message: String): ApiResponse<List<T>> {
        return ApiResponse(CODE_SERVICE_FAILURE, CODE_FAILURE, message, listOf())
    }