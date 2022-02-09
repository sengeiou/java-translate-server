package com.cretin.webcore.oss

import com.aliyun.oss.HttpMethod
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.GeneratePresignedUrlRequest
import com.cretin.webcore.config.AppConstants
import com.cretin.webcore.utils.DEStool
import com.cretin.webcore.utils.StringUtils
import org.apache.http.util.TextUtils
import java.util.*


object OssUrlAuthHelper {

    private var ossClient: OSS? = null

    init {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        val endpoint = AppConstants.ALIYUN_OSS_ENDPOINT
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        val accessKeyId = AppConstants.ALIYUN_SDK_AK
        val accessKeySecret = AppConstants.ALIYUN_SDK_SK

        // 创建OSSClient实例。
        ossClient = OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret)
    }

    /**
     * 获取默认的仓库的资源
     */
    fun urlGet(objectName: String?, bucketName: String = "jokes-bucket"): String? {
        val url = urlGet(objectName ?: "", bucketName, Date(Date().getTime() + 24 * 3600 * 1000))
        if (TextUtils.isEmpty(url)) {
            return ""
        }
        if (url?.startsWith("ftp://") == true) {
            return url
        }
        return "ftp://" + DEStool.encrptText(url)
    }

    /**
     * 获取云端完整头像
     */
    fun getAvatarUrl(avatar: String?): String {
        if (avatar?.startsWith("aliyun") == true) {
            return AppConstants.ALIYUM_AVATAR_HOST + avatar;
        } else if (avatar?.startsWith("qiniu") == true) {
            return QiniuAuthHelper.avatarGet(avatar ?: "") ?: ""
        } else {
            return avatar ?: ""
        }
    }

    /**
     * 获取默认的仓库的资源
     */
    fun urlGet(objectName: String?, bucketName: String = "jokes-bucket", expiration: Date): String? {
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        if (StringUtils.isEmpty(objectName)) {
            return ""
        }
        if (objectName?.startsWith("ftp://") == true) {
            return objectName
        }
        if (objectName?.startsWith("http") == true) {
            return objectName
        }
        if (objectName?.startsWith("qiniu") == true) {
            return QiniuAuthHelper.urlGet(objectName ?: "")
        }
        return ossClient?.generatePresignedUrl(bucketName, objectName, expiration, HttpMethod.GET)?.toString()
    }

    /**
     * 获取查询信息的接口
     */
    fun infoUrlGet(objectName: String, bucketName: String = "jokes-bucket"): String? {
        val req = GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET)
        // 设置URL过期时间为1小时。
        val expiration = Date(Date().getTime() + 24 * 3600 * 1000)
        req.expiration = expiration
        req.process = "image/info"
        return ossClient?.generatePresignedUrl(req)?.toString()
    }

    /**
     * 获取缩略图
     */
    fun urlThumbnailGet(objectName: String, bucketName: String = "jokes-bucket"): String? {
        val req = GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET)
        // 设置URL过期时间为1小时。
        val expiration = Date(Date().getTime() + 24 * 3600 * 1000)
        req.expiration = expiration
        req.process = "resize,w_400"
        return ossClient?.generatePresignedUrl(req)?.toString()
    }

    /**
     * 调用不到
     */
    fun onDestrpy() {
        // 关闭OSSClient。
        ossClient?.shutdown()
    }

}