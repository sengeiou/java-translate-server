package com.cretin.webapi.controller

import com.cretin.webdb.common.Resp
import com.cretin.webdb.request.*
import com.cretin.webservices.service.api.AdminTranslateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


/**
 * 翻译系统
 */
@RestController
@RequestMapping("/admin/translate")
@ResponseBody
class AdminTranslateController {

    @Autowired
    private var adminTranslateService: AdminTranslateService? = null

    /**
     * 获取客户端翻译列表
     */
    @PostMapping("/client/list")
    fun translateList(@RequestBody data: TranslateListClientRequest,
                      httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateList(data, httpServletRequest))
    }

    /**
     * 客户端翻译数据上传
     */
    @PostMapping("/client/list/upload")
    fun uploadTranslateList(@RequestBody data: TranslateListUploadRequest,
                            httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.uploadTranslateList(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 客户端翻译数据上传 英文版 做覆盖用
     */
    @PostMapping("/client/list/upload/en")
    fun uploadEnTranslateList(@RequestBody data: TranslateListUploadRequest,
                              httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.uploadEnTranslateList(data, httpServletRequest)
        return Resp.createSuccess()
    }


    /**
     * 后台获取数据
     */
    @PostMapping("/list")
    fun translateAdminList(@RequestBody data: TranslateListAdminRequest,
                           httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateAdminList(data, httpServletRequest))
    }

    /**
     * 用户登录
     */
    @PostMapping("/user/login")
    fun translateUserLogin(@RequestBody data: TranslateUserLoginRequest,
                           httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateUserLogin(data, httpServletRequest))
    }

    /**
     * 用户修改密码
     */
    @PostMapping("/user/changepsw")
    fun translateUserChangePsw(@RequestBody data: TranslateUserChangePswRequest,
                               httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateUserChangePsw(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 用户重置token
     */
    @PostMapping("/user/resettoken")
    fun translateUserResetToken(httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateUserResetToken(httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 获取模块列表
     */
    @PostMapping("/modules")
    fun translateModuleList(httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateModuleList(httpServletRequest))
    }

    /**
     * 更新模块列表
     */
    @PostMapping("/modules/update/list")
    fun translateUpdateModuleList(httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.updateTranslateModulesList()
        return Resp.createSuccess()
    }

    /**
     * 更新模块
     */
    @PostMapping("/modules/update")
    fun translateUpdateModule(@RequestBody data: TranslateModuleUpdateAdminRequest, httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateUpdateModule(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 编辑翻译内容
     */
    @PostMapping("/edit")
    fun translateEditAdminList(@RequestBody data: TranslateEditAdminRequest,
                               httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateEditAdminList(data, httpServletRequest))
    }

    /**
     * merge翻译
     */
    @PostMapping("/merge")
    fun translateMergeAdmin(@RequestBody data: TranslateMergeAdminRequest,
                            httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateMergeAdmin(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * confirm翻译
     */
    @PostMapping("/confirm")
    fun translateConfirmAdmin(@RequestBody data: TranslateConfirmAdminRequest,
                            httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateConfirmAdmin(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 拆分翻译
     */
    @PostMapping("/split")
    fun translateSplitAdmin(@RequestBody data: TranslateSplitAdminRequest,
                            httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateSplitAdmin(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 工具下载
     */
    @PostMapping("/tools/download")
    fun translateToolsDownloadAdmin(httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateToolsDownloadAdmin(httpServletRequest))
    }

    /**
     * 删除翻译
     */
    @PostMapping("/delete")
    fun translateDeleteAdmin(@RequestBody data: TranslateSplitAdminRequest,
                             httpServletRequest: HttpServletRequest): Any? {
        adminTranslateService?.translateDeleteAdmin(data, httpServletRequest)
        return Resp.createSuccess()
    }

    /**
     * 获取翻译日志
     */
    @PostMapping("/log/list")
    fun translateLogList(@RequestBody data: TranslateLogAdminRequest,
                         httpServletRequest: HttpServletRequest): Any? {
        return Resp(adminTranslateService?.translateLogList(data, httpServletRequest))
    }
}