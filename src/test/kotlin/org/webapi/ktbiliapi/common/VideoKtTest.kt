package org.webapi.ktbiliapi.common

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.webapi.ktbiliapi.dsl.createAppKeySignContext
import org.webapi.ktbiliapi.dsl.createWbiSignContext
import org.webapi.ktbiliapi.sign.AppKeySec

internal class VideoKtTest {

    @Test
    fun testGetActualVideoUrl() = runBlocking {

        val wbi = createWbiSignContext()

        val app = createAppKeySignContext(AppKeySec.AndroidVideoStream)

        wbi {
            val bodies = getDashResponseBodies("BV1HX4y177gi")

            bodies.forEach {
                val url = getActualVideoUrl(it)
                println(url)
            }
        }



    }
}