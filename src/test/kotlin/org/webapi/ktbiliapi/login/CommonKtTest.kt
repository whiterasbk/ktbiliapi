package org.webapi.ktbiliapi.login

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.webapi.ktbiliapi.login.web.QRUserContext
import org.webapi.ktbiliapi.login.web.getUserCtxFromParse

internal class CommonKtTest {
    @Test
    fun getUserCtxFromParse() {
        val ctx = getUserCtxFromParse("https://123.com?DedeUserID=1&DedeUserID__ckMd5=2&Expires=3&SESSDATA=4&bili_jct=5&refresh_token=6")
        assertEquals(ctx, QRUserContext("1", "2", "4", "5", 3, "6"))
    }
}