package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.DashResponseBody
import org.webapi.ktbiliapi.utils.getMethod
import org.webapi.ktbiliapi.utils.toav

suspend fun getDashResponseBody(avid: Long, cid: Long, fnval: Int = 16): DashResponseBody = getMethod {
    baseUrl = "https://api.bilibili.com/x/player/playurl"
    "fnval"(fnval)
    "avid"(avid)
    "cid"(cid)
}

suspend fun getDashResponseBody(bvid: String, cid: Long, fnval: Int = 16): DashResponseBody = getDashResponseBody(bvid.toav(), cid, fnval)

suspend fun getDashResponseBodies(avid: Long, fnval: Int = 16): List<DashResponseBody> = getCidList(avid).map {
    getDashResponseBody(avid, it, fnval)
}

suspend fun getDashResponseBodies(bvid: String, fnval: Int = 16): List<DashResponseBody> = getDashResponseBodies(bvid.toav(), fnval)

fun getActualVideoUrl(
    responseBody: DashResponseBody,
    filterQuality: (List<Int>) -> Int = { it.first() }
): String {
    return responseBody.data.dash.video.first { it.id == filterQuality(responseBody.data.dash.video.map {v -> v.id }) }.baseUrl
}