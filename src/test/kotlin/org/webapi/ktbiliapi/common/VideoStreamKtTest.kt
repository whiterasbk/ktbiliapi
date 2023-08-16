package org.webapi.ktbiliapi.common

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.webapi.ktbiliapi.dsl.usingAppKeySign
import org.webapi.ktbiliapi.dsl.usingWbiSign
import org.webapi.ktbiliapi.sign.AppKeySec

internal class VideoStreamKtTest {

    @Test
    fun testGetActualVideoUrl() = runBlocking {

        val wbi = usingWbiSign()


        val app = usingAppKeySign(AppKeySec.AndroidVideoStream)

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