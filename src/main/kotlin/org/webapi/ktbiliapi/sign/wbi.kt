package org.webapi.ktbiliapi.sign

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.webapi.ktbiliapi.serializable.WbiResponseBody
import org.webapi.ktbiliapi.utils.GetMethodConfig
import org.webapi.ktbiliapi.utils.getMethod
import org.webapi.ktbiliapi.utils.md5
import java.net.URLEncoder
import java.util.*
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.collections.sortedBy
import kotlin.collections.toMutableMap

private val mixinKeyEncTab = intArrayOf(
    46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
    33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
    61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
    36, 20, 34, 44, 52
)

suspend fun getWbiResponseBody(): WbiResponseBody = getMethod(GetMethodConfig(true)) {
    baseUrl = "https://api.bilibili.com/x/web-interface/nav"
}

suspend fun getWebImg() = getWbiResponseBody().let { it.data.wbi_img.img_url to it.data.wbi_img.sub_url }

fun getMixinKey(imgKey: String, subKey: String): String {
    val s = imgKey + subKey
    val key = StringBuilder()
    for (i in 0..31) {
        key.append(s[mixinKeyEncTab[i]])
    }
    return key.toString()
}

suspend fun getMixinKey() = getWebImg().let { getMixinKey(it.first, it.second) }

data class WbiSignData(
    val wts: String,
    val w_rid: String
)

suspend fun wbiSign(params: Map<String, String>) = getWebImg().let { wbiSign(it.first, it.second, params) }

fun wbiSign(imgKey: String, subKey: String, params: Map<String, String>): WbiSignData {
    val mixinKey = getMixinKey(imgKey, subKey)
    val mutableParams = params.toMutableMap()
    val wts = (System.currentTimeMillis() / 1000).toString()
    mutableParams["wts"] = wts
    val stringParam = StringJoiner("&")

    mutableParams.entries.sortedBy { it.key }.forEach {
        stringParam.add("${it.key}=${URLEncoder.encode(it.value, "UTF-8")}")
    }

    return WbiSignData(wts ,(stringParam.toString() + mixinKey).md5())
}

fun signViaWbi(imgKey: String, subKey: String, params: MutableMap<String, String>) {
    val sign = wbiSign(imgKey, subKey, params)
    params["wts"] = sign.wts
    params["w_rid"] = sign.w_rid
}

object WBICache {

    private var webImg: Pair<String, String>? = null

    suspend fun getWebImgData(): Pair<String, String> {
        return if (webImg == null) {
            refresh()
            webImg!!
        } else webImg!!
    }

    suspend fun refresh() {
        webImg = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }.get("https://api.bilibili.com/x/web-interface/nav").body<WbiResponseBody>().let {
            it.data.wbi_img.img_url to it.data.wbi_img.sub_url
        }
    }
}

suspend fun signViaWbiCached(params: MutableMap<String, String>) {
    WBICache.getWebImgData().let {
        signViaWbi(it.first, it.second, params)
    }
}