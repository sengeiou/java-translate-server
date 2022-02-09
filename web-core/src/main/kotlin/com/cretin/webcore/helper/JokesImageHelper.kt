package com.cretin.webcore.helper

import com.cretin.webcore.oss.OssUrlAuthHelper

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: JokesImageHelper
 * Author: cretin
 * Date: 2021/3/31 4:17 下午
 * Description: 处理段子的图片
 */
object JokesImageHelper {

    /**
     * 处理段子的图片
     */
    fun handlerJokesImageUrls(imageUrls: String): String {
        return imageUrls?.split(",")?.map {
            OssUrlAuthHelper.urlGet(it)
        }.joinToString(",") { it ?: "" }
    }
}