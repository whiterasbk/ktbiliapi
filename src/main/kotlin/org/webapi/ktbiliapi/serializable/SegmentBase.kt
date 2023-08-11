package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class SegmentBase(
    val Initialization: String,
    val indexRange: String
)