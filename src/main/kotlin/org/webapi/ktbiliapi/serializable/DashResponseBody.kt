package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class DashResponseBody(
    val code: Int,
    val data: DashQueryData,
    val message: String,
    val ttl: Int
)