package org.webapi.ktbiliapi

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.javase.QRCode
import org.webapi.ktbiliapi.login.web.LoginViaQRCodeMethod
import java.io.File

fun main() {
    val viaQRCode = LoginViaQRCodeMethod()

    runBlocking {
        val loginDir = File("src/test/resources/login")

        if (!loginDir.exists()) loginDir.mkdir()

        val url = viaQRCode.getQRCodeUrl()
        File("src/test/resources/login/login.jpg").writeBytes(QRCode.from(url).to(ImageType.JPG).stream().toByteArray())
        println("获取二维码成功, 请打开 src/test/resources/login/login.jpg 使用客户端扫码")

        var state = ""
        val ctx = viaQRCode.login {
            if (state != it)
                println("登录状态: $it")
            state = it
        }

        val json = Json.encodeToString(ctx)
        File("src/test/resources/login/login.json").writeText(json)
        println("登录信息已存入 src/test/resources/login/login.json")
    }

}