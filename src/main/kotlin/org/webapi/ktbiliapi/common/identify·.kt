package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.CidResponseBody
import org.webapi.ktbiliapi.utils.getMethod
import org.webapi.ktbiliapi.utils.toav

/**
 * 获取视频分P的cid
 * @param avid 视频 avid
 * @return [CidResponseBody] 数据实体, 包含了返回的所有信息
 */
suspend fun getCidResponseBody(avid: Long): CidResponseBody = getMethod {
    baseUrl = "https://api.bilibili.com/x/player/pagelist"
    "aid"(avid)
}

/**
 * 根据视频的 [avid] 获取其所有分P的 cid
 */
suspend fun getCidList(avid: Long): List<Long> {
    val body = getCidResponseBody(avid)
    return body.data.map { it.cid }
}

/**
 * 根据视频的 [bvid] 获取其所有分P的 cid
 */
suspend fun getCidList(bvid: String): List<Long> = getCidList(bvid.toav())