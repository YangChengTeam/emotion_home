package com.yc.emotion.home.message.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by suns  on 2020/8/6 17:18.
 */
public class VideoBannerInfo {

    /**
     * id : 2
     * cover : http://qg.bshu.com/uploads/20200618/481659855508cea1b8fb0dae3e0d8635.jpg
     * jump_url : http://www.baidu.com
     * title : BC如何高段位挽回，不再低姿态请求
     * sub_title : 关系经营导师
     * view_count : null
     */

    private int id;
    private String cover;
    private String jump_url;
    private String title;
    private String sub_title;
    private int view_count;
    private int type;
    @JSONField(name = "weixin")
    private String wx;
    private int is_online;
    private List<VideoBannerVideo> live_reback_url;

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

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public List<VideoBannerVideo> getLive_reback_url() {
        return live_reback_url;
    }

    public void setLive_reback_url(List<VideoBannerVideo> live_reback_url) {
        this.live_reback_url = live_reback_url;
    }
}
