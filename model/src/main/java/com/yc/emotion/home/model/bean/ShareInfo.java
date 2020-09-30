package com.yc.emotion.home.model.bean;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2019/7/9 16:09.
 */
public class ShareInfo {
    private String title;
    @JSONField(name = "description")
    private String desp;

    private String img;
    private String url;

    private Bitmap bitmap;

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
