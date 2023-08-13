package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.CidResponseBody
import org.webapi.ktbiliapi.utils.getMethod
import org.webapi.ktbiliapi.utils.toav

suspend fun getCidResponseBody(avid: Long): CidResponseBody = getMethod {
    baseUrl = "https://api.bilibili.com/x/player/pagelist"
    "aid"(avid)
    // datafyClient.get("https://api.bilibili.com/x/player/pagelist?aid=$avid").body()
}

suspend fun getCidList(avid: Long): List<Long> {
    val body = getCidResponseBody(avid)
    return body.data.map { it.cid }
}

suspend fun getCidList(bvid: String): List<Long> = getCidList(bvid.toav())