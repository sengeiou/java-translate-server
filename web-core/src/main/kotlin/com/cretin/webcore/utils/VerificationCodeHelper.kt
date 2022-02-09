package com.cretin.webcore.utils

import java.lang.StringBuilder
import kotlin.random.Random

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: VerificationCodeHelper
 * Author: cretin
 * Date: 2021/3/11 10:39 上午
 * Description: 验证码生成帮助类
 */
object VerificationCodeHelper {
    private var cahrsWithNum = "abcdefghijklmnopqrstuvwxyz0123456789"
    private var cahrs = "abcdefghijklmnopqrstuvwxyz"

    /**
     * 生成指定位数的随机数字验证码
     */
    fun createNumCode(num: Int = 6): String {
        val code = StringBuilder()
        for (i in 0 until num) {
            code.append(Random.nextInt(10))
        }
        return code.toString()
    }

    /**
     * 创建指定数量的字母加数字
     */
    fun createCharNumCode(num: Int = 6): String {
        val code = StringBuilder()
        for (i in 0 until 6) {
            code.append(cahrsWithNum[Random.nextInt(cahrsWithNum.length)])
        }
        return code.toString().toUpperCase()
    }

    /**
     * 创建指定数量的字母
     */
    fun createCharCode(num: Int = 6): String {
        val code = StringBuilder()
        for (i in 0 until 6) {
            code.append(cahrs[Random.nextInt(cahrs.length)])
        }
        return code.toString().toUpperCase()
    }
}