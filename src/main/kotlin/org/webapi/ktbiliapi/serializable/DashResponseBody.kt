package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable

@Serializable
data class DashResponseBody(
    val code: Int,
    val data: DashQueryData,
    val message: String,
    val ttl: Int
) : CommonResponseBody() {
    init {
        iMessage = message
        iCode = code
    }
}

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

@Serializable
data class Dolby(
    val audio: List<Audio>?,
    val type: Int
)

@Serializable
data class SegmentBase(
    val Initialization: String,
    val indexRange: String
)

@Serializable
data class SupportFormat(
    val codecs: List<String>,
    val display_desc: String,
    val format: String,
    val new_description: String,
    val quality: Int,
    val superscript: String
)

@Serializable
data class SegmentBaseX(
    val index_range: String,
    val initialization: String
)

@Serializable
data class Video(
    val SegmentBase: SegmentBase,
    val backupUrl: List<String>,
    val backup_url: List<String>,
    val bandwidth: Int,
    val baseUrl: String,
    val base_url: String,
    val codecid: Int,
    val codecs: String,
    val frameRate: String,
    val frame_rate: String,
    val height: Int,
    val id: Int,
    val mimeType: String,
    val mime_type: String,
    val sar: String,
    val segment_base: SegmentBaseX,
    val startWithSap: Int,
    val start_with_sap: Int,
    val width: Int
)

@Serializable
data class Audio(
    val SegmentBase: SegmentBase,
    val backupUrl: List<String>,
    val backup_url: List<String>,
    val bandwidth: Int,
    val baseUrl: String,
    val base_url: String,
    val codecid: Int,
    val codecs: String,
    val frameRate: String,
    val frame_rate: String,
    val height: Int,
    val id: Int,
    val mimeType: String,
    val mime_type: String,
    val sar: String,
    val segment_base: SegmentBaseX,
    val startWithSap: Int,
    val start_with_sap: Int,
    val width: Int
)

@Serializable
data class Dash(
    val audio: List<Audio>,
    val dolby: Dolby,
    val duration: Int,
    val flac: Flac?,
    val minBufferTime: Double,
    val min_buffer_time: Double,
    val video: List<Video>
)

@Serializable
data class Flac(
    val display: Boolean,
    val audio: Audio
)