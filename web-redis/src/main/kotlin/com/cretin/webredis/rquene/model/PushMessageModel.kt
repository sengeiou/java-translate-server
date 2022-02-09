package com.cretin.webredis.rquene.model

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: ActiveCodeMessageModel
 * Author: cretin
 * Date: 2021/3/16 2:00 下午
 * Description:
 */
data class PushMessageModel(val userId: Int = 0, var type: Int = 0) {
    var targetId: Int = 0 //目标id
    var msgContent: String = "" //消息内容
}