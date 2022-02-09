package com.cretin.webcore.sms

import com.aliyun.dysmsapi20170525.Client
import com.aliyun.dysmsapi20170525.models.SendSmsRequest
import com.aliyun.teaopenapi.models.Config
import com.cretin.webcore.config.AppConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * FileName: SMSHelper
 * Author: cretin
 * Date: 2021/3/11 9:13 上午
 * Description:短信工具类
 */
@Service
class SMSHelper {

    @Autowired
    private var appConstants: AppConstants? = null

    private var client: Client? = null

    /**
     * 创建客户端
     */
    private fun createClient(): Client {
        val config = Config() // 您的AccessKey ID
                .setAccessKeyId(AppConstants.ALIYUN_SDK_AK) // 您的AccessKey Secret
                .setAccessKeySecret(AppConstants.ALIYUN_SDK_SK)
        // 访问的域名
        config.endpoint = AppConstants.ALIYUN_SMS_ENDPOINT
        return Client(config)
    }

    /**
     * 发送验证码
     */
    fun sendCodeMsg(phone: String, code: String): MutableMap<String, Any> {
        if (AppConstants.isDebug() == true) {
            return mutableMapOf<String,Any>(Pair("success",true),Pair("msg","验证码发送成功"),Pair("logMsg",""),Pair("logCode","OK"))
        }
        val sendSmsRequest = SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(AppConstants.ALIYUN_SMS_CODE_SIGN_NAME)
                .setTemplateCode(AppConstants.ALIYUN_SMS_CODE_TEMOCODE)
                .setTemplateParam("{\"code\":\"" + code + "\"}")
        (client ?: createClient()).let {
            val sendResp = it.sendSms(sendSmsRequest)
            if (sendResp.body.code == "OK") {
                //发送成功
                return mutableMapOf<String,Any>(Pair("success",true),Pair("msg","验证码发送成功"),Pair("logMsg",""),Pair("logCode","OK"))
            } else {
                //发送失败
                when (sendResp.body.code) {
                    "BUSINESS_LIMIT_CONTROL" -> {
                        return mutableMapOf<String,Any>(Pair("success",false),Pair("msg","验证码获取过快"),Pair("logMsg",sendResp.body.message),Pair("logCode",sendResp.body.code))
                    }
                    else -> {
                        return mutableMapOf<String,Any>(Pair("success",false),Pair("msg","验证码发送失败"),Pair("logMsg",sendResp.body.message),Pair("logCode",sendResp.body.code))
                    }
                }
            }
        }
    }
}