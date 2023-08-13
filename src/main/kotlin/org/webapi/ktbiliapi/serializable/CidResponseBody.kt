package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class CidResponseBody (
    val code: Int,
    val data: List<CidQueryData>,
    val message: String,
    val ttl: Int
) : CommonResponseBody() {
    init {
        iMessage = message
        iCode = code
    }
}

@Serializable
data class CidQueryData(
    val cid: Long,
    val dimension: Dimension,
    val duration: Long,
    val first_frame: String,
    val from: String,
    val page: Int,
    val part: String,
    val vid: String,
    val weblink: String
)

@Serializable
data class Dimension(
    val height: Int,
    val rotate: Int,
    val width: Int
)