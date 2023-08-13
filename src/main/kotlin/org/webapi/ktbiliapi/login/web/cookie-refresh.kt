package org.webapi.ktbiliapi.login.web

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.webapi.ktbiliapi.utils.getCorrespondPath

suspend fun getRefreshCsrf(): String {
    val correspondPath = getCorrespondPath(System.currentTimeMillis())

    println(">>>>>>")
    runBlocking {
        println("https://www.bilibili.com/correspond/1/${correspondPath}")

        println(HttpClient().get("https://www.bilibili.com/correspond/1/${correspondPath}").body<Any>())
    }
    val htmlPage = "" // getMethod<String>(true) {
//        baseUrl = "https://www.bilibili.com/correspond/1/${correspondPath}"
//
//
//
//    }
//
//    println(htmlPage)

    val divPattern = """<div\s+id="1-name">(.*?)</div>""".toRegex(RegexOption.DOT_MATCHES_ALL)
    val matchResult = divPattern.find(htmlPage)

    return matchResult?.groupValues?.get(1)?.trim() ?: "404"// throw Exception("404")
}