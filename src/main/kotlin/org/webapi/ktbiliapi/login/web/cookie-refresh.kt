package org.webapi.ktbiliapi.login.web

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.utils.io.*
import org.webapi.ktbiliapi.utils.GetMethodConfig
import org.webapi.ktbiliapi.utils.getCorrespondPath
import org.webapi.ktbiliapi.utils.getMethod
import java.nio.ByteBuffer

@OptIn(InternalAPI::class)
suspend fun getRefreshCsrf(): String {

    val response = getMethod<HttpResponse> (GetMethodConfig(true)) {
        baseUrl = "https://www.bilibili.com/correspond/1/" + getCorrespondPath(System.currentTimeMillis())
    }

    println(response)
    println(response.status.value)
    if (response.status.value != 200) throw Exception(response.status.description)


    val channel = response.bodyAsChannel()
    val bb = ByteBuffer.allocate(channel.availableForRead)
    channel.readAvailable(bb)
    val array = bb.array()
    println(array.size)
    println(array.asList())

    val divPattern = """<div\s+id="1-name">(.*?)</div>""".toRegex(RegexOption.DOT_MATCHES_ALL)
    val matchResult = divPattern.find(response.bodyAsText())

    return matchResult?.groupValues?.get(1)?.trim() ?: "404"// throw Exception("404")
}