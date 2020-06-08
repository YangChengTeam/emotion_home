package com.yc.emotion.home.base.listener

import com.yc.emotion.home.model.bean.UserAccreditInfo

/**
 * Created by wanglin  on 2018/11/19 15:19.
 */
interface ThirdLoginListener {
    fun onLoginResult(userDataInfo: UserAccreditInfo)
}