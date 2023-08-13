package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class LoginQRCodeResponseBody(
    val code: Int,
    val data: LoginQRCodeData,
    val message: String,
    val ttl: Int
) : CommonResponseBody() {
    init {
        iMessage = message
        iCode = code
    }
}

@Serializable
data class LoginQRCodeData(
    val qrcode_key: String,
    val url: String
)