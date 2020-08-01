package com.yc.emotion.home.index.domain.bean;

/**
 * Created by suns  on 2020/6/2 14:21.
 */
public class Message {
    //消息类型 1.普通消息 2.导师消息 3.获取微信消息 4.点赞消息 5进入房间
    // 6直播结束 7官方巡逻消息 8禁言消息 9有人进群和离群消息或者点赞消息

    public static final int command_normal = 1;
    public static final int command_tutor = 2;
    public static final int command_get_wx = 3;

    public static final int command_praise = 4;

    public static final int command_come_room = 5;

    public static final int command_leave_room = 6;

    public static final int command_official_msg = 7;

    public static final int command_muted_msg = 8;

    public static final int command_come_out = 9;


    private String content;//内容

    private int cmdId;//自定义消息信令

    public Message() {
    }

    public Message(String content, int cmdId) {
        this.content = content;
        this.cmdId = cmdId;
    }



    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
