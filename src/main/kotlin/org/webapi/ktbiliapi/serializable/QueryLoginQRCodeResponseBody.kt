package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class QueryLoginQRCodeResponseBody(
    val code: Int,
    val data: Data,
    val message: String,
    val ttl: Int
) : CommonResponseBody() {
    init {
        iMessage = message
        iCode = code
    }
}

@Serializable
data class Data(
    val code: Int,
    val message: String,
    val refresh_token: String,
    val timestamp: Long,
    val url: String
)