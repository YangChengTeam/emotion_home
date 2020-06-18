package com.yc.emotion.home.index.domain.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by suns  on 2020/5/30 11:21.
 */
public class ChatItem implements MultiItemEntity {
    private String username;

    private String message;

    private String face;

    public static final int TYPE_ME = 1;//自己发的消息
    public static final int TYPE_OTHER = 2;//其他人发的消息
    public static final int TYPE_NOTIFICATION = 3;//系统消息
    public static final int TYPE_COME_CHAT = 4;//进入聊天室
    public static final int TYPE_GET_WX = 5;//用户点击获取微信
    public static final int TYPE_TOUR_MSG=6;//巡视消息

    private int type;

    public ChatItem() {
    }

    public ChatItem(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public ChatItem(String username, int type) {
        this.username = username;
        this.type = type;
    }

    public ChatItem(String username, String message, int type) {
        this.username = username;
        this.message = message;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
