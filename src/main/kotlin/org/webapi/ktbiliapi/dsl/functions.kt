package org.webapi.ktbiliapi.dsl

import org.webapi.ktbiliapi.login.web.QRUserContext
import org.webapi.ktbiliapi.login.web.addQRCodeUserCookies
import org.webapi.ktbiliapi.sign.AppKeySec
import org.webapi.ktbiliapi.sign.WBICache
import org.webapi.ktbiliapi.sign.signViaAppKey
import org.webapi.ktbiliapi.sign.signViaWbiCached
import org.webapi.ktbiliapi.utils.SequenceOperateBGMHandlerFactory
import org.webapi.ktbiliapi.login.web.LoginViaQRCodeMethod

/**
 * 创建使用 wbi 方式签名的 dsl 上下文对象
 *
 * wbi 所使用的 img_url 和 sub_url 会被 [WBICache] 内部缓存, 使用 `WBICache.refresh()` 可以刷新这两个值
 *
 * 具体使用方法:
 * ```kotlin
 * val wbiCtx = usingWbiSign()
 * val responseBody = wbiCtx {
 *     getDashResponseBodies("bvid").first()
 * }
 * ```
 * 如此, 在访问 api 的时候, 将会自动使用 wbi 对参数进行签名
 */
fun usingWbiSign(): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateParams { signViaWbiCached(it) }
    return StateDSLContext(factory.getHandler())
}

/**
 * 创建使用 appKey 方式签名的 dsl 上下文对象
 *
 * 和 [usingWbiSign] 使用方式基本一致
 *
 * @param appKeySec 选取对应的 appkey 与 appsec, 见 [AppKeySec] 列出的所有备选值
 */
fun usingAppKeySign(appKeySec: AppKeySec): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateParams { signViaAppKey(appKeySec, it) }
    return StateDSLContext(factory.getHandler())
}

/**
 * 创建附带的 web 二维码登录信息的 dsl 上下文对象
 *
 * 使用方式:
 * ```kotlin
 * val method = LoginViaQRCodeMethod()
 * val qrcodeUrl = method.getQRCodeUrl()
 * val userCtx = method.login()
 * val loginCtx = usingWebQRCodeLogin(userCtx)
 * val responseBody = wbiCtx {
 *      getDashResponseBodies("bvid").first()
 * }
 * ```
 * @param userCtx 使用 [LoginViaQRCodeMethod] 或其他方式获得的 [QRUserContext], 将会在每次请求时附带用户登录信息
 */
fun usingWebQRCodeLogin(userCtx: QRUserContext): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateHeaders { addQRCodeUserCookies(userCtx, it) }
    return StateDSLContext(factory.getHandler())
}

suspend inline fun <reified T> runCatchingProviding(sdc: StateDSLContext, crossinline block: suspend () -> T): Result<T> {
    return runCatching { sdc { block() } }
}