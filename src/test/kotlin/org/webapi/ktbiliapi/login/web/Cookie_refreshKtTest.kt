package org.webapi.ktbiliapi.login.web

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Cookie_refreshKtTest {

    @Test
    fun getRefresh_Csrf() {
        runBlocking {
            val csrf = getRefreshCsrf()
            println(csrf)
        }
    }
}