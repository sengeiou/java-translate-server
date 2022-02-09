package com.cretin.webservices.service.api.impl

import com.cretin.webdb.constant.TranslateIndicator
import com.cretin.webdb.request.TranslateListUploadRequest
import com.cretin.webdb.entity.*
import com.cretin.webdb.service.*
import com.cretin.webservices.service.api.AdminTranslateService
import com.cretin.webservices.service.api.AsyncTaskService
import org.apache.http.util.TextUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: AsyncTaskServiceImpl
 * Author: cretin
 * Date: 2021/8/1 7:39 下午
 * Description:
 */
@Service
class AsyncTaskServiceImpl : AsyncTaskService {

    @Autowired
    private var adminTranslateService: AdminTranslateService? = null

    @Autowired
    private var tbStrTranslateItemService: ITbStrTranslateItemService? = null

    @Async("processExecutor")
    override fun uploadTranslateList(data: TranslateListUploadRequest) {
        //先对数据进行去重
        //存储所有的key
        val appKeys = mutableSetOf<String>()
        val items = mutableListOf<TranslateListUploadRequest.ItemBean>()
        data.items?.forEach {
            if (!appKeys.contains(it.key)) {
                items.add(it)
            }
            appKeys.add(it.key)
        }
        data.items = items

        //记录要上传的数据 带时候进行批量上传
        val uploadCacheData = mutableListOf<TbStrTranslateItem>()
        //记录要更新的数据
        val updateCacheData = mutableListOf<TbStrTranslateItem>()
        //记录要删除的数据
        val deleteCacheData = mutableListOf<TbStrTranslateItem>()

        //查询出所有的翻译数据
        val allTranslate = tbStrTranslateItemService?.list()

        /**
         * 添加需要新增的数据
         */
        fun addUploadTranslate(it: TranslateListUploadRequest.ItemBean, mergeStatus: Int? = 1) {
            val tempData = TbStrTranslateItem()
            tempData.addTime = LocalDateTime.now()
            if (data.clientType == 0) {
                tempData.androidKey = it.key
                tempData.androidPackage = it.packageName
                tempData.androidSource = it.content
            } else {
                tempData.iosKey = it.key
                tempData.iosPackage = it.packageName
                tempData.iosSource = it.content
            }
            tempData.translateSource = it.content
            tempData.translateTw = ""
            tempData.translateEn = ""
            tempData.mergeStatus = mergeStatus
            uploadCacheData.add(tempData)
        }

        fun handlerTranslateItem(it: TranslateListUploadRequest.ItemBean) {
            //查询当前上传的数据是否已经存在于翻译系统
            val translate = allTranslate?.find { temp -> if (data.clientType == 0) temp.androidKey == it.key else temp.iosKey == it.key }
            if (translate != null) {
                //当前这条数据已经在翻译系统了

                //检查内容有没有发生变化
                if (translate.translateSource == it.content) {
                    //内容没有发生变化 直接忽略

                } else {
                    //内容发生了变化 在列表中查询此内容的所有数据条数 但是不包含已经匹配过正确的数据
                    val translateList = allTranslate?.filter { temp ->
                        temp.translateSource == it.content
                                && ((TextUtils.isEmpty(temp.androidKey) && !TextUtils.isEmpty(temp.iosKey))
                                || (!TextUtils.isEmpty(temp.androidKey) && TextUtils.isEmpty(temp.iosKey)))
                    }

                    if (translateList.size == 1) {
                        //找到一条数据
                        if (data.clientType == 0) {
                            if (TextUtils.isEmpty(translate.iosKey)) {
                                //这条数据本来就是android这边的数据 iOSKey 是空的

                                if (!TextUtils.isEmpty(translateList[0].androidKey)) {
                                    //说明找到的新的content的数据是Android数据
                                    //那就把找到的这条数据更新为未合并
                                    val temp = translateList[0]
                                    temp.mergeStatus = 0
                                    temp.updateTime = LocalDateTime.now()
                                    updateCacheData.add(temp)
                                    //然后把本来的数据的内容修改一下
                                    translate.mergeStatus = 0
                                    translate.androidPackage = it.packageName
                                    translate.androidSource = it.content
                                    translate.androidKey = it.key
                                    translate.translateSource = it.content
                                    translate.updateTime = LocalDateTime.now()
                                    updateCacheData.add(translate)
                                } else {
                                    //说明找到的新的content的数据是iOS数据
                                    //需要将两条数据合并到一起
                                    translateList[0].apply {
                                        androidKey = it.key
                                        androidSource = it.content
                                        mergeStatus = 1
                                        androidPackage = it.packageName
                                        updateTime = LocalDateTime.now()
                                        updateCacheData.add(this)
                                    }

                                    //删除之前的数据
                                    deleteCacheData.add(translate)
                                }
                            } else {
                                //这条数据是一条完整的数据 既有AndroidKey也有iOSKey

                                if (!TextUtils.isEmpty(translateList[0].androidKey)) {
                                    //说明找到的新的content的数据是Android数据
                                    //那么我们当前正在提交的这个数据直接作为新增数据
                                    addUploadTranslate(it, 0)
                                    //修改原有数据的android数据内容
                                    translate.androidKey = ""
                                    translate.androidSource = ""
                                    translate.androidPackage = ""
                                    translate.updateTime = LocalDateTime.now()
                                    updateCacheData.add(translate)
                                    //被找到的这个content内容需要修改下状态
                                    val temp = translateList[0]
                                    temp.mergeStatus = 0
                                    temp.updateTime = LocalDateTime.now()
                                    updateCacheData.add(temp)
                                } else {
                                    //说明找到的新的content的数据是iOS数据
                                    //需要合并数据
                                    translateList[0].apply {
                                        androidKey = it.key
                                        androidSource = it.content
                                        mergeStatus = 1
                                        androidPackage = it.packageName
                                        updateTime = LocalDateTime.now()
                                        updateCacheData.add(this)
                                    }
                                    //修改原有数据的android数据内容
                                    translate.androidKey = ""
                                    translate.androidSource = ""
                                    translate.androidPackage = ""
                                    translate.updateTime = LocalDateTime.now()
                                    updateCacheData.add(translate)
                                }
                            }
                        } else {
                            if (TextUtils.isEmpty(translate.androidKey)) {
                                //这条数据本来就是ios这边的数据 AndroidKey 是空的

                                if (!TextUtils.isEmpty(translateList[0].iosKey)) {
                                    //说明找到的新的content的数据是iOS数据
                                    //那就把找到的这条数据更新为未合并
                                    val temp = translateList[0]
                                    temp.mergeStatus = 0
                                    temp.updateTime = LocalDateTime.now()
                                    updateCacheData.add(temp)
                                    //然后把本来的数据的内容修改一下
                                    translate.mergeStatus = 0
                                    translate.iosPackage = it.packageName
                                    translate.iosSource = it.content
                                    translate.iosKey = it.key
                                    translate.translateSource = it.content
                                    translate.updateTime = LocalDateTime.now()
                                    updateCacheData.add(translate)
                                } else {
                                    //说明找到的新的content的数据是Android数据
                                    //需要将两条数据合并到一起
                                    translateList[0].apply {
                                        iosKey = it.key
                                        iosSource = it.content
                                        mergeStatus = 1
                                        iosPackage = it.packageName
                                        updateTime = LocalDateTime.now()
                                        updateCacheData.add(this)
                                    }

                                    //删除之前的数据
                                    deleteCacheData.add(translate)
                                }
                            } else {
                                //这条数据是一条完整的数据 既有AndroidKey也有iOSKey

                                if (!TextUtils.isEmpty(translateList[0].iosKey)) {
                                    //说明找到的新的content的数据是iOS数据
                                    //那么我们当前正在提交的这个数据直接作为新增数据
                                    addUploadTranslate(it, 0)
                                    //修改原有数据的android数据内容
                                    translate.iosKey = ""
                                    translate.iosSource = ""
                                    translate.iosPackage = ""
                                    translate.updateTime = LocalDateTime.now()
                                    updateCacheData.add(translate)
                                    //被找到的这个content内容需要修改下状态
                                    val temp = translateList[0]
                                    temp.mergeStatus = 0
                                    temp.updateTime = LocalDateTime.now()
                                    updateCacheData.add(temp)
                                } else {
                                    //说明找到的新的content的数据是Andoid数据
                                    //需要合并数据
                                    translateList[0].apply {
                                        iosKey = it.key
                                        iosSource = it.content
                                        mergeStatus = 1
                                        iosPackage = it.packageName
                                        updateTime = LocalDateTime.now()
                                        updateCacheData.add(this)
                                    }
                                    //修改原有数据的android数据内容
                                    translate.iosKey = ""
                                    translate.iosSource = ""
                                    translate.iosPackage = ""
                                    translate.updateTime = LocalDateTime.now()
                                    updateCacheData.add(translate)
                                }
                            }
                        }
                    } else if (translateList.size > 1) {
                        //找到至少2条数据
                        //没有办法直接合并 那就直接新增吧 然后把之前的数据修改一下状态
                        //1 修改状态
                        translateList.forEach { t ->
                            t.mergeStatus = 0
                            t.updateTime = LocalDateTime.now()
                            updateCacheData.add(t)
                        }
                        //2 新增数据 并且状态要是未合并
                        addUploadTranslate(it, 0)
                        //有可能是之前就是已经合并好的
                        if (!TextUtils.isEmpty(translate.iosKey) && !TextUtils.isEmpty(translate.androidKey)) {
                            if (data.clientType == 0) {
                                //当前上传的是Android
                                //取消当前数据的Android 数据
                                translate.androidPackage = ""
                                translate.androidSource = ""
                                translate.androidKey = ""
                                updateCacheData.add(translate)
                            } else {
                                //当前上传的是iOS
                                //取消当前数据的当前上传的是iOS 数据
                                translate.iosPackage = ""
                                translate.iosSource = ""
                                translate.iosKey = ""
                                updateCacheData.add(translate)
                            }
                        } else {
                            if (data.clientType == 0) {
                                //translate不为空 说明之前是有数据的 我们需要对原始数据进行处理
                                if (TextUtils.isEmpty(translate.iosKey)) {
                                    //说明当前数据被迁移后就变成了无效数据 那么就删掉吧
                                    deleteCacheData.add(translate)
                                } else {
                                    //iOS不为空
                                    translate.androidKey = ""
                                    translate.androidSource = ""
                                    translate.androidPackage = ""
                                    updateCacheData.add(translate)
                                }
                            } else {
                                if (TextUtils.isEmpty(translate.androidKey)) {
                                    //说明当前数据被迁移后就变成了无效数据 那么就删掉吧
                                    deleteCacheData.add(translate)
                                } else {
                                    //Android不为空
                                    translate.iosSource = ""
                                    translate.iosPackage = ""
                                    translate.iosKey = ""
                                    updateCacheData.add(translate)
                                }
                            }
                        }
                    } else {
                        //没有找到数据 那么直接新增数据就好了
                        addUploadTranslate(it)
                        //有可能是之前就是已经合并好的
                        if (!TextUtils.isEmpty(translate.iosKey) && !TextUtils.isEmpty(translate.androidKey)) {
                            if (data.clientType == 0) {
                                //当前上传的是Android
                                //取消当前数据的Android 数据
                                translate.androidPackage = ""
                                translate.androidSource = ""
                                translate.androidKey = ""
                                updateCacheData.add(translate)
                            } else {
                                //当前上传的是iOS
                                //取消当前数据的当前上传的是iOS 数据
                                translate.iosPackage = ""
                                translate.iosSource = ""
                                translate.iosKey = ""
                                updateCacheData.add(translate)
                            }
                        } else {
                            if (data.clientType == 0) {
                                //translate不为空 说明之前是有数据的 我们需要对原始数据进行处理
                                if (TextUtils.isEmpty(translate.iosKey)) {
                                    //说明当前数据被迁移后就变成了无效数据 那么就删掉吧
                                    deleteCacheData.add(translate)
                                } else {
                                    //iOS不为空
                                    translate.androidKey = ""
                                    translate.androidSource = ""
                                    translate.androidPackage = ""
                                    updateCacheData.add(translate)
                                }
                            } else {
                                if (TextUtils.isEmpty(translate.androidKey)) {
                                    //说明当前数据被迁移后就变成了无效数据 那么就删掉吧
                                    deleteCacheData.add(translate)
                                } else {
                                    //Android不为空
                                    translate.iosSource = ""
                                    translate.iosPackage = ""
                                    translate.iosKey = ""
                                    updateCacheData.add(translate)
                                }
                            }
                        }
                    }
                }
            } else {
                //当前这条数据是新增数据
                //获取当前内容在数据库中的库存
                val translateList = allTranslate?.filter { temp ->
                    temp.translateSource == it.content
                            && ((TextUtils.isEmpty(temp.androidKey) && !TextUtils.isEmpty(temp.iosKey))
                            || (!TextUtils.isEmpty(temp.androidKey) && TextUtils.isEmpty(temp.iosKey)))
                }
                if (translateList.isNullOrEmpty()) {
                    //没有找到 直接添加
                    addUploadTranslate(it)
                } else {
                    //找到至少一条
                    if (translateList.size == 1) {
                        //只有一条数据
                        translateList[0].apply {
                            if (data.clientType == 0) {
                                if (TextUtils.isEmpty(this.androidKey)) {
                                    //当前这条数据是iOS的数据 那么我们需要合并数据为一条数据
                                    translateList[0].apply {
                                        androidKey = it.key
                                        androidSource = it.content
                                        mergeStatus = 1
                                        androidPackage = it.packageName
                                        updateTime = LocalDateTime.now()
                                        updateCacheData.add(this)
                                    }
                                } else {
                                    //当前这条数据是Android数据 那么我们需要将其添加到数据库
                                    addUploadTranslate(it, 0)
                                    //然后更新找到的那一条数据为未合并
                                    updateCacheData.add(translateList[0].let {
                                        it.mergeStatus = 0
                                        it
                                    })
                                }
                            } else {
                                if (TextUtils.isEmpty(this.iosKey)) {
                                    //当前这条数据是Android的数据 那么我们需要合并数据为一条数据
                                    translateList[0].apply {
                                        iosKey = it.key
                                        iosSource = it.content
                                        mergeStatus = 1
                                        iosPackage = it.packageName
                                        updateTime = LocalDateTime.now()
                                        updateCacheData.add(this)
                                    }
                                } else {
                                    //当前这条数据是ios数据 那么我们需要将其添加到数据库
                                    addUploadTranslate(it, 0)
                                    //然后更新找到的那一条数据为未合并
                                    updateCacheData.add(translateList[0].let {
                                        it.mergeStatus = 0
                                        it
                                    })
                                }
                            }
                        }
                    } else {
                        //多条数据
                        // 1 修改这些数据的状态
                        translateList.forEach {
                            it.mergeStatus = 0
                            it.updateTime = LocalDateTime.now()
                            updateCacheData.add(it)
                        }
                        // 2 添加本条数据
                        addUploadTranslate(it, 0)
                    }
                }
            }
        }

        //遍历要上传的数据
        data.items?.forEach {
            handlerTranslateItem(it)
        }


        if (uploadCacheData.isNotEmpty()) {
            try {
                tbStrTranslateItemService?.saveBatch(uploadCacheData)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        if (updateCacheData.isNotEmpty()) {
            try {
                tbStrTranslateItemService?.updateBatchById(updateCacheData.distinctBy { it.id })
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        if (deleteCacheData.isNotEmpty()) {
            try {
                tbStrTranslateItemService?.removeByIds(deleteCacheData.map { it.id })
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        //更新module
        adminTranslateService?.updateTranslateModulesList()

        TranslateIndicator.isUploadingTranslate = false
    }

    @Async("processExecutor")
    override fun uploadEnTranslateList(data: TranslateListUploadRequest) {
        if (data.items?.isEmpty() == true) return
        //找到所有能用数据
        val allTranslate = tbStrTranslateItemService?.list()?.filter { temp -> !TextUtils.isEmpty(if (data.clientType == 0) temp.androidKey else temp.iosKey) }
        val needUpdateList = mutableListOf<TbStrTranslateItem>()
        data.items?.forEach {
            if (!TextUtils.isEmpty(it.content.trim())) {
                val item = allTranslate?.find { temp -> (if (data.clientType == 0) temp.androidKey else temp.iosKey) == it.key }
                if (item != null) {
                    if (TextUtils.isEmpty(item.translateEn)) {
                        //可以设置
                        val newItem = TbStrTranslateItem()
                        BeanUtils.copyProperties(item, newItem)
                        newItem.translateEn = it.content.trim()
                        needUpdateList.add(newItem)
                    }
                }
            }
        }
        if (needUpdateList.isNotEmpty()) {
            tbStrTranslateItemService?.saveOrUpdateBatch(needUpdateList)
        }
    }
}