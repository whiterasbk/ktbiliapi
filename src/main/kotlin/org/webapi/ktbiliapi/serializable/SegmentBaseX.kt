package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class SegmentBaseX(
    val index_range: String,
    val initialization: String
)