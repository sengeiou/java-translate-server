package com.cretin.webcore.helper

import java.math.BigDecimal


/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: NumHelper
 * Author: cretin
 * Date: 2021/3/24 1:41 下午
 * Description: 数据帮助类
 */
object NumFormatHelper {

    /**
     * 格式化数字
     */
    fun formatNum(num: Int): String {
        if (num < 1000) {
            return num.toString()
        }
        if (num < 10000) {
            return calculateNum(num, 1000) + "k"
        }
        return calculateNum(num, 10000) + "w"
    }

    private fun calculateNum(num: Int, beishu: Int): String {
        val b = BigDecimal(num.toString())
        val temp = StringBuilder(b.divide(BigDecimal(beishu.toString())).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
        while (temp.endsWith("0") || temp.endsWith(".")) {
            temp.delete(temp.length - 1, temp.length)
        }
        if (temp.length == 0) {
            return "0"
        }
        return temp.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(calculateNum(6427, 1000))
        println(calculateNum(0, 1000))
        println(calculateNum(6467, 1000))
        println(calculateNum(6422, 1000))
    }
}