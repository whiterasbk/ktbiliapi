package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.VideoDetailResponseBody
import org.webapi.ktbiliapi.utils.getMethod

suspend fun getVideoDetail(avid: Long): VideoDetailResponseBody = getMethod {
    baseUrl = "https://api.bilibili.com/x/web-interface/view"
    "aid"(avid)
}

suspend fun getVideoDetail(bvid: String): VideoDetailResponseBody = getMethod {
    baseUrl = "https://api.bilibili.com/x/web-interface/view"
    "bvid"(bvid)
}

val VideoDetailResponseBody.cover: String get() = data.pic

val VideoDetailResponseBody.title: String get() = data.title

val VideoDetailResponseBody.pubdate: Long get() = data.pubdate


