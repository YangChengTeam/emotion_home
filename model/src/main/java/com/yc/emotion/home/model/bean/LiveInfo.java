package com.yc.emotion.home.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class LiveInfo {

    public int id;
    public String img;//直播封面
    public String title;//直播标题

    @JSONField(name = "tutor_name")
    public String name;//导师姓名

    @JSONField(name = "people_number")
    public int onlineCount;//在线人数

    @JSONField(name = "status")
    public int state;//直播状态
    @JSONField(name = "video_link")
    public String liveUrl;//直播地址
    @JSONField(name = "tutor_cat")
    public String liveType;//直播分类 关系经营导师

    @JSONField(name = "start_time")
    public String liveTime;//开播时间

}
