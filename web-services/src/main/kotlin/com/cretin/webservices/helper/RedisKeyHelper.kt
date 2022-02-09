package com.cretin.webservices.helper

/**
 * Copyright (C), 2015-2021, 电点科技有限公司
 * FileName: RedisKeyHelper
 * Author: cretin
 * Date: 2021/4/6 7:03 下午
 * Description: redis 通用的key 帮助类
 */
object RedisKeyHelper {

    //首页用户推荐key
    const val HOME_USER_RECOMMEND_IDS = "home_user_recommend"

    //首页推荐的文本段子
    const val HOME_JOKES_RECOMMEND_TEXT_IDS = "jokes_type_text_ids"

    //首页推荐的图片段子
    const val HOME_JOKES_RECOMMEND_PIC_IDS = "jokes_type_pic_ids"

    //首页推荐的视频段子
    const val HOME_JOKES_RECOMMEND_VIDEO_IDS = "jokes_type_video_ids"

    //获取新鲜的段子id 近10000条
    const val HOME_JOKES_LATEST_IDS = "jokes_latest_ids"

    //首页推荐数据
    const val HOME_JOKES_RECOMMEND_IDS = "jokes_recommend_ids"
}