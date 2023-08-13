package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.DashResponseBody

enum class AudioQuality(val code: Int) {
    Q64K(30216), Q132K(30232), Q192K(30280), Dolby(30250), HiRes(30251);
}

/**
 * 从 [DashResponseBody] 中获取音频拉流地址
 * @param responseBody
 * @param quality 音频的质量, 值见 [AudioQuality]
 * @return 该音频流对应的地址, 有效期一般只有 120min, 注意事项见 [getVideoStreamUrl]
 * @see [getVideoStreamUrl]
 */
fun getAudioStreamUrl(
    responseBody: DashResponseBody,
    quality: AudioQuality = AudioQuality.Q64K
): String {
    return responseBody.data.dash.audio.first { it.id == quality.code }.baseUrl
}

/**
 * @see getAudioStreamUrl
 */
fun DashResponseBody.audioStreamUrl(quality: AudioQuality = AudioQuality.Q64K) = getAudioStreamUrl(this, quality)

