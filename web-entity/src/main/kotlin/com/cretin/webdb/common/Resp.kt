package com.cretin.webdb.common

data class Resp<T>(var data: T? = null,
                   var msg: String = "数据返回成功",
                   var code: Int = 200) {

    companion object {
        fun <T> createSuccess(data: T?): Resp<T> {
            return Resp(data)
        }

        fun <T> createSuccess(data: T?, msg: String): Resp<T> {
            return Resp(data, msg = msg)
        }

        fun createSuccess(msg: String): Resp<Any> {
            return Resp(null, msg = msg)
        }

        fun createSuccess(): Resp<Any> {
            return Resp()
        }

        fun <T> createFail(msg: String): Resp<T> {
            return Resp(msg = msg, code = 201)
        }
    }
}