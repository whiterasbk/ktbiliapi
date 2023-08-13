package org.webapi.ktbiliapi.common

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
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
                val url = getVideoStreamUrl(it)
                println(url)
            }
        }



    }

    @Test
    fun getActualVideoUrlSample() = runBlocking {
        val url = getDashResponseBodies("BV1HX4y177gi").first().videoStreamUrl {
            it.last()
        }
    }
}