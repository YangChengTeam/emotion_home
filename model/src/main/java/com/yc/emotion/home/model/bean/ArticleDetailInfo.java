package com.yc.emotion.home.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/13.
 * 所有的文章详情信息
 */

public class ArticleDetailInfo implements Serializable {

    public int type = 1;

    /**
     * "category_name": "其他",
     * create_time : 1509927506
     * id : 263
     * image :
     * post_title : 约超萌超可爱美女出来看电影，当晚就拿下
     */

    public String category_name;
    public int create_time;
    public int collect_time;
    public int id;
    public int feeluseful;
    public String image;
    public String post_title;

    @JSONField(name = "cat_name")
    public String tag;

    public ArticleDetailInfo(int type, String post_title) {
        this.type = type;
        this.post_title = post_title;
    }

    public ArticleDetailInfo() {
    }

    public ArticleDetailInfo(int type, String category_name, int create_time, int id, int feeluseful, String image, String post_title) {
        this.type = type;
        this.category_name = category_name;
        this.create_time = create_time;
        this.id = id;
        this.feeluseful = feeluseful;
        this.image = image;
        this.post_title = post_title;
    }

    @Override
    public String toString() {
        return "ArticleDetailInfo{" +
                "type=" + type +
                ", category_name='" + category_name + '\'' +
                ", create_time=" + create_time +
                ", collect_time=" + collect_time +
                ", id=" + id +
                ", feeluseful=" + feeluseful +
                ", image='" + image + '\'' +
                ", post_title='" + post_title + '\'' +
                '}';
    }
}
