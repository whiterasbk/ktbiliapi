package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.DashResponseBody

enum class AudioQuality(val code: Int) {
    Q64K(30216), Q132K(30232), Q192K(30280), Dolby(30250), HiRes(30251);
}

fun getActualAudioUrl(
    responseBody: DashResponseBody,
    quality: AudioQuality = AudioQuality.Q64K
): String {
    return responseBody.data.dash.audio.first { it.id == quality.code }.baseUrl
}

