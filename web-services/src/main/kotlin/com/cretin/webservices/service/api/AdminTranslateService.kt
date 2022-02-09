package com.cretin.webservices.service.api

import com.cretin.webdb.request.TranslateConfirmAdminRequest
import com.cretin.webdb.request.*
import javax.servlet.http.HttpServletRequest

interface AdminTranslateService {
    /**
     * 获取翻译列表
     */
    fun translateList(data: TranslateListClientRequest, httpServletRequest: HttpServletRequest): Any

    /**
     * 上传内容进行翻译
     */
    fun uploadTranslateList(data: TranslateListUploadRequest, httpServletRequest: HttpServletRequest)

    /**
     * 上传英文版本的翻译
     */
    fun uploadEnTranslateList(data: TranslateListUploadRequest, httpServletRequest: HttpServletRequest)

    /**
     * 获取翻译列表
     */
    fun translateAdminList(data: TranslateListAdminRequest, httpServletRequest: HttpServletRequest): Any

    /**
     * 更新翻译
     */
    fun translateEditAdminList(data: TranslateEditAdminRequest, httpServletRequest: HttpServletRequest): Any

    /**
     * 获取翻译日志
     */
    fun translateLogList(data: TranslateLogAdminRequest, httpServletRequest: HttpServletRequest): Any

    /**
     * 合并翻译
     */
    fun translateMergeAdmin(data: TranslateMergeAdminRequest, httpServletRequest: HttpServletRequest)

    /**
     * 拆分翻译
     */
    fun translateSplitAdmin(data: TranslateSplitAdminRequest, httpServletRequest: HttpServletRequest)

    /**
     * 删除翻译
     */
    fun translateDeleteAdmin(data: TranslateSplitAdminRequest, httpServletRequest: HttpServletRequest)

    /**
     * 工具下载
     */
    fun translateToolsDownloadAdmin(httpServletRequest: HttpServletRequest): Any

    /**
     * 获取翻译模块
     */
    fun translateModuleList(httpServletRequest: HttpServletRequest): Any

    /**
     * 更新翻译模块列表
     */
    fun updateTranslateModulesList()

    /**
     * 更新翻译模块内容
     */
    fun translateUpdateModule(data: TranslateModuleUpdateAdminRequest, httpServletRequest: HttpServletRequest)

    /**
     * 用户登录
     */
    fun translateUserLogin(data: TranslateUserLoginRequest, httpServletRequest: HttpServletRequest): Any

    /**
     * 修改密码
     */
    fun translateUserChangePsw(data: TranslateUserChangePswRequest, httpServletRequest: HttpServletRequest)

    /**
     * 重置token
     */
    fun translateUserResetToken(httpServletRequest: HttpServletRequest)

    /**
     * 确认翻译
     */
    fun translateConfirmAdmin(data: TranslateConfirmAdminRequest, httpServletRequest: HttpServletRequest)
}