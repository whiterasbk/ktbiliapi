package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class Dolby(
    val audio: String?,
    val type: Int
)