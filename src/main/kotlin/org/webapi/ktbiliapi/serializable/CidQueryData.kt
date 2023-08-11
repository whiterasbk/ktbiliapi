package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

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