package org.webapi.ktbiliapi.dsl

import org.webapi.ktbiliapi.utils.BeforeGetMethodHandler
import org.webapi.ktbiliapi.utils.usingBGMHandler

class StateDSLContext(private val handler: BeforeGetMethodHandler) {
    suspend operator fun <R> invoke(block: suspend () -> R): R {
        return usingBGMHandler(handler) {
            block()
        }
    }
}