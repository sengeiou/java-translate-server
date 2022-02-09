package com.cretin.webdb.request

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: DingdingMsgRequest
 * Author: cretin
 * Date: 2021/8/3 9:07 上午
 * Description: 获取云端翻译列表 clientType 0 Android 1 iOS
 */
data class TranslateListClientRequest(val clientType: Int, val token: String? = "")