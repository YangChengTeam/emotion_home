package com.yc.emotion.home.message.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.MessageInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/15 14:04.
 */
interface MessageView : IView, IDialog,StateDefaultImpl {
    fun showMessageInfos(messageInfos: List<MessageInfo>){}
    fun showNotificationDetail(data: MessageInfo?){}
}