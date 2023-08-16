package org.webapi.ktbiliapi.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

internal class CryptoKtTest {

    @Test
    fun testCorrespondPath() {

        val currentTimeMillis = Instant.now().toEpochMilli()

        val proxyClient = HttpClient {
            engine {
                proxy = ProxyBuilder.http("http://localhost:7890/")
            }
        }

        val json = runBlocking {
            val response = proxyClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "wasm-rsa.vercel.app"
                    path("api", "rsa")
                    parameters.append("t", (currentTimeMillis * 1000).toString())
                }
            }

            response.body<String>()
        }

        val hash = JSONObject(json).get("hash")

        runBlocking {
            val url = "https://www.bilibili.com/correspond/1/$hash"
            println(url)
            // val hp = HttpClient().get(url).body<String>()
            // println(hp)
        }

        val correspondPath = getCorrespondPath(currentTimeMillis)

        assertEquals(hash, correspondPath)
    }
}