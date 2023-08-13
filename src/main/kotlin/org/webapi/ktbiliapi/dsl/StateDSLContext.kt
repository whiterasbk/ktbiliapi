package org.webapi.ktbiliapi.dsl

import org.webapi.ktbiliapi.utils.BeforeGetMethodHandler
import org.webapi.ktbiliapi.utils.usingBGMHandler

class StateDSLContext(private val handler: BeforeGetMethodHandler) {
    suspend operator fun invoke(block: suspend () -> Unit) {
        usingBGMHandler(handler) {
            block()
        }
    }
}