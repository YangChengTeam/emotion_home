package com.yc.emotion.home.mine.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/15 15:05.
 */
interface MineView : IView, IDialog {
    fun showUserInfo(data: UserInfo?) {}
    fun showUpdateUserInfo(data: UserInfo?) {}
    fun showUserInterseInfo(data: List<UserInterInfo>?) {}
    fun showSuggestionSuccess() {}
    fun showInvatationcode(code: String){}
}