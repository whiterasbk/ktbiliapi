package org.webapi.ktbiliapi.utils

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

fun importPublicKey(publicKeyPEM: String): PublicKey {
    val publicKeyBytes = publicKeyPEM
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\n", "")
        .trim()

    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKeySpec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBytes))
    return keyFactory.generatePublic(publicKeySpec)
}

//suspend fun importPublicKey(modulus: String, exponent: String): Cipher {
//    val publicKeySpec = RSAPublicKeySpec(
//        BigInteger(1, Base64.getDecoder().decode(modulus)),
//        BigInteger(1, Base64.getDecoder().decode(exponent))
//    )
//    val publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec)
//
//    val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
//    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
//
//    return cipher
//}
//
//suspend fun getCorrespondPath(timestamp: Long, cipher: Cipher): String {
//    val data = "refresh_$timestamp".toByteArray(StandardCharsets.UTF_8)
//    val encrypted = cipher.doFinal(data)
//    return encrypted.joinToString("") { it.toInt().and(0xFF).toString(16).padStart(2, '0') }
//}

fun getCorrespondPath(ts: Long): String {

    val publicKeyPEM = """
        -----BEGIN PUBLIC KEY-----
        MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLgd2OAkcGVtoE3ThUREbio0Eg
        Uc/prcajMKXvkCKFCWhJYJcLkcM2DKKcSeFpD/j6Boy538YXnR6VhcuUJOhH2x71
        nzPjfdTcqMz7djHum0qSZA0AyCBDABUqCrfNgCiJ00Ra7GmRj+YCK1NJEuewlb40
        JNrRuoEUXpabUzGB8QIDAQAB
        -----END PUBLIC KEY-----
    """.trimIndent()

    val publicKey = importPublicKey(publicKeyPEM)
    val cipher = Cipher.getInstance("RSA/ECB/OAEPPadding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val data = "refresh_$ts".toByteArray()
    val encrypted = cipher.doFinal(data)

    return encrypted.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }
}
