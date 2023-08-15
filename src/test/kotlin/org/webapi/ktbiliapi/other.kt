package org.webapi.ktbiliapi


import cn.edu.hfut.dmic.webcollector.model.CrawlDatum
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums
import cn.edu.hfut.dmic.webcollector.model.Page
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.util.Cookie
import getCorrespondPath
import io.ktor.client.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.webapi.ktbiliapi.dsl.usingWbiSign
import org.webapi.ktbiliapi.dsl.usingWebQRCodeLogin
import org.webapi.ktbiliapi.login.web.QRUserContext
import org.webapi.ktbiliapi.login.web.addQRCodeUserCookies
import org.webapi.ktbiliapi.sign.signViaWbiCached
import org.webapi.ktbiliapi.utils.GetMethodConfig
import org.webapi.ktbiliapi.utils.SequenceOperateBGMHandlerFactory
import org.webapi.ktbiliapi.utils.getMethod
import org.webapi.ktbiliapi.utils.usingBGMHandler
import java.io.File


fun main() {
    val url = "https://www.bilibili.com/correspond/1/0005dd006aee72ec103bfe256e37c74d3327aacc350274dee073edf1bc934502eb43bfecf1c87492662ceb4fdbe3313c9f4031efc3170272d710c1caaceedbaf1f41c77b2a19a1709c9eb1c7810879541aec457a82a99cc73edf0bdd091f4b0a33363acf3e4e0ba19f7220e4fa190d955b87891d064f2d40c5901f2bccf8c8c6"

    val webClient = WebClient()
    val options = webClient.options
    options.isJavaScriptEnabled = true
    options.isCssEnabled = false

    val qrUserContext = Json.decodeFromString<QRUserContext>(File("src/test/resources/login/login.json").readText())

    val ck = mutableMapOf<String, String>()

    addQRCodeUserCookies(qrUserContext, ck)

    ck.entries.forEach { (k, v) ->
        webClient.cookieManager.addCookie(com.gargoylesoftware.htmlunit.util.Cookie("www.bilibili.com", k, v))
    }

    val page: HtmlPage = webClient.getPage(url)

    println(page.visibleText)
    println(page.textContent)

    // 获取特定元素的 innerHTML
    println(page.body.textContent)
    println(page.body.visibleText)

}

suspend fun mainqs() {

    class SimpleWebCollector(crawlPath: String, autoParse: Boolean) :
        BreadthCrawler(crawlPath, autoParse) {
        init {
            addSeed("https://www.bilibili.com/correspond/1/0005dd006aee72ec103bfe256e37c74d3327aacc350274dee073edf1bc934502eb43bfecf1c87492662ceb4fdbe3313c9f4031efc3170272d710c1caaceedbaf1f41c77b2a19a1709c9eb1c7810879541aec457a82a99cc73edf0bdd091f4b0a33363acf3e4e0ba19f7220e4fa190d955b87891d064f2d40c5901f2bccf8c8c6") // 替换为你要抓取的网页 URL
        }

        override fun visit(page: Page, next: CrawlDatums) {
            if (page.matchUrl("https://www.example.com.*")) { // 替换为你要抓取的网页 URL 匹配规则
                val title: String = page.doc().title()
                val url: String = page.url()
                println("Title: $title")
                println("URL: $url")
            }
        }
    }


    val crawlPath = "crawl_data" // 设置抓取数据存储路径
    val collector = SimpleWebCollector(crawlPath, true)
    collector.threads = 1 // 设置抓取线程数
    collector.start(1) // 启动抓取

}




suspend fun mainm() {
    val qrUserContext = Json.decodeFromString<QRUserContext>(File("src/test/resources/login/login.json").readText())

    val login = usingWebQRCodeLogin(qrUserContext)

    val response = login {
        getMethod<String>(GetMethodConfig(true, client = HttpClient {
            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
        })) {
            baseUrl = "https://www.bilibili.com/correspond/1/${getCorrespondPath(System.currentTimeMillis())}"
            println(baseUrl)
        }
    }

    println(response)
}

suspend fun maina() {
    val fac = SequenceOperateBGMHandlerFactory()

    fac.addOperateParams {
//        signViaAppKey(AppKeySec.AndroidVideoStream, it)
        signViaWbiCached(it)
    }

    fac.addOperateHeaders {
        addQRCodeUserCookies(Json.decodeFromString(""), it)
    }

    usingBGMHandler(fac.getHandler()) {
        getMethod<String> {
            baseUrl = ""
        }
        ""
    }

    val wbi = usingWbiSign()

    wbi {

    }
}

