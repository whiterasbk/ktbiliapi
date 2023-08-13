package org.webapi.ktbiliapi.dsl

import org.webapi.ktbiliapi.login.web.QRUserContext
import org.webapi.ktbiliapi.login.web.addQRCodeUserCookies
import org.webapi.ktbiliapi.sign.AppKeySec
import org.webapi.ktbiliapi.sign.signViaAppKey
import org.webapi.ktbiliapi.sign.signViaWbiCached
import org.webapi.ktbiliapi.utils.SequenceOperateBGMHandlerFactory
import org.webapi.ktbiliapi.utils.addCookie

fun createWbiSignContext(): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateParams { signViaWbiCached(it) }
    return StateDSLContext(factory.getHandler())
}

fun createAppKeySignContext(appKeySec: AppKeySec): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateParams { signViaAppKey(appKeySec, it) }
    return StateDSLContext(factory.getHandler())
}

fun createQRCodeLoginContext(userCtx: QRUserContext): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateHeaders { addQRCodeUserCookies(userCtx, it) }
    return StateDSLContext(factory.getHandler())
}

fun createQRCodeLoginAndSignViaWbiContext(userCtx: QRUserContext): StateDSLContext {
    val factory = SequenceOperateBGMHandlerFactory()
    factory.addOperateParams { signViaWbiCached(it) }
    factory.addOperateHeaders { addQRCodeUserCookies(userCtx, it) }
    return StateDSLContext(factory.getHandler())
}