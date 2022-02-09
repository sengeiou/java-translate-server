package com.cretin.webservices.service.api

import javax.servlet.http.HttpServletRequest

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: TokenService
 * Author: cretin
 * Date: 2021/3/12 2:19 下午
 * Description: token
 */
interface TokenService {
    fun getToken(userId: Int): String

    fun getIdByToken(httpServletRequest: HttpServletRequest, ignoreErr: Boolean = false): Int
}