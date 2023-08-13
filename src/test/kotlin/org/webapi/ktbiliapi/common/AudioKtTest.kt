package org.webapi.ktbiliapi.common

import javazoom.jl.player.advanced.AdvancedPlayer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.webapi.ktbiliapi.dsl.usingWbiSign
import java.io.BufferedInputStream
import java.net.URL

internal class AudioKtTest {

    @Test
    fun testGetActualAudioUrl() = runBlocking {
        val wbi = usingWbiSign()
        wbi {
            val bodies = getDashResponseBodies("BV1HX4y177gi")

            bodies.forEach {
                val url = getAudioStreamUrl(it)
                println(url)

                val audioUrl = URL(url)
                val inputStream = audioUrl.openStream()
                val bufferedInputStream = BufferedInputStream(inputStream)

                val player = AdvancedPlayer(bufferedInputStream)
                player.play()
            }
        }
    }

}
