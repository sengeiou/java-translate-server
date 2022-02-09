package com.cretin.webservices

import com.cretin.webcore.config.AppConstants
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.util.Base64Utils
import org.springframework.web.client.RestTemplate
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher
import java.security.KeyFactory
import java.util.Base64


/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: OneKeyLoginHelper
 * Author: cretin
 * Date: 2021/3/23 5:00 下午
 * Description: 一键登录帮助类
 */
object OneKeyLoginHelper {
    //一键登录 加密公钥
    private var rsaPublic = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDd20p4xaStai+EmAy7ykJMsRi/\n" +
            "uJkM2iKqkmMFGBrHw6Boc+YYqBLMXtytM1h4Iqfi1KB19gMCKqjlwDAExAsj1N4l\n" +
            "xqP8NAloKyinfGQUSyrCUds7AcM3pXzlqh2MhHhwSMki9Wm8sFnH59QSynOf2XEo\n" +
            "oMHtbrpbmIZ40F9X2QIDAQAB"

    //一键登录加密私钥
    private var rsaPrivate = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAN3bSnjFpK1qL4SYDLvKQkyxGL+4mQzaIqqSYwUYGsfDoGhz5hioEsxe3K0zWHgip+LUoHX2AwIqqOXAMATECyPU3iXGo/w0CWgrKKd8ZBRLKsJR2zsBwzelfOWqHYyEeHBIySL1abywWcfn" +
            "1BLKc5/ZcSigwe1uuluYhnjQX1fZAgMBAAECgYEAlKUG4qBZ067fo5FUhsXfYg8O" +
            "K/Z85TWKIIrwOUuLNekGorV0p3SFE9VDOfC7B9MAdWRNFnv8TAlVHIqGIctQQjRN" +
            "knJ33Ecn8e0xzWmM2XdV0qKs/MZz/j0mi7kzxnyIAAneICnr5yDHr+8u8342j0r1" +
            "dakusE9a/mqa9SYCMDkCQQDxqFDz0+zOz4HX/6upWe9LR6V4JrKdcntWwm/yyjA8" +
            "hXbLcpeasOSWUeBb6dvedEcFVzPaKAi38eMLENcB1yhnAkEA6wYgAiDKhjYsfUdD" +
            "AIlIFYVgXDJldgvL3gQiuF3GXSf9gGFjXwkzQdt/i0S3mOIVLcOY1M0epEVOdieF" +
            "j2JVvwJAA91FLiD9cHExCls0S4j8op+KrdkwkgP6pPCHCcLh8pXPyIEUm4yomx7E" +
            "D7Q1p+EEIbm7QonRWd+sWcjzJzxhkQJAXny76Z8dTRACmutHeLO4k05oCykHuYnM" +
            "kJnBC7ZHxbQKKs78kHy2asfAY1FhLsnWEewNSaCdrRVgEgiZnae/KQJADfanzccb" +
            "w/sjy8GyvyxfSkzeYCGCePKnpc+JOr3himy7E9FM6wya15Vbmup1lC0oLcJBtf25" +
            "yyaywl3/fhDQ7w=="

    //极光一键登录验证url
    private var jgOnekeyUrl = "https://api.verification.jpush.cn/v1/web/loginTokenVerify"

    private fun decrypt(cryptograph: String?, prikey: String?): String? {
        val keySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey))
        val privateKey: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)
        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val b: ByteArray = Base64.getDecoder().decode(cryptograph)
        return String(cipher.doFinal(b))
    }

    /**
     * 验证码token 并获取手机号
     */
    fun verifyObtainPhone(token: String): String? {
        try {
            val realToken = token.replace(" ","+")
            val appKeyAndMaster = AppConstants.JG_APP_KEY + ":" + AppConstants.JG_APP_SECRET
            val httpHeaders = HttpHeaders()
            httpHeaders.add("Authorization", "Basic " + String(Base64Utils.encode(appKeyAndMaster.toByteArray())))
            httpHeaders.add("Content-Type", "application/json")
            val params = mutableMapOf<String, String>()
            params.put("loginToken", realToken)
            val httpEntity = HttpEntity<MutableMap<String, String>>(params, httpHeaders)
            val restTemplate = RestTemplate()
            val result = restTemplate.postForEntity(jgOnekeyUrl, httpEntity, Map::class.java).body

            if (result != null) {
                val code = result["code"]
                if (code == 8000) {
                    val phone = result["phone"].toString()
                    val realPhone = decrypt(phone, rsaPrivate)
                    return realPhone
                }
            }
        }catch (e:Throwable){

        }
        return ""
    }
    //88xLAfwG0+PGvMkOHZ5zBS1TPlS3qdT4ieKxFIvvyADBeRy5L+658d25L+qBc/QiFcP/YYVpQtA0dfCsFzo3yr5sSHVBwaJ51sYgbu7S4PJhkHMwJwW7JgpdmDi3fzQGOrv40i64KrKTY5KyAPdZz3kUJbg+pY3xvJHRmffAw/yvT0KCoegb7aDvwrAdZWNtX8Q1aOGaIr7OZvGW3zdCP9HHDvsfjglXSbGRUifMsrA71fJDQuQjqVlUHYOtyG4LDvBe6lxci9Tyxyr8pgQcrMhy3ZMZMwCFteVSd8oGgO2NcGSXNxYarncY+c4vZrqczI4Zu0NAVX90QTgvmw1G2GNq+mw397cjdq7dsGw676Y=
    //88xLAfwG0 PGvMkOHZ5zBS1TPlS3qdT4ieKxFIvvyADBeRy5L 658d25L qBc/QiFcP/YYVpQtA0dfCsFzo3yr5sSHVBwaJ51sYgbu7S4PJhkHMwJwW7JgpdmDi3fzQGOrv40i64KrKTY5KyAPdZz3kUJbg pY3xvJHRmffAw/yvT0KCoegb7aDvwrAdZWNtX8Q1aOGaIr7OZvGW3zdCP9HHDvsfjglXSbGRUifMsrA71fJDQuQjqVlUHYOtyG4LDvBe6lxci9Tyxyr8pgQcrMhy3ZMZMwCFteVSd8oGgO2NcGSXNxYarncY c4vZrqczI4Zu0NAVX90QTgvmw1G2GNq mw397cjdq7dsGw676Y=
}
