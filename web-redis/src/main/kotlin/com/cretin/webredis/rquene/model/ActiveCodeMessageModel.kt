package com.cretin.webredis.rquene.model

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: ActiveCodeMessageModel
 * Author: cretin
 * Date: 2021/3/16 2:00 下午
 * Description:
 */
data class ActiveCodeMessageModel(val userId: Int = 0, var type: Int = 0, var plus: Boolean = false, var msg: String = "") {
    var targetUserId: Int = 0 //这条消息对应的目标用户
    var targetId: Int = 0 //目标id 如果操作的是内容 就是内容id 是子评论就是父评论的id
    var msgContent: String = "" //消息内容 如果是评论类型 就是评论的内容 其他类型就是空

    //记录实际发放的积分值
    var realNum = -1
    //记录实际的消费信息
    var realTips = ""

    var jokeId:Int = 0 //jokeId
    var commentId:Int = 0 //评论id
}