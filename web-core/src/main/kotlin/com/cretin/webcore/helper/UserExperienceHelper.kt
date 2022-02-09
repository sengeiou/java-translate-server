package com.cretin.webcore.helper

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: UserExperienceHelper
 * Author: cretin
 * Date: 2021/3/17 5:43 下午
 * Description:用户经验帮助类
 */
object UserExperienceHelper {

    /**
     * 根据用户经验获取用户等级
     */
    fun getUserRankByExperience(experience: Int): Int {
        if(experience<=0){
            return 1
        }
        when (experience) {
            in 0..20 -> {
                return 1
            }
            in 21..60 -> {
                return 2
            }
            in 61..120 -> {
                return 3
            }
            in 121..200 -> {
                return 4
            }
            in 201..400 -> {
                return 5
            }
            in 401..800 -> {
                return 6
            }
            in 801..1500 -> {
                return 7
            }
            in 1501..3000 -> {
                return 8
            }
            in 3001..6000 -> {
                return 9
            }
            in 6001..9000 -> {
                return 10
            }
            in 9001..18000 -> {
                return 11
            }
            in 18001..30000 -> {
                return 12
            }
            in 30001..54000 -> {
                return 13
            }
            in 54001..90000 -> {
                return 14
            }
            in 90001..120000 -> {
                return 15
            }
            in 120001..200000 -> {
                return 16
            }
            in 200001..700000 -> {
                return 17
            }
            else -> {
                return 18
            }
        }
    }

}