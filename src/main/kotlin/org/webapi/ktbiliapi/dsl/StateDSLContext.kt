package org.webapi.ktbiliapi.dsl

import org.webapi.ktbiliapi.utils.BeforeGetMethodHandler
import org.webapi.ktbiliapi.utils.DefaultBeforeGetMethodHandler
import org.webapi.ktbiliapi.utils.SequenceOperateBGMHandlerFactory
import org.webapi.ktbiliapi.utils.usingBGMHandler
import java.sql.Wrapper

class StateDSLContext (
    private var handler: BeforeGetMethodHandler = DefaultBeforeGetMethodHandler()
) {
    suspend operator fun <R> invoke(block: suspend () -> R): R {
        return usingBGMHandler(handler) {
            block()
        }
    }

    operator fun plus(next: StateDSLContext): StateDSLContext {
        val factory = SequenceOperateBGMHandlerFactory()
        factory.addOperateHeaders(handler::handleHeader)
        factory.addOperateParams(handler::handleParams)
        factory.addOperateHeaders(next.handler::handleHeader)
        factory.addOperateParams(next.handler::handleParams)
        handler = factory.getHandler()
        return this
    }
}