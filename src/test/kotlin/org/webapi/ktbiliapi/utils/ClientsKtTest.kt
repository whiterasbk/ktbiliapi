package org.webapi.ktbiliapi.utils

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ClientsKtTest {

    @Test
    fun warpConnectUrlWithParams() = runBlocking {
        val url = warpGetConnectUrlWithParams {
            baseUrl("https://123.com")
            "ur"("223")
            "wid"(123)
        }.getCombinedUrl()

        assertEquals("https://123.com?ur=223&wid=123", url)

    }
}