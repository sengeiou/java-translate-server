package com.cretin.webapi.controller

import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: TestController
 * Author: cretin
 * Date: 2021/5/3 9:15 下午
 * Description: 测试Controller
 */
@RestController
@RequestMapping("/test")
@ResponseBody
class TestController {

    @GetMapping("/test")
    fun homeListForType(httpServletRequest: HttpServletRequest): String {
        return "hello world"
    }
}