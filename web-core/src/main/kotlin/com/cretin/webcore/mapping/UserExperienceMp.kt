package com.cretin.webcore.mapping

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: UserExperienceMapping
 * Author: cretin
 * Date: 2021/3/17 5:13 下午
 * Description: 用户经验值映射表
 */
object UserExperienceMp {

    /**
     * 点赞获取经验值的数量
     */
    fun getLikeJokesExperience(): Int {
        return 2;
    }

    /**
     * 踩段子获取经验值的数量
     */
    fun getUnlikeJokesExperience(): Int {
        return 2;
    }


}