package com.yc.emotion.home.base.listener;

import com.yc.emotion.home.model.bean.UserAccreditInfo;

/**
 * Created by wanglin  on 2018/11/19 15:19.
 */
public interface ThirdLoginListener {

    void onLoginResult(UserAccreditInfo userDataInfo);
}
