/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: AppConstants
 * Author:   cretin
 * Date:     11/15/18 11:43
 * Description: 应用通用数据
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
</desc></version></time></author> */
package com.cretin.webcore.config

import com.cretin.webcore.helper.SpringContextUtil

/**
 * 〈应用通用数据〉
 *
 * @author cretin
 * @create 11/15/18
 * @since 1.0.0
 */
class AppConstants {

    companion object {
        /**
         * 通用错误信息
         */
        const val COMM_ERROR = "请求超时,请稍后再试"

        /**
         * 默认分页 数据条数
         */
        const val DEFAULT_PAGE_SIZE = 10

        /**
         * 抽奖花费积分数量
         */
        const val LOTTERY_COST = 30;

        /**
         * 常用时间格式器
         */
        const val COMM_TIME_FORMATER = "yyyy-MM-dd HH:mm:ss"

        /**
         * SIGNIN date format
         */
        const val SIGNIN_TIME_FORMATER = "yyyy-MM-dd"

        /**
         * aliyun 短信sdk accessKeyId
         */
        const val ALIYUN_SDK_AK = "LTAI4G33rJxqaDSGJy7BNMEQ"

        /**
         * aliyun 短信sdk accessKeySecret
         */
        const val ALIYUN_SDK_SK = "ddp3rSqEhcNIcGrOPkLA7JMhRuM0Wt"

        /**
         * aliyun oss endpoint
         */
        const val ALIYUN_OSS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com"

        /**
         * 阿里云存放头像的前缀
         */
        const val ALIYUM_AVATAR_HOST = "https://jokes-avatar.oss-cn-beijing.aliyuncs.com/"

        /**
         * aliyun sms endpoint
         */
        const val ALIYUN_SMS_ENDPOINT = "dysmsapi.aliyuncs.com"

        /**
         * 七牛云ak
         */
        const val QINIU_AK = "dDzvWsb8wd1RAL-icMJHfM_YonGrmtPhIIlIcL1K"

        /**
         * 七牛云sk
         */
        const val QINIU_SK = "G38dYTn10I_DvCZCwZsgjKPafMyiOActAA6Uxo7N"

        /**
         * aliyun sms template code
         */
        const val ALIYUN_SMS_CODE_TEMOCODE = "SMS_110450018"

        /**
         * aliyun sms sign name
         */
        const val ALIYUN_SMS_CODE_SIGN_NAME = "段子乐"

        /**
         * aliyun sms sign name
         */
        const val APP_DEFAULT_SIGNATURE = "他正在想一个爆炸的签名..."

        /**
         * 极光 appkey srcret
         */
        const val JG_APP_KEY = "9f1f00f124832dfa17c31a9b"
        const val JG_APP_SECRET = "87d623734c34f08cedcf2637"

        //qq群信息
        const val QQ_NUM = "935276640"
        const val QQ_KEY = "uCoxC-pS4rEaWkI0fRgdaO2kvPwMM0GA"

        /**
         * token 过期时间
         */
        const val USER_TOKEN_EXPIRED_TIME = 15 * 24 * 3600L

        /**
         * 获取rpc请求host
         */
        fun getRecommendedSystemRpcHost(): String {
            if (isDebug()) {
                return "http://localhost:9005/rpc_service"
//                return "https://www.mxnzp.com/jokes-temp/rpc_service"
            } else {
                return "http://localhost:9006/rpc_service"
//            return "http://localhost:9006/rpc_service"
            }
        }

        /**
         * 获取
         */
        fun getWebHostPre(): String {
            if (isDebug()) {
                return "https://www.mxnzp.com/jokes-web-dev"
            }
            return "https://www.mxnzp.com/jokes-web"
        }

        /**
         * 获取默认头像
         */
        fun getUserDefaultAvatar(nickname: String = "", userId: String = ""): String {
            return "aliyun/jokes/avatar/default_avatar.png";
        }

        /**
         * 是否校验参数 建议上线再开启
         */
        fun isSignRequest(): Boolean {
            if (isDebug()) {
                return false
            }
            return true
        }

        /**
         * 是否是测试环境
         */
        fun isDebug(): Boolean {
            return SpringContextUtil.getActiveProfile() == "dev"
        }

        /**
         * 获取IM前缀
         */
        fun getImPre(): String {
            return if (isDebug()) "test_" else "prod_"
        }
    }


}