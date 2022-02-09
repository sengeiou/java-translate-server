package com.cretin.webdb.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: CustomUserService
 * Author: cretin
 * Date: 2021/3/17 6:23 下午
 * Description: 自定义的用户service
 */
interface CustomHomeService : BaseMapper<Any> {
    /**
     * 获取翻译的module
     */
    @Select("select ios_package,android_package from tb_str_translate_item group by ios_package,android_package")
    fun getTranslateModules(): MutableList<MutableMap<String, Any?>>?
}