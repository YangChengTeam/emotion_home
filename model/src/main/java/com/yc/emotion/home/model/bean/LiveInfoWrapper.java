package com.yc.emotion.home.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class LiveInfoWrapper {
    public int code;
    @JSONField(name = "list")
    public List<LiveInfo> data;



}
