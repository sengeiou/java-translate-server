package com.cretin.webservices.service.api

import com.cretin.webdb.request.TranslateListUploadRequest


/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: AdminTaskService
 * Author: cretin
 * Date: 2021/7/24 10:44 上午
 * Description:
 */
interface AsyncTaskService {
    /**
     * 上传翻译
     */
    fun uploadTranslateList(data: TranslateListUploadRequest)

    /**
     * 上传英文版本的翻译
     */
    fun uploadEnTranslateList(data: TranslateListUploadRequest)

}