package com.cretin.webdb.resp

data class TranslateItemResp(
        var id: Int,
        var content: String,
        val androidKey: String? = "",
        val androidPackage: String? = "",
        val iosKey: String? = "",
        val iosPackage: String? = "",
        val translateEn: String? = "",
        val translateTw: String? = "",
        val addTime: String? = "",
        val updateTime: String? = "",
        val isMerged: Boolean = true,
        val isConfirm: Boolean = false)