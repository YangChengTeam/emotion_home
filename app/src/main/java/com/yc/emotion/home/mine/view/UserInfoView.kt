package com.yc.emotion.home.mine.view

import com.yc.emotion.home.base.listener.OnUserInfoListener
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.model.bean.UserInfo

/**
 *
 * Created by suns  on 2019/11/19 15:13.
 */
interface UserInfoView : IView, IDialog {
    fun sendCodeSuccess() {}
    fun showPhoneLoginSuccess(userInfo: UserInfo?) {}
    fun showPhoneRegisterSuccess(data: UserInfo?) {}
    fun showResetPwdSuccess() {}
    fun thirdLoginSuccess(data: UserInfo?, finish: Boolean?) {}
    fun getUserInfoSuccess(userInfo: UserInfo, listener: OnUserInfoListener?) {}
    fun getWechatInfoSuccess(wx: String, listener: BaseActivity.OnWxListener?) {}
    fun setPwdSuccess(){}
}