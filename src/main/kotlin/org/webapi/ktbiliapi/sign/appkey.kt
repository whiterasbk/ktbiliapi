package org.webapi.ktbiliapi.sign

import org.webapi.ktbiliapi.utils.md5
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

enum class AppKeySec(val key: String, val sec: String) {
    Ai4cCreatorAndroid("9d5889cf67e615cd", "8fd9bb32efea8cef801fd895bef2713d"),
    Pink1AndroidUniversalRes("1d8b6e7d45233436", "560c52ccd288fed045859ed18bffd973"),
    Pink1AndroidUserInfo("783bbb7264451d82", "2653583c8873dea268ab9386918b1d65\t"),
    Pink1AndroidOldVer("57263273bc6b67f6", "a0488e488d1567960d3a765e8d129f90"),
    AndroidVideoStream("iVGUTjsxvpLeuDCf", "aHRmhWMLkdeMuILqORnYZocwMBpMEOdt"),
    IOSVideoStream("YvirImLGlLANCLvM", "JNlZNgfNGKZEpaDTkCdPQVXntXhuiJEM"),
    Login("bca7e84c2d947ac6", "60698ba2f68e01ce44738920a0ffe768"),
}

fun appSign(aks: AppKeySec, params: Map<String, String>) = appSign(aks.key, aks.sec, params)

fun appSign(key: String, sec: String, params: Map<String, String>): String {
    // 为请求参数进行 APP 签名
    // 为请求参数进行 APP 签名
    val mutableParams = params.toMutableMap()
    mutableParams["appkey"] = key
    // 按照 key 重排参数
    // 按照 key 重排参数
    val sortedParams = TreeMap(mutableParams)
    // 序列化参数
    // 序列化参数
    val queryBuilder = StringBuilder()

    for ((key1, value) in sortedParams) {
        if (queryBuilder.isNotEmpty()) {
            queryBuilder.append('&')
        }
        queryBuilder
            .append(URLEncoder.encode(key1, StandardCharsets.UTF_8))
            .append('=')
            .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
    }

    return queryBuilder.append(sec).toString().md5()
}

fun signViaAppKey(key: String, sec: String, params: MutableMap<String, String>) {
    params["appkey"] = key
    params["sign"] = appSign(key, sec, params)
}

fun signViaAppKey(aks: AppKeySec, params: MutableMap<String, String>) = signViaAppKey(aks.key, aks.sec, params)

