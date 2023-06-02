package org.generis.domain

import lombok.Data
import lombok.NoArgsConstructor
import kotlinx.serialization.Serializable


@Data
@NoArgsConstructor
@Serializable
data class ApiResponse<T>(
     val systemCode: String,
     val code: String,
     val message: String,
     val data: T?
)



