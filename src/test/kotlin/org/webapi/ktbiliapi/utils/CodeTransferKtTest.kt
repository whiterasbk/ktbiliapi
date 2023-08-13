package org.webapi.ktbiliapi.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CodeTransferKtTest {

    @Test
    fun getAv() {
        assertEquals(872064693, "BV1JV4y1v72j".av)
    }

    @Test
    fun getBv() {
        assertEquals("BV1JV4y1v72j", 872064693L.bv)
    }
}