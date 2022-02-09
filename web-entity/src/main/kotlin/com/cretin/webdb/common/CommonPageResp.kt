package com.cretin.webdb.common

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: CommonPageResp
 * Author: cretin
 * Date: 2021/7/21 9:29 上午
 * Description:
 */
class CommonPageResp(val total: Int, val currentPage: Int, val pageSize: Int, val list: MutableList<*>) {
    val totalPage: Int
        get() {
            return total / pageSize!! + (if (total % pageSize == 0) 0 else 1)
        }
}