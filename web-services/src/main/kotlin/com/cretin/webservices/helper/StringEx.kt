package com.cretin.webservices.helper

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.cretin.webcore.exception.WholeException
import com.cretin.webdb.entity.TbStrTranslateUser
import com.cretin.webdb.service.ITbStrTranslateUserService
import org.apache.http.util.TextUtils
import javax.servlet.http.HttpServletRequest

fun String.checkToken(strTranslateUserService: ITbStrTranslateUserService?) {
    if (TextUtils.isEmpty(this)) {
        throw WholeException("请输入正确的登录令牌")
    }
    val userQueryWrapper = QueryWrapper<TbStrTranslateUser>()
    userQueryWrapper.eq("token", this)
    val user = strTranslateUserService?.getOne(userQueryWrapper, false)
    if (user == null) throw WholeException("请输入正确的登录令牌")
    if (user.status != 0) throw WholeException("当前登录令牌已被限制使用")
}

/**
 * 获取token
 */
fun HttpServletRequest.getToken(): String {
    return this.getHeader("token") ?: ""
}

/**
 * 获取用户胡
 */
fun ITbStrTranslateUserService.getUser(token: String): TbStrTranslateUser? {
    val queryWrapper = QueryWrapper<TbStrTranslateUser>()
    queryWrapper.eq("token", token)
    return this.getOne(queryWrapper, false)
}