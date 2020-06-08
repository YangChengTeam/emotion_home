package com.yc.emotion.home.index.domain.bean;

/**
 * Created by suns  on 2020/6/2 14:35.
 */
public enum MessageType {

    NORMAL(1), TUTOR(2), GET_WX(3), PRAISE(4);

    int type;

    MessageType(int i) {
        this.type = i;
    }
}
