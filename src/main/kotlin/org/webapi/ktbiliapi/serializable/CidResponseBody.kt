package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable
import org.webapi.ktbiliapi.serializable.common.Dimension

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