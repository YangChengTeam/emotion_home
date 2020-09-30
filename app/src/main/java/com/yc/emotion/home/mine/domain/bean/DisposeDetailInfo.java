package com.yc.emotion.home.mine.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2020/8/24 19:44.
 */
public class DisposeDetailInfo {


    private String title;
    @JSONField(name = "no")
    private String dispose_num;
    @JSONField(name = "amount")
    private String money;

    @JSONField(name = "status")
    private int state;//0 等待 1成功 2失败
    @JSONField(name = "refund_reason")
    private String failure;
    @JSONField(name = "created_at")
    private String date;

    private int id;


    public DisposeDetailInfo() {
    }

    public DisposeDetailInfo(String title, String dispose_num, String money, String date, int state, String failure) {
        this.title = title;
        this.dispose_num = dispose_num;
        this.money = money;

        this.state = state;
        this.failure = failure;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDispose_num() {
        return dispose_num;
    }

    public void setDispose_num(String dispose_num) {
        this.dispose_num = dispose_num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
