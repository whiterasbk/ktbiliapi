package org.webapi.ktbiliapi.common

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.webapi.ktbiliapi.dsl.runCatchingProviding
import org.webapi.ktbiliapi.dsl.usingWbiSign

internal class Video_detailKtTest {

    @Test
    fun getVideoDetail() = runBlocking {

        runCatchingProviding(usingWbiSign()) {

        }.getOrThrow()
    }
}