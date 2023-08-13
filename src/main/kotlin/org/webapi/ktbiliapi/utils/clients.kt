package org.webapi.ktbiliapi.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.webapi.ktbiliapi.serializable.CommonResponseBody
import java.util.StringJoiner

fun addCookie(cookie: String, headers: MutableMap<String, String>) {
    headers["Cookie"]?.let {
        headers["Cookie"] = "$it;$cookie"
    } ?: run {
        headers["Cookie"] = cookie
    }
}

var localBeforeGetMethodHandler: BeforeGetMethodHandler = DefaultBeforeGetMethodHandler()

interface BeforeGetMethodHandler {
    suspend fun handleParams(params: MutableMap<String, String>)

    suspend fun handleHeader(header: MutableMap<String, String>)
}

class DefaultBeforeGetMethodHandler : BeforeGetMethodHandler {
    override suspend fun handleParams(params: MutableMap<String, String>) {}

    override suspend  fun handleHeader(header: MutableMap<String, String>) {}
}

/**
 * 代表序列操作 参数和头的工厂
 */
class SequenceOperateBGMHandlerFactory {
    private val actionsForParams: MutableList<suspend (MutableMap<String, String>) -> Unit> = mutableListOf()
    private val actionsForHeaders: MutableList<suspend (MutableMap<String, String>) -> Unit> = mutableListOf()

    fun addOperateParams(a: suspend (MutableMap<String, String>) -> Unit) {
        actionsForParams.add(a)
    }

    fun addOperateHeaders(a: suspend (MutableMap<String, String>) -> Unit) {
        actionsForHeaders.add(a)
    }

    fun getHandler() = object : BeforeGetMethodHandler {
        override suspend fun handleParams(params: MutableMap<String, String>) {
            for (a in actionsForParams) a(params)
        }

        override suspend fun handleHeader(header: MutableMap<String, String>) {
            for (a in actionsForParams) a(header)
        }
    }
}

data class GetContextEntity(
    var baseUrl: String = "",
    var params: MutableMap<String, String> = mutableMapOf(),
    var headers: MutableMap<String, String> = mutableMapOf()
) {
    fun baseUrl(url: String) {
        this.baseUrl = url
    }
    operator fun String.invoke(value: Any) {
        params[this] = value.toString()
    }

    operator fun String.get(value: Any) {
        headers[this] = value.toString()
    }

    fun header(key: String, value: Any) {
        headers[key] = value.toString()
    }

    fun getCombinedUrl(): String {
        return if (params.isEmpty()) baseUrl else "$baseUrl?" + StringJoiner("&").let {
            params.forEach { (k, v) -> it.add("$k=$v") }
            (it.toString())
        }
    }
}

suspend fun usingBGMHandler(handler: BeforeGetMethodHandler, block: suspend () -> Unit) {
    val mutex = Mutex()
    mutex.withLock {
        val inner = localBeforeGetMethodHandler
        localBeforeGetMethodHandler = handler
        block()
        localBeforeGetMethodHandler = inner
    }
}

suspend fun warpGetConnectUrlWithParams(block: GetContextEntity.() -> Unit): GetContextEntity {
    return GetContextEntity().apply {
        block()
        localBeforeGetMethodHandler.handleParams(params)
        localBeforeGetMethodHandler.handleHeader(headers)
    }
}

class BiliApiException(val code: Int, val msg: String) : Exception(msg)

suspend inline fun <reified T> getMethodReturningHeaders(ignoreErrCode: Boolean = false, noinline block: GetContextEntity.() -> Unit): Pair<T, Headers> {
    val ctx = warpGetConnectUrlWithParams(block)

    // println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + ctx.headers)
    // println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + ctx.baseUrl)
    // println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + ctx.params)

    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }.config { BrowserUserAgent() }.use {

        val response = it.get(ctx.baseUrl) {
            ctx.params.forEach { (k, v) -> parameter(k, v) }
            ctx.headers.forEach { (k, v) -> header(k, v) }
            header("Referer", "https://www.bilibili.com")
        }

        val headers = response.headers
        val body = response.body<T>()

        if (!ignoreErrCode) {
            val rb = body as CommonResponseBody
            if (rb.iCode != 0) throw BiliApiException(rb.iCode, rb.iMessage)
        }

//        if (response.status.value == 404) {
//            throw Exception("404")
//        }

        body to headers
    }
}

suspend inline fun <reified T> getMethod(ignoreErrCode: Boolean = false, noinline block: GetContextEntity.() -> Unit): T {
    return getMethodReturningHeaders<T>(ignoreErrCode, block).first
}

