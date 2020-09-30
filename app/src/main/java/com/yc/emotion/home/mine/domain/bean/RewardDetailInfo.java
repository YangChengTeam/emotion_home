package com.yc.emotion.home.mine.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2020/8/24 14:57.
 */
public class RewardDetailInfo {

    private String title;
    @JSONField(name = "created_at")
    private String date;

    @JSONField(name = "amount")
    private String money;
    private String id;


    private String nick_name;

    private String face;
    private int count;

    public RewardDetailInfo() {
    }

    public RewardDetailInfo(String title, String date, String money) {
        this.title = title;
        this.date = date;
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
