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

/**
 * 全局私有处理器
 * @see BeforeGetMethodHandler
 */
private var localBeforeGetMethodHandler: BeforeGetMethodHandler = DefaultBeforeGetMethodHandler()

/**
 * 签名和头请求前处理器
 *
 * 该类代表在 GET 方法之前, 对其中的 参数(Parameter)和头(Header) 进行调整的动作
 *
 * 简单来说 只要调用了 [warpGetConnectUrlWithParams] 来对 [GetContextEntity] 进行设置, 在设置完成以后, 会再调用
 *
 * [BeforeGetMethodHandler] 的 [handleParams] 和 [handleHeader] 分别对 parameters 和 headers 进行处理, 然后才传递给 [getMethod] 进行最后的请求操作
 *
 * 以此来实现 对参数的调整 如添加新参数, 签名等, 对头的调整 如添加 Cookie, 设置 UA 等
 */
interface BeforeGetMethodHandler {
    suspend fun handleParams(params: MutableMap<String, String>)

    suspend fun handleHeader(header: MutableMap<String, String>)
}

/**
 * [localBeforeGetMethodHandler] 的默认值, 对于参数和头什么都不做
 */
class DefaultBeforeGetMethodHandler : BeforeGetMethodHandler {
    override suspend fun handleParams(params: MutableMap<String, String>) {}

    override suspend  fun handleHeader(header: MutableMap<String, String>) {}
}

/**
 * 序列操作签名和头请求前处理器产生工厂
 *
 * 可以添加多个对参数和头的处理
 *
 * 使用方式:
 * ```kotlin
 * val factory = SequenceOperateBGMHandlerFactory()
 * factory.addOperateParams { params ->
 *      // 对 参数进行处理
 * }
 * factory.addOperateHeaders { headers ->
 *      // 对 请求头进行处理
 * }
 * // 获取 handler
 * val handler = factory.getHandler()
 * ```
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

/**
 * Get 请求 的配置对象
 */
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

/**
 * 在自己的作用域切换 [localBeforeGetMethodHandler] 实例为自己提供的 [BeforeGetMethodHandler]
 *
 */
suspend fun <R> usingBGMHandler(handler: BeforeGetMethodHandler, block: suspend () -> R): R {
    val mutex = Mutex()
    val returnValue: R
    mutex.withLock {
        val inner = localBeforeGetMethodHandler
        localBeforeGetMethodHandler = handler
        returnValue = block()
        localBeforeGetMethodHandler = inner
    }

    return returnValue
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

