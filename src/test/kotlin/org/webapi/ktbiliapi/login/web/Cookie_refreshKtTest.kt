package org.webapi.ktbiliapi.login.web

import getCorrespondPath
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.util.zip.GZIPInputStream

internal class Cookie_refreshKtTest {

    @Test
    fun getRefresh_Csrf() {
        runBlocking {
            val csrf = getRefreshCsrf()
            println(csrf)
        }
    }

    @Test
    fun test2() {
        val baseUrl = "https://www.bilibili.com/correspond/1/" + getCorrespondPath(System.currentTimeMillis())

        val url = URL(baseUrl);
        val connection = url.openConnection();

        connection.connect();

        val contentEncoding = connection.contentEncoding;
        if (contentEncoding != null && contentEncoding.equals("gzip", true)) {
            // Open a GZIP input stream to read the compressed content
            val gzipInputStream = GZIPInputStream(connection.getInputStream())
            val reader = InputStreamReader(gzipInputStream);
            val bufferedReader = BufferedReader(reader);

            val result = bufferedReader.readLines().joinToString("")


            // Print the uncompressed text content
            println(result);
        } else println("error")


//        val c = URL(baseUrl).openConnection()
//
//        c.connect()
//        println(c.contentEncoding)
//        println(c.contentLength)
//        println(c.contentType)
//        println(c.content)

    }
}