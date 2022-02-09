package com.cretin.translatetools.config

class Config {
    companion object {
        val IS_DEBUG = false
        val CAN_UPLAOD_EN = false // 是否可以上传英文版本
        val VERSION = "v1.1.0"
//        val HOST = "http://127.0.0.1:9003"//测试环境
        val HOST = if(IS_DEBUG) "http://127.0.0.1:8050" else "http://tools.cretinzp.com/translate_api"
        val HOST_ADMIN = if(IS_DEBUG) "http://127.0.0.1:8080/#/home" else "http://tools.cretinzp.com/translate-web/#/home"
        val UPDATE_TIPS = """
V1.1.0更新内容如下：
1、新增身份验证，更加安全
2、可清空控制台内容输出
3、iOS取消英文文件的备份
        """.trimMargin()
    }
}