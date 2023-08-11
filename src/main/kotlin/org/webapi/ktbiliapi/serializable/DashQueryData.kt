package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class DashQueryData(
    val accept_description: List<String>,
    val accept_format: String,
    val accept_quality: List<Int>,
    val dash: Dash,
    val format: String,
    val from: String,
    val high_format: String?,
    val last_play_cid: Int,
    val last_play_time: Int,
    val message: String,
    val quality: Int,
    val result: String,
    val seek_param: String,
    val seek_type: String,
    val support_formats: List<SupportFormat>,
    val timelength: Int,
    val video_codecid: Int
)