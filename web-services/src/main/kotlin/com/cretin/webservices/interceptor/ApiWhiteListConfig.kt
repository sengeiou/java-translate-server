package com.cretin.webservices.interceptor

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: ApiWhiteListConfig
 * Author: cretin
 * Date: 2021/4/20 2:09 下午
 * Description: 请求白名单
 */
object ApiWhiteListConfig {

    /**
     * 检查请求是否需要被检验
     */
    fun checkApi(api: String): Boolean {
        if (api == "/user/login/onekey") {
            return false
        }
        return true
    }
}