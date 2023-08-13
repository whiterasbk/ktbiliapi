package org.webapi.ktbiliapi.common

import org.webapi.ktbiliapi.serializable.DashResponseBody
import org.webapi.ktbiliapi.utils.av
import org.webapi.ktbiliapi.utils.getMethod

/**
 * 获取视频的 dash 流信息
 * @param avid 视频的 avid
 * @param cid 分P 的 id 可以通过 [getCidList] 获得
 * @param fnval 清晰度, 默认为 16
 * @return [DashResponseBody] 数据类, 包含了该 api 所有的返回信息
 */
suspend fun getDashResponseBody(avid: Long, cid: Long, fnval: Int = 16): DashResponseBody = getMethod {
    baseUrl = "https://api.bilibili.com/x/player/playurl"
    "fnval"(fnval)
    "avid"(avid)
    "cid"(cid)
}

/**
 * 获取视频的 dash 流信息
 * @param avid 视频的 bvid
 * @param cid 分P 的 id 可以通过 [getCidList(avid)] 获得
 * @param fnval 清晰度, 默认为 16
 * @return [DashResponseBody] 数据类, 包含了该 api 所有的返回信息
 */
suspend fun getDashResponseBody(bvid: String, cid: Long, fnval: Int = 16): DashResponseBody = getDashResponseBody(bvid.av, cid, fnval)

/**
 * 获取视频所有分P的 dash 流信息
 * @param avid 视频的 avid
 * @param fnval 清晰度, 默认为 16
 * @return 返回一个列表, 该列表包含每P 的 [DashResponseBody] 数据类
 */
suspend fun getDashResponseBodies(avid: Long, fnval: Int = 16): List<DashResponseBody> = getCidList(avid).map {
    getDashResponseBody(avid, it, fnval)
}

/**
 * 获取视频所有分P的 dash 流信息
 * @param avid 视频的 bvid
 * @param fnval 清晰度, 默认为 16
 * @return 返回一个列表, 该列表包含每P 的 [DashResponseBody] 数据类
 */
suspend fun getDashResponseBodies(bvid: String, fnval: Int = 16): List<DashResponseBody> = getDashResponseBodies(bvid.av, fnval)

/**
 * 从 dash 流中获取视频拉流地址
 *
 * **关于鉴权**
 * - WEB 端取流需要验证防盗链，即 referer 为 .bilibili.com 域名下且 UA 不能为空
 * - APP 端也需要验证防盗链，即 UA 需要含有 Mozilla/5.0 BiliDroid/ *.*.* (bbcallen@gmail.com)（*为版本）
 *
 * 如 referer或 UA 错误的情况会被判定为盗链，返回403 Forbidden故无法取流
 *
 * 若传 platform=html5参数取流，则不会进行防盗链验证，即可通过 HTML 标签<video>播放
 *
 * [参考](https://socialsisteryi.github.io/bilibili-API-collect/docs/video/videostream_url.html#dash%E6%A0%BC%E5%BC%8F)
 * @param responseBody 从 [getDashResponseBody] 或者别的方式获取的 dash 数据实体
 * @param filterQuality 回调函数, 一般情况下 [responseBody] 里会有很多不同质量的流, 该 lambda 会将所有视频质量对应的 id map成一个 List 供使用者选择, 使用者只需在该 lambda 中返回其中一个 id 即可
 * @return 该视频流对应的地址, 有效期一般只有 120min
 */
fun getVideoStreamUrl(
    responseBody: DashResponseBody,
    filterQuality: (List<Int>) -> Int = { it.first() }
): String {
    return responseBody.data.dash.video.first { it.id == filterQuality(responseBody.data.dash.video.map {v -> v.id }) }.baseUrl
}

/**
 * @see getVideoStreamUrl
 */
fun DashResponseBody.videoStreamUrl(filterQuality: (List<Int>) -> Int = { it.first() }) = getVideoStreamUrl(this, filterQuality)