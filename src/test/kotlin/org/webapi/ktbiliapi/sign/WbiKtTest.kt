package org.webapi.ktbiliapi.sign

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class WbiKtTest {

    @Test
    fun wbiSign() {
        val imgKey = "653657f524a547ac981ded72ea172057"
        val subKey = "6e4909c702f846728e64f6007736a338"

        val data = mapOf(
            "foo" to "one one four",
            "bar" to "五一四",
            "baz" to "1919810"
        )

        val sign = wbiSign(imgKey, subKey, data)

        val sign1 = runBlocking { wbiSign(data) }

        println(sign)
        println(sign1)
    }
}