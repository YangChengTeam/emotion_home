package com.yc.emotion.home.mine.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2020/8/4 14:33.
 */
public class LiveVideoInfo {

    /**
     * id : 2
     * cover : http://qg.bshu.com/uploads/20200618/481659855508cea1b8fb0dae3e0d8635.jpg
     * sub_title : 关系经营导师
     * title : BC如何高段位挽回，不再低姿态请求
     * tag_title : 直播预约
     * teacher_name : 胜羽
     * jump_url : http://www.baidu.com
     */

    private int id;
    private String cover;
    private String sub_title;
    private String title;
    private String tag_title;
    private String teacher_name;
    private String jump_url;
    private int view_count;
    @JSONField(name = "weixin")
    private String wx;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag_title() {
        return tag_title;
    }

    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
