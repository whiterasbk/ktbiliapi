package org.webapi.ktbiliapi.sign

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AppkeyKtTest {

    @Test
    fun appSign() {
        val sign = appSign(AppKeySec.Pink1AndroidUniversalRes, mapOf(
            "id" to "114514",
            "str" to "1919810",
            "test" to "いいよ，こいよ",
        ))

        assertEquals("01479cf20504d865519ac50f33ba3a7d", sign)
    }
}