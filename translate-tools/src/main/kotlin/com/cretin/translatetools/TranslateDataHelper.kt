package com.cretin.translatetools

import com.cretin.translatetools.config.Config
import com.cretin.translatetools.entity.TranslateDownloadBean
import com.cretin.translatetools.entity.TranslateUploadBean
import com.cretin.translatetools.utils.GsonGet
import com.cretin.translatetools.utils.RestTemplateUtils
import com.google.gson.JsonObject
import org.springframework.web.client.RestTemplate

/**
 * 翻译数据帮助类
 */
object TranslateDataHelper {
    private val restTemplate by lazy { RestTemplate() }

    /**
     * 下载翻译数据
     */
    fun downloadTranslate(token: String): TranslateDownloadBean? {
        try {
            val url = Config.HOST + "/admin/translate/client/list"
            val params = mutableMapOf<String, Any>()
            params["clientType"] = 0
            params["token"] = token
            val result = RestTemplateUtils.doPost(restTemplate, url, params)
            return GsonGet.getGson().fromJson(result, TranslateDownloadBean::class.java)
        } catch (e: Throwable) {
            return TranslateDownloadBean().apply {
                this.code = 0
                this.msg = e.message
            }
        }
    }

    /**
     * 下载翻译数据
     */
    fun downloadTranslateIOS(token: String): TranslateDownloadBean? {
        try {
            val url = Config.HOST + "/admin/translate/client/list"
            val params = mutableMapOf<String, Any>()
            params["clientType"] = 1
            params["token"] = token
            val result = RestTemplateUtils.doPost(restTemplate, url, params)
            return GsonGet.getGson().fromJson(result, TranslateDownloadBean::class.java)
        } catch (e: Throwable) {
            return TranslateDownloadBean().apply {
                this.code = 0
                this.msg = e.message
            }
        }
    }


    /**
     * 上传本地翻译
     */
    fun uploadTranslate(data: TranslateUploadBean): String? {
        val url = Config.HOST + "/admin/translate/client/list/upload"
        try {
            return RestTemplateUtils.doPost(restTemplate, url, data)
        } catch (e: Throwable) {
            return e.message
        }
    }

    /**
     * 上传本地英文翻译
     */
    fun uploadEnTranslate(data: TranslateUploadBean): String? {
        val url = Config.HOST + "/admin/translate/client/list/upload/en"
        try {
            return RestTemplateUtils.doPost(restTemplate, url, data)
        } catch (e: Throwable) {
            return e.message
        }
    }
}

fun main(args: Array<String>) {
    var code = 1
    var start = 2021001
    while (code == 1){
        val result = RestTemplateUtils.doPost(RestTemplate(), "https://www.mxnzp.com/api/lottery/common/aim_lottery?expect=${start}&code=qlc&app_id=ixssxqertpltndez&app_secret=QUF5S2JLZkNqSHdyeVVLczdCNSt1QT09", null)
        val map = GsonGet.getGson().fromJson<MutableMap<String,Any>>(result, MutableMap::class.java)
        code = map["code"].toString().toDouble().toInt()
        start++
        println("正在执行 $start")
    }
    println("结束了")
}
