package com.cretin.webcore.mapping

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: UserMsgTypeMp
 * Author: cretin
 * Date: 2021/3/27 2:31 下午
 * Description: 用户消息类型mp
 */
object UserMsgTypeMp {

    /**
     * 通过经验类型获取消息类型
     */
    fun getMsgTypeByExperienceType(experienceType: Int): Int {
        return when (experienceType) {
            UserExperienceTypeMp.EX_TYPE_LIKE,
            UserExperienceTypeMp.EX_TYPE_UNLIKE -> {
                1
            }
            UserExperienceTypeMp.EX_TYPE_COMMENT,
            UserExperienceTypeMp.EX_TYPE_COMMENT_ITEM_REPLY,
            UserExperienceTypeMp.EX_TYPE_COMMENT_ITEM -> {
                2
            }
            UserExperienceTypeMp.EX_TYPE_ATTENTION -> {
                3
            }
            else -> {
                0
            }
        }
    }

    /**
     * 通过经验类型获取子消息类型
     */
    fun getMsgItemTypeByExperienceType(experienceType: Int): Int {
        return when (experienceType) {
            UserExperienceTypeMp.EX_TYPE_LIKE -> {
                10
            }
            UserExperienceTypeMp.EX_TYPE_UNLIKE -> {
                11
            }
            UserExperienceTypeMp.EX_TYPE_COMMENT_LIKE -> {
                12
            }
            UserExperienceTypeMp.EX_TYPE_COMMENT -> {
                20
            }
            UserExperienceTypeMp.EX_TYPE_COMMENT_ITEM -> {
                21
            }
            UserExperienceTypeMp.EX_TYPE_COMMENT_ITEM_REPLY -> {
                22
            }
            UserExperienceTypeMp.EX_TYPE_ATTENTION -> {
                30
            }
            else -> {
                0
            }
        }
    }

    /**
     * 根据子类型获取子类型的描述
     *
     * isCommentList 是否是用户的评论列表
     */
    fun getMsgItemTypeDescByType(msgItemType: Int, isCommentList: Boolean = false): String {
        return when (msgItemType) {
            10 -> {
                "赞了你的帖子"
            }
            11 -> {
                "踩了你的帖子"
            }
            12 -> {
                "赞了你的评论"
            }
            20 -> {
                if (isCommentList) {
                    "你评论了%s的帖子"
                } else {
                    "评论了你的帖子"
                }
            }
            21, 22 -> {
                if (isCommentList) {
                    "你回复了%s的评论"
                } else {
                    "回复了你的评论"
                }
            }
            30 -> {
                "关注了你"
            }
            else -> {
                ""
            }
        }
    }

    /**
     * 根据类型获取类型的描述
     */
    fun getMsgTypeDescByType(msgMainType: Int): String {
        return when (msgMainType) {
            1 -> {
                "赞 · 踩"
            }
            2 -> {
                "评论"
            }
            3 -> {
                "关注"
            }
            else -> {
                ""
            }
        }
    }
}