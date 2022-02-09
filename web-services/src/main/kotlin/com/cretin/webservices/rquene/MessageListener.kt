package com.cretin.webservices.rquene

import com.cretin.webredis.rquene.model.ActiveCodeMessageModel
import com.cretin.webredis.rquene.model.PushMessageModel
import com.github.sonus21.rqueue.annotation.RqueueListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MessageListener {

    /**
     * 发活跃值
     */
    @RqueueListener(value = ["active_code_msg"])
    @Transactional
    fun activeCodeMsg(model: ActiveCodeMessageModel) {

    }

    /**
     * 发送推送
     */
    @RqueueListener(value = ["push_custom_msg"])
    @Transactional
    fun pushEvent(model: PushMessageModel) {

    }
}