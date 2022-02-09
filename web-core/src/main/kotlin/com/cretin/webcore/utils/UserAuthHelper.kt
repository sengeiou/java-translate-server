package com.cretin.webcore.utils

import com.cretin.webcore.exception.WholeException

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: UserAuthHelper
 * Author: cretin
 * Date: 2021/4/3 10:20 下午
 * Description: 验证用户身份
 */
object UserAuthHelper {

    /**
     * 检查用户的状态
     */
    fun checkUser(status: Int) {
        if (status == 1) {
            throw WholeException(203, "此账号已被封禁")
        } else if (status == 2) {
            throw WholeException(204, "此账户已被注销")
        }
    }
}