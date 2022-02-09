package com.cretin.webservices.service.api.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.cretin.webcore.config.AppConstants
import com.cretin.webcore.exception.WholeException
import com.cretin.webcore.utils.UUIDUtils
import com.cretin.webcore.utils.getMillis
import com.cretin.webdb.common.CommonPageResp
import com.cretin.webdb.constant.TranslateIndicator
import com.cretin.webdb.entity.*
import com.cretin.webdb.mapper.CustomHomeService
import com.cretin.webdb.request.*
import com.cretin.webdb.resp.TranslateItemResp
import com.cretin.webdb.service.*
import com.cretin.webservices.helper.checkToken
import com.cretin.webservices.helper.getToken
import com.cretin.webservices.helper.getUser
import com.cretin.webservices.service.api.AdminTranslateService
import com.cretin.webservices.service.api.AsyncTaskService
import org.apache.http.util.TextUtils
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: AdminExcessServiceImpl
 * Author: cretin
 * Date: 2021/8/23 5:53 下午
 * Description:
 */
@Service
class AdminTranslateServiceImpl : AdminTranslateService {

    @Autowired
    private var tbStrTranslateItemService: ITbStrTranslateItemService? = null

    @Autowired
    private var asyncTaskService: AsyncTaskService? = null

    @Autowired
    private var tbStrTranslateLogService: ITbStrTranslateLogService? = null

    @Autowired
    private var customHomeService: CustomHomeService? = null

    @Autowired
    private var tbStrTranslateModuleService: ITbStrTranslateModuleService? = null

    @Autowired
    private var globalConfigService: ITbGlobalConfigService? = null

    @Autowired
    private var strTranslateUserService: ITbStrTranslateUserService? = null

    override fun translateList(data: TranslateListClientRequest, httpServletRequest: HttpServletRequest): Any {
        data.token?.checkToken(strTranslateUserService)

        val result = mutableMapOf<String, Any?>()
        val list = mutableListOf<MutableMap<String, Any?>>()
        if (data.clientType == 0) {
            //android
            val queryWrapper = QueryWrapper<TbStrTranslateItem>()
            queryWrapper.ne("translate_en", "")
            queryWrapper.ne("android_key", "")
            tbStrTranslateItemService?.list(queryWrapper)?.forEach {
                list?.add(mutableMapOf<String, Any?>().apply {
                    put("key", it.androidKey)
                    put("packageName", it.androidPackage)
                    put("content", it.androidSource)
                    put("translateEn", it.translateEn)
                    put("translateTw", it.translateTw)
                })
            }
        } else {
            //ios
            val queryWrapper = QueryWrapper<TbStrTranslateItem>()
            queryWrapper.ne("translate_en", "")
            queryWrapper.ne("ios_key", "")
            tbStrTranslateItemService?.list(queryWrapper)?.forEach {
                list?.add(mutableMapOf<String, Any?>().apply {
                    put("key", it.iosKey)
                    put("packageName", it.iosPackage)
                    put("content", it.iosSource)
                    put("translateEn", it.translateEn)
                    put("translateTw", it.translateTw)
                })
            }
        }
        result["items"] = list
        result["count"] = list.size
        return result
    }

    override fun uploadTranslateList(data: TranslateListUploadRequest, httpServletRequest: HttpServletRequest) {
        data.token?.checkToken(strTranslateUserService)

        if (TranslateIndicator.isUploadingTranslate) {
            throw WholeException("当前有其他翻译上传任务在执行，请10s后再次提交翻译")
        }
        //对任务进行限流
        TranslateIndicator.isUploadingTranslate = true
        asyncTaskService?.uploadTranslateList(data)
    }

    override fun uploadEnTranslateList(data: TranslateListUploadRequest, httpServletRequest: HttpServletRequest) {
        data.token?.checkToken(strTranslateUserService)
        asyncTaskService?.uploadEnTranslateList(data)
    }

    override fun translateAdminList(data: TranslateListAdminRequest, httpServletRequest: HttpServletRequest): Any {
        val page = Page<TbStrTranslateItem>(data.page, data.size)
        var queryWrapper = QueryWrapper<TbStrTranslateItem>()

        if (data.orderType == 0) {
            queryWrapper = queryWrapper.orderByDesc("update_time")
        } else if (data.orderType == 1) {
            queryWrapper = queryWrapper.orderByAsc("update_time")
        } else if (data.orderType == 2) {
            queryWrapper = queryWrapper.orderByDesc("add_time")
        } else if (data.orderType == 3) {
            queryWrapper = queryWrapper.orderByAsc("add_time")
        }

        if (data.clientType != 0) {
            if (data.clientType == 1) {
                //查看android
                queryWrapper = queryWrapper.and { it.ne("android_key", "") }
            } else {
                queryWrapper = queryWrapper.and { it.ne("ios_key", "") }
            }
        }
        if (data.mergeStatus != 0) {
            if (data.mergeStatus == 1) {
                queryWrapper = queryWrapper.and { it.eq("merge_status", "1") }
            } else {
                queryWrapper = queryWrapper.and { it.eq("merge_status", "0") }
            }
        }
        if (data.translateState != 0) {
            if (data.translateState == 1) {
                queryWrapper = queryWrapper.and { it.ne("translate_en", "") }
            } else {
                queryWrapper = queryWrapper.and { it.eq("translate_en", "") }
            }
        }
        if (!TextUtils.isEmpty(data.moduleKey)) {
            queryWrapper = queryWrapper.and {
                it.like("android_package", data.moduleKey)
                        .or()
                        .like("ios_package", data.moduleKey)
            }
        }
        if (!TextUtils.isEmpty(data.searchKey)) {
            if (data.searchType == true) {
                queryWrapper = queryWrapper.and {
                    it.eq("translate_source", data.searchKey)
                            .or()
                            .eq("android_key", data.searchKey)
                            .or()
                            .eq("ios_key", data.searchKey)
                            .or()
                            .eq("android_package", data.searchKey)
                            .or()
                            .eq("ios_package", data.searchKey)
                            .or()
                            .eq("translate_en", data.searchKey)
                }
            } else {
                queryWrapper = queryWrapper.and {
                    it.like("translate_source", data.searchKey)
                            .or()
                            .like("android_key", data.searchKey)
                            .or()
                            .like("ios_key", data.searchKey)
                            .or()
                            .like("android_package", data.searchKey)
                            .or()
                            .like("ios_package", data.searchKey)
                            .or()
                            .like("translate_en", data.searchKey)
                }
            }

        }
        val pageInfo = tbStrTranslateItemService?.page(page, queryWrapper)
        val result = mutableListOf<TranslateItemResp>()
        pageInfo?.records?.forEach {
            result.add(TranslateItemResp(it.id,
                    it.translateSource, it.androidKey, it.androidPackage,
                    it.iosKey, it.iosPackage, it.translateEn, it.translateTw,
                    DateTime(it.addTime.getMillis()).toString(AppConstants.COMM_TIME_FORMATER),
                    DateTime(it.updateTime.getMillis()).toString(AppConstants.COMM_TIME_FORMATER),
                    it.mergeStatus == 1, it.confirmStatus == 1
            ))
        }
        return CommonPageResp(page.total.toInt(), data.page.toInt(), data.size.toInt(), result)
    }

    override fun translateEditAdminList(data: TranslateEditAdminRequest, httpServletRequest: HttpServletRequest): Any {
        if (TextUtils.isEmpty(data.translate)) throw WholeException("翻译的内容不能为空")
        val translate = tbStrTranslateItemService?.getById(data.id) ?: throw WholeException("被翻译的内容不存在或被删除")
        translate.translateEn = data.translate
        translate.updateTime = LocalDateTime.now()
        tbStrTranslateItemService?.updateById(translate)

        //添加日志
        httpServletRequest.getToken()
        val log = TbStrTranslateLog()
        log.translateEn = data.translate
        log.translateUser = strTranslateUserService?.getUser(httpServletRequest?.getToken() ?: "")?.nickname
        log.translateId = translate.id
        log.updateTime = LocalDateTime.now()
        tbStrTranslateLogService?.save(log)

        val result = mutableMapOf<String, Any>()
        result["id"] = translate?.id ?: 0
        result["translate"] = translate?.translateEn ?: ""
        result["update_time"] = DateTime(translate?.updateTime?.getMillis()).toString(AppConstants.COMM_TIME_FORMATER)
        return result
    }

    override fun translateLogList(data: TranslateLogAdminRequest, httpServletRequest: HttpServletRequest): Any {
        val queryWrapper = QueryWrapper<TbStrTranslateLog>()
        queryWrapper.eq("translate_id", data.translateId)
        queryWrapper.orderByDesc("update_time")
        val result = mutableListOf<MutableMap<String, Any?>>()
        tbStrTranslateLogService?.list(queryWrapper)?.forEach {
            val temp = mutableMapOf<String, Any?>()
            temp["time"] = DateTime(it.updateTime.getMillis()).toString(AppConstants.COMM_TIME_FORMATER)
            temp["log"] = it.translateUser + " 翻译为：" + it.translateEn
            result.add(temp)
        }
        return result
    }

    override fun translateMergeAdmin(data: TranslateMergeAdminRequest, httpServletRequest: HttpServletRequest) {
        if (data.fromId == data.toId) throw WholeException("不能跟自己合并")

        val translateFrom = tbStrTranslateItemService?.getById(data.fromId) ?: throw WholeException("合并方翻译不存在")
        val translateTo = tbStrTranslateItemService?.getById(data.toId) ?: throw WholeException("被合并方翻译不存在")

        if (translateFrom.translateSource != translateTo.translateSource) {
            throw WholeException("合并双方的中文内容必须一致才可合并")
        }

        if (!TextUtils.isEmpty(translateTo.androidKey) && !TextUtils.isEmpty(translateFrom.androidKey)) {
            throw WholeException("不支持在相同平台互相合并")
        }

        if (!TextUtils.isEmpty(translateTo.iosKey) && !TextUtils.isEmpty(translateFrom.iosKey)) {
            throw WholeException("不支持在相同平台互相合并")
        }

        if (!TextUtils.isEmpty(translateTo.iosKey) && !TextUtils.isEmpty(translateTo.androidKey)) {
            throw WholeException("合并失败，合并的目标翻译不支持被合并")
        }

        if (!TextUtils.isEmpty(translateFrom.iosKey) && !TextUtils.isEmpty(translateFrom.androidKey)) {
            throw WholeException("合并失败，发起合并的翻译不支持被合并")
        }

        if (!TextUtils.isEmpty(translateFrom.iosKey)) {
            //覆盖的是ios数据
            translateTo.iosKey = translateFrom.iosKey
            translateTo.iosSource = translateFrom.iosSource
            translateTo.iosPackage = translateFrom.iosPackage
        } else {
            translateTo.androidKey = translateFrom.androidKey
            translateTo.androidSource = translateFrom.androidSource
            translateTo.androidPackage = translateFrom.androidPackage
        }
        if (TextUtils.isEmpty(translateTo.translateEn)) {
            translateTo.translateEn = translateFrom.translateEn
        }
        tbStrTranslateItemService?.removeById(translateFrom)

        translateTo.updateTime = LocalDateTime.now()
        translateTo.mergeStatus = 1
        tbStrTranslateItemService?.updateById(translateTo)
    }

    @Transactional
    override fun translateSplitAdmin(data: TranslateSplitAdminRequest, httpServletRequest: HttpServletRequest) {
        val translate = tbStrTranslateItemService?.getById(data.id) ?: throw WholeException("操作的内容不存在")
        tbStrTranslateItemService?.removeById(translate.id)

        //生成两条记录
        val itemAndroid = TbStrTranslateItem()
        itemAndroid.androidKey = translate.androidKey
        itemAndroid.androidSource = translate.androidSource
        itemAndroid.androidPackage = translate.androidPackage
        itemAndroid.updateTime = LocalDateTime.now()
        itemAndroid.addTime = LocalDateTime.now()
        itemAndroid.translateEn = translate.translateEn
        itemAndroid.mergeStatus = 0
        itemAndroid.translateSource = translate.translateSource
        tbStrTranslateItemService?.save(itemAndroid)

        val itemIOS = TbStrTranslateItem()
        itemIOS.iosKey = translate.iosKey
        itemIOS.iosSource = translate.iosSource
        itemIOS.iosPackage = translate.iosPackage
        itemIOS.updateTime = LocalDateTime.now()
        itemIOS.addTime = LocalDateTime.now()
        itemIOS.translateEn = translate.translateEn
        itemIOS.translateSource = translate.translateSource
        itemIOS.mergeStatus = 0
        tbStrTranslateItemService?.save(itemIOS)

    }

    override fun translateDeleteAdmin(data: TranslateSplitAdminRequest, httpServletRequest: HttpServletRequest) {
        val translate = tbStrTranslateItemService?.getById(data.id) ?: throw WholeException("被删除的数据不存在")
        tbStrTranslateItemService?.removeById(translate.id)
    }

    override fun translateToolsDownloadAdmin(httpServletRequest: HttpServletRequest): Any {
        val queryWrapper = QueryWrapper<TbGlobalConfig>()
        queryWrapper.between("type", 0, 1)
        val result = mutableMapOf<String, Any>()
        globalConfigService?.list(queryWrapper)?.apply {
            forEach {
                if (it.type == 0) {
                    result["jdk_path"] = it.configExtra1
                } else {
                    result["translate_tools_path"] = it.configExtra1
                }
            }
        }
        return result
    }

    override fun translateModuleList(httpServletRequest: HttpServletRequest): Any {
        val result = mutableListOf<MutableMap<String, Any?>>()
        val map = mutableMapOf<String, Any?>()
        map["key"] = ""
        map["id"] = "-1"
        map["value"] = "所有模块"
        result.add(map)
        val queryWrapper = QueryWrapper<TbStrTranslateModule>()
        queryWrapper.orderByAsc("id")
        tbStrTranslateModuleService?.list(queryWrapper)?.forEach {
            val temp = mutableMapOf<String, Any?>()
            temp["key"] = it.path
            temp["add_time"] = DateTime(it.addTime.getMillis()).toString(AppConstants.COMM_TIME_FORMATER)
            temp["id"] = it.id
            temp["value"] = if (TextUtils.isEmpty(it.pathLabel)) it.path else it.pathLabel
            result.add(temp)
        }
        return result
    }

    override fun updateTranslateModulesList() {
        fun checkContent(content: String?): Boolean {
            if (TextUtils.isEmpty(content)) {
                return false
            }
            if (content == "null") {
                return false
            }
            return true
        }

        val modules = customHomeService?.getTranslateModules()
        val keys = mutableSetOf<String>()
        modules?.forEach {
            val androidKey = it?.get("android_package")?.toString() ?: ""
            val iosKey = it?.get("ios_package")?.toString() ?: ""
            if (checkContent(androidKey)) {
                keys.add(androidKey)
            }
            if (checkContent(iosKey)) {
                keys.add(iosKey)
            }
        }

        tbStrTranslateModuleService?.list()?.map { it.path }?.forEach {
            keys.remove(it)
        }

        if (keys.isNotEmpty()) {
            tbStrTranslateModuleService?.saveBatch(keys.map {
                val item = TbStrTranslateModule()
                item.path = it
                item.pathLabel = ""
                item.addTime = LocalDateTime.now()
                item
            })
        }
    }

    override fun translateUpdateModule(data: TranslateModuleUpdateAdminRequest, httpServletRequest: HttpServletRequest) {
        val module = tbStrTranslateModuleService?.getById(data.id)
        if (module == null) {
            throw WholeException("你操作的内容不存在")
        }
        module.pathLabel = data.label
        module.addTime = LocalDateTime.now()
        tbStrTranslateModuleService?.updateById(module)
    }

    override fun translateUserLogin(data: TranslateUserLoginRequest, httpServletRequest: HttpServletRequest): Any {
        val userQueryWrapper = QueryWrapper<TbStrTranslateUser>()
        userQueryWrapper.eq("username", data.userName)
        userQueryWrapper.eq("password", data.password)
        var user = strTranslateUserService?.getOne(userQueryWrapper, false)
        if (user == null) {
            //注册
            user = TbStrTranslateUser()
            user.nickname = data.userName
            user.username = data.userName
            user.password = data.password
            user.token = UUIDUtils.getUuid()
            user.updateTime = LocalDateTime.now()
            user.status = 0
            strTranslateUserService?.save(user)
        } else {
            if (user.status != 0) throw WholeException("登录失败，账号已被限制使用")
        }

        val map = mutableMapOf<String, Any?>()
        map["nickname"] = user.nickname
        map["token"] = user.token
        return map
    }

    override fun translateUserChangePsw(data: TranslateUserChangePswRequest, httpServletRequest: HttpServletRequest) {
        val userQueryWrapper = QueryWrapper<TbStrTranslateUser>()
        userQueryWrapper.eq("token", httpServletRequest.getToken())
        val user = strTranslateUserService?.getOne(userQueryWrapper, false)
        if (user == null) {
            throw WholeException(201, "登录状态已过期")
        }
        user.password = data.password
        strTranslateUserService?.updateById(user)

        throw WholeException(201, "密码已被修改，请重新登录")
    }

    override fun translateUserResetToken(httpServletRequest: HttpServletRequest) {
        val userQueryWrapper = QueryWrapper<TbStrTranslateUser>()
        userQueryWrapper.eq("token", httpServletRequest.getToken())
        val user = strTranslateUserService?.getOne(userQueryWrapper, false)

        user?.token = UUIDUtils.getUuid()
        strTranslateUserService?.updateById(user)

        throw WholeException(201, "token已被重置，请重新登录")
    }

    override fun translateConfirmAdmin(data: TranslateConfirmAdminRequest, httpServletRequest: HttpServletRequest) {
        val translate = tbStrTranslateItemService?.getById(data.id)
        if (translate == null) {
            throw WholeException("被操作的翻译未找到")
        }
        if (TextUtils.isEmpty(translate.translateEn) && data.status) {
            throw WholeException("当前选择项翻译内容为空，不允许设置为确认翻译")
        }
        translate.confirmStatus = if (data.status) 1 else 0
        tbStrTranslateItemService?.updateById(translate)
    }

}