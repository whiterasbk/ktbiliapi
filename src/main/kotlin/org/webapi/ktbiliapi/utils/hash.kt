package org.webapi.ktbiliapi.utils

import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.jvm.Throws

@Throws(NoSuchAlgorithmException::class)
fun String.md5(charset: Charset = Charsets.UTF_8): String = this.toByteArray(charset).md5()

@Throws(NoSuchAlgorithmException::class)
fun ByteArray.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this)
    val sb = StringBuilder()
    for (b in digest) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}