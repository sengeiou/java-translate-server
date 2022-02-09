package com.cretin.translatetools.entity

data class ConfigJson(var clientType: Int = 0,
                      var rootPath: String = "",
                      var token: String = "",
                      var version: String = "1.0.0") {
    companion object {
        fun createNew(): ConfigJson {
            return ConfigJson(0, "", "","1.0.0")
        }
    }
}