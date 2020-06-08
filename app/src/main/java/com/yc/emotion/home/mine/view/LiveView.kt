package com.yc.emotion.home.mine.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.domain.bean.LiveInfo

/**
 *
 * Created by suns  on 2020/6/5 09:36.
 */
interface LiveView : IView, IDialog {
    fun showLoginSuccess(data: LiveInfo) {}
    fun showCreateRoomSuccess(data: LiveInfo) {}
}