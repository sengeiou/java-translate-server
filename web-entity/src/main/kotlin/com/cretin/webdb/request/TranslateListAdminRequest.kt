package com.cretin.webdb.request

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: DingdingMsgRequest
 * Author: cretin
 * Date: 2021/8/3 9:07 上午
 * Description: 获取云端翻译列表 clientType 0 全部 1 Android 2 iOS
 * translateState 0 全部 1 已翻译 2 未翻译
 * searchKey 搜索字符串
 * mergeStatus 0 所有 1 已合并 2 未合并
 * orderType 0 更新时间倒序 1 更新时间正序 2 添加时间倒序 3 添加时间正序
 */
data class TranslateListAdminRequest(val clientType: Int? = 0,
                                     val moduleKey: String? = "",
                                     val translateState: Int? = 0,
                                     val searchKey: String? = "",
                                     val mergeStatus: Int? = 0,
                                     val orderType: Int? = 0,
                                     val page: Long = 1,
                                     val searchType:Boolean? = false,
                                     val size: Long = 20)