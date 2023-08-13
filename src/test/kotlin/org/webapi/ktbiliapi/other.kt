package org.webapi.ktbiliapi

import kotlinx.serialization.json.Json
import org.webapi.ktbiliapi.common.getDashResponseBodies
import org.webapi.ktbiliapi.dsl.createWbiSignContext
import org.webapi.ktbiliapi.login.web.addQRCodeUserCookies
import org.webapi.ktbiliapi.sign.WBICache
import org.webapi.ktbiliapi.sign.signViaWbiCached
import org.webapi.ktbiliapi.utils.SequenceOperateBGMHandlerFactory
import org.webapi.ktbiliapi.utils.getMethod
import org.webapi.ktbiliapi.utils.usingBGMHandler

import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

fun main() {




    val modulusBase64 = "y4HdjgJHBlbaBN04VERG4qNBIFHP6a3GozCl75AihQloSWCXC5HDNgyinEnhaQ_4-gaMud_GF50elYXLlCToR9se9Z8z433U3KjM-3Yx7ptKkmQNAMggQwAVKgq3zYAoidNEWuxpkY_mAitTSRLnsJW-NCTa0bqBFF6Wm1MxgfE"
    val exponentBase64 = "AQAB"

    val timestamp = System.currentTimeMillis()
    val data = "refresh_$timestamp".toByteArray(StandardCharsets.UTF_8)

    val modulusBytes = Base64.getDecoder().decode(modulusBase64)
    val exponentBytes = Base64.getDecoder().decode(exponentBase64)

    val publicKeySpec = X509EncodedKeySpec(modulusBytes)
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec)

    val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)

    val encryptedData = cipher.doFinal(data)
    val encryptedHex = encryptedData.joinToString("") { it.toInt().and(0xFF).toString(16).padStart(2, '0') }

    println(encryptedHex)
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

    val wbi = createWbiSignContext()

    wbi {

    }
}

