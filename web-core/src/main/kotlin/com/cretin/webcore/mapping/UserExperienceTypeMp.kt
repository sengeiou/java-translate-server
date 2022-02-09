package com.cretin.webcore.mapping

import com.cretin.webcore.config.AppConstants

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: UserExperienceTypeMp
 * Author: cretin
 * Date: 2021/3/17 5:18 下午
 * Description: 用户获取经验值的类型mapping
 */
object UserExperienceTypeMp {
    private val mapping = mutableMapOf<Int, Pair<Int, String>>()

    //登录成功获取经验值
    const val EX_TYPE_LOGIN = 1

    //点赞
    const val EX_TYPE_LIKE = 2

    //踩
    const val EX_TYPE_UNLIKE = 3

    //关乎
    const val EX_TYPE_ATTENTION = 4

    //评论
    const val EX_TYPE_COMMENT = 5

    //子评论
    const val EX_TYPE_COMMENT_ITEM = 6

    //回复子评论
    const val EX_TYPE_COMMENT_ITEM_REPLY = 61

    //点赞评论
    const val EX_TYPE_COMMENT_LIKE = 7

    //绑定邀请码
    const val EX_TYPE_BIND_CODE_INVITER = 8 //邀请者的奖励

    const val EX_TYPE_BIND_CODE = 9//被邀请者的奖励

    const val EX_TYPE_SIGNIN = 10//签到

    const val EX_TYPE_LOTTERY = 11//抽奖

    const val EX_TYPE_LOTTERY_COST = 12//抽奖消费

    const val EX_TYPE_POST_JOKES = 13//段子审核通过

    const val EX_TYPE_OLD_DATA = 14//数据迁移

    init {
        mapping[EX_TYPE_LOGIN] = Pair(6, "每日登录奖励")
        mapping[EX_TYPE_LIKE] = Pair(2, "点赞帖子")
        mapping[EX_TYPE_UNLIKE] = Pair(2, "踩帖子")
        mapping[EX_TYPE_ATTENTION] = Pair(2, "关注用户")
        mapping[EX_TYPE_COMMENT] = Pair(2, "发表评论")
        mapping[EX_TYPE_COMMENT_ITEM] = Pair(2, "回复评论")
        mapping[EX_TYPE_COMMENT_ITEM_REPLY] = Pair(2, "回复子评论")
        mapping[EX_TYPE_COMMENT_LIKE] = Pair(2, "点赞评论")
        mapping[EX_TYPE_BIND_CODE] = Pair(30, "绑定邀请码")
        mapping[EX_TYPE_BIND_CODE_INVITER] = Pair(80, "邀请用户奖励")
        mapping[EX_TYPE_SIGNIN] = Pair(6, "每日签到奖励")
        mapping[EX_TYPE_LOTTERY] = Pair(-1, "转盘抽奖获奖")
        mapping[EX_TYPE_LOTTERY_COST] = Pair(AppConstants.LOTTERY_COST, "转盘抽奖消费")
        mapping[EX_TYPE_POST_JOKES] = Pair(8, "帖子审核通过")
        mapping[EX_TYPE_OLD_DATA] = Pair(-1, "旧数据迁移")
    }

    /**
     * 根据类型和数量获取描述
     */
    fun getDescByTypeAndNum(type: Int, num: Int): String {
        val desc = mapping[type]?.second
        if(type == EX_TYPE_LOTTERY_COST){
            return desc?:""
        }
        if (num < 0) {
            return "取消" + desc
        } else {
            return desc?:""
        }
    }

    fun getNumByType(type: Int): Int {
        if (mapping.containsKey(type)) {
            return mapping[type]?.first ?: 0
        }
        return 0
    }

    fun getDescByType(type: Int): String {
        if (mapping.containsKey(type)) {
            return mapping[type]?.second ?: ""
        }
        return ""
    }

}