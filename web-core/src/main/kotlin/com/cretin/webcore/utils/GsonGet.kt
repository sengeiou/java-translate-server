package com.cretin.webcore.utils

import com.google.gson.Gson

object GsonGet {

    private var gson: Gson = Gson()

    /**
     * 获取gson
     */
    fun getGson(): Gson {
        return gson
    }
}