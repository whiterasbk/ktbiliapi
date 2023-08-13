package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class WbiResponseBody (
    val code: Int,
    val data: WbiQueryData,
    val message: String,
    val ttl: Int
) : CommonResponseBody() {
    init {
        iMessage = message
        iCode = code
    }
}

@Serializable
data class WbiQueryData(
    val isLogin: Boolean,
    val wbi_img: FakeWebImage
)

@Serializable
data class FakeWebImage(
    val img_url: String,
    val sub_url: String
)
