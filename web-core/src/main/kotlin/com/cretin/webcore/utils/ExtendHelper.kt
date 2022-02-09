package com.cretin.webcore.utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.servlet.http.HttpServletRequest

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: ExtendHelper
 * Author: cretin
 * Date: 2021/3/12 8:19 下午
 * Description: 扩展
 */

/**
 * 获取秒
 */
fun LocalDateTime.getSeconds(): Long {
    return this.toEpochSecond(ZoneOffset.of("+8"))
}

/**
 * 获取毫秒
 */
fun LocalDateTime.getMillis(): Long {
    return this.toInstant(ZoneOffset.of("+8")).toEpochMilli()
}

/**
 * 获取渠道
 */
fun HttpServletRequest.getChannel(): String? {
    return this.getHeader("channel")
}

/**
 * 获取唯一id
 */
fun HttpServletRequest.getUniqueId(): String? {
    return this.getHeader("uk")
}

/**
 * 获取app信息
 */
fun HttpServletRequest.getAppInfo(): String? {
    return this.getHeader("app") +" "+this.getHeader("device")
}

/**
 * 获取当前用户版本号
 */
fun HttpServletRequest.getVersionCode(): Int {
    val appInfos = this.getHeader("app")?.split(";")
    if (appInfos?.size == 3) {
        return appInfos[1].toInt()
    }
    return -1
}

