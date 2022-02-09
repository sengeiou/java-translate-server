package com.cretin.webcore.oss

import com.cretin.webcore.config.AppConstants
import com.google.gson.Gson
import com.qiniu.common.QiniuException
import com.qiniu.http.Response
import com.qiniu.storage.Configuration
import com.qiniu.storage.Region
import com.qiniu.storage.UploadManager
import com.qiniu.storage.model.DefaultPutRet
import com.qiniu.util.Auth
import java.io.File


/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: QiniuAuthHelper
 * Author: cretin
 * Date: 2021/3/30 10:00 上午
 * Description: 七牛云的helper
 */
object QiniuAuthHelper {
    //本地存储的auth
    private var auth: Auth? = null
    private val preHost = "http://jokes-img.cretinzp.com/"
    private val preHostDev = "http://jokes-pic-dev.cretinzp.com/"
    private val preAvatarHost = "http://jokes-avatar.cretinzp.com/"
    private val preAvatarHostDev = "http://jokes-avatar-dev.cretinzp.com/"

    init {
        val accessKey = AppConstants.QINIU_AK
        val secretKey = AppConstants.QINIU_SK
        auth = Auth.create(accessKey, secretKey)
    }

    /**
     * 获取token
     */
    fun getToken(): String? {
        val bucket = if (AppConstants.isDebug()) "jokes-pic-dev" else "jokes-pic"
        return auth?.uploadToken(bucket, null, 3600L, null)
    }

    /**
     * 上传文件
     */
    fun uploadFile(localFilePath: File) {
        val bucket = "jokes-avatar"
        //构造一个带指定 Region 对象的配置类
        //构造一个带指定 Region 对象的配置类
        val cfg = Configuration(Region.region2())
        //...其他参数参考类注释
        val uploadManager = UploadManager(cfg)
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        val key: String? = "qiniu/jokes/avatar/default/"+localFilePath.name
        val auth = Auth.create(AppConstants.QINIU_AK, AppConstants.QINIU_SK)
        val upToken = auth.uploadToken(bucket)
        try {
            val response: Response = uploadManager.put(localFilePath, key, upToken)
            //解析上传成功的结果
            val putRet: DefaultPutRet = Gson().fromJson(response.bodyString(), DefaultPutRet::class.java)
            println(putRet.key)
            println(putRet.hash)
        } catch (ex: QiniuException) {
            val r: Response = ex.response
            System.err.println(r.toString())
            try {
                System.err.println(r.bodyString())
            } catch (ex2: QiniuException) {
                //ignore
            }
        }
    }

    /**
     * 获取头像token
     */
    fun getAvatarToken(): String? {
        val bucket = if (AppConstants.isDebug()) "jokes-avatar-dev" else "jokes-avatar"
        return auth?.uploadToken(bucket, null, 3600L, null)
    }

    /**
     * 获取加密之后的连接
     */
    fun urlGet(url: String): String? {
        return auth?.privateDownloadUrl((if (AppConstants.isDebug()) preHostDev else preHost) + url, 3600)
    }

    /**
     * 获取头像
     */
    fun avatarGet(url: String): String? {
        return (if (AppConstants.isDebug()) preAvatarHostDev else preAvatarHost) + url
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val map = mutableMapOf<String, String>()
        map.put("file", "dsdsds")
        map.put("ok", "ss")
        val list = mutableListOf<MutableMap<String, String>>()
        list.add(map)

        val result = list?.map { it["file"] }?.joinToString(separator = ",") { it.toString() }
        println(result)
    }
}