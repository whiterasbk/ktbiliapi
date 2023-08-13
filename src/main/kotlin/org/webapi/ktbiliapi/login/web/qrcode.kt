package org.webapi.ktbiliapi.login.web

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.webapi.ktbiliapi.serializable.LoginQRCodeResponseBody
import org.webapi.ktbiliapi.serializable.QueryLoginQRCodeResponseBody
import org.webapi.ktbiliapi.utils.addCookie
import org.webapi.ktbiliapi.utils.getMethod
import java.net.URL
import java.util.regex.Pattern

@Serializable
data class QRUserContext(
    val DedeUserID: String,
    val DedeUserID__ckMd5: String,
    val SESSDATA: String,
    val bili_jct: String,
    val Expires: Long,
    val refresh_token: String,
) {
    companion object {
        fun fromJson(json: String): QRUserContext = Json.decodeFromString(json)
    }

    fun toJson(): String = Json.encodeToString(this)
}

fun getUserCtxFromParse(urlString: String): QRUserContext {

    val dedeUserID = "DedeUserID=([^&]+)".toRegex().find(urlString)?.groupValues?.get(1)
    val dedeUserIDCkMd5 = "DedeUserID__ckMd5=([^&]+)".toRegex().find(urlString)?.groupValues?.get(1)
    val expires = "Expires=([^&]+)".toRegex().find(urlString)?.groupValues?.get(1)?.toLongOrNull()
    val sessdata = "SESSDATA=([^&]+)".toRegex().find(urlString)?.groupValues?.get(1)
    val biliJct = "bili_jct=([^&]+)".toRegex().find(urlString)?.groupValues?.get(1)
    val refreshToken = "refresh_token=([^&]+)".toRegex().find(urlString)?.groupValues?.get(1)

    return if (dedeUserID != null && dedeUserIDCkMd5 != null && expires != null &&
        sessdata != null && biliJct != null && refreshToken != null
    ) {
        QRUserContext(
            DedeUserID = dedeUserID,
            DedeUserID__ckMd5 = dedeUserIDCkMd5,
            Expires = expires,
            SESSDATA = sessdata,
            bili_jct = biliJct,
            refresh_token = refreshToken
        )
    } else throw Exception("Failed to extract all required parameters")
}

suspend fun getLoginQRCodeResponseBody(): LoginQRCodeResponseBody = getMethod {
    baseUrl = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate"
    // datafyClient.get("https://passport.bilibili.com/x/passport-login/web/qrcode/generate").body()
}


suspend fun queryQRCodeLonginResponseBody(qrcode_key: String): QueryLoginQRCodeResponseBody {
    return getMethod {
        baseUrl("https://passport.bilibili.com/x/passport-login/web/qrcode/poll")
        "qrcode_key"(qrcode_key)
    }
}

class QRCodeExpiredException : Exception("qrcode has been expired")

class LostQRCodeException : Exception("qrcode key should not be null")

class LoginViaQRCodeMethod {

    private var url: String? = null
    var key: String? = null
        private set

    suspend fun getQRCodeUrl(): String {
        val data = getLoginQRCodeResponseBody().data
        url = data.url
        key = data.qrcode_key
        return data.url
    }

    suspend fun login(callback: (String) -> Unit = {}) : QRUserContext {
        var userContext: QRUserContext? = null

        for (i in 1..90) {
            val body = queryQRCodeLonginResponseBody(key ?: throw LostQRCodeException())

            when(body.data.code) {
                0 -> {
                    // 扫码登录成功
                    userContext = getUserCtxFromParse(body.data.url + "&refresh_token=" +body.data.refresh_token )
                    break
                }

                86038 -> {
                    // 二维码已失效
                    throw QRCodeExpiredException()
                }

                86101 -> {
                    // 未扫码
                    callback("未扫码")
                }

                86090 -> {
                    // 二维码已扫码未确认
                    callback("二维码已扫码未确认")
                }

            }

            delay(2000)
        }

        return userContext ?: throw Exception("get userContext failed")
    }
}

fun addQRCodeUserCookies(data: QRUserContext, headers: MutableMap<String, String>) {
    addCookie("SESSDATA=" + data.SESSDATA, headers)
    addCookie("DedeUserID=" + data.DedeUserID, headers)
    addCookie("DedeUserID__ckMd5=" + data.DedeUserID__ckMd5, headers)
    addCookie("bili_jct=" + data.bili_jct, headers)
}
