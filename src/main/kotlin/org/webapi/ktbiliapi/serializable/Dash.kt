package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class Dash(
    val audio: List<Audio>,
    val dolby: Dolby,
    val duration: Int,
    val flac: String?,
    val minBufferTime: Double,
    val min_buffer_time: Double,
    val video: List<Video>
)