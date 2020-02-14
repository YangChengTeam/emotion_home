package com.yc.emotion.home.model.bean.event;

import com.yc.emotion.home.model.bean.UserInfo;

/**
 * Created by mayn on 2019/5/9.
 */

public class EventLoginState {

    public static final int STATE_LOGINED = 1;
    public static final int STATE_EXIT = 2;
    public static final int STATE_UPDATE_INFO = 3;

    public String nick_name;

    public int state;

    public UserInfo userInfo;

    public EventLoginState(int state) {
        this.state = state;
    }

    public EventLoginState(int state, UserInfo userInfo) {
        this.state = state;
        this.userInfo = userInfo;
    }

    //    public boolean isLogined;


}
