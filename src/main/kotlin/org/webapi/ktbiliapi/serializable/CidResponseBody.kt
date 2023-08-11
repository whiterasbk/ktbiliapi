package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class CidResponseBody(
    val code: Int,
    val data: List<CidQueryData>,
    val message: String,
    val ttl: Int
)