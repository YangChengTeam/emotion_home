package com.yc.emotion.home.model.bean;

import java.io.Serializable;

/**
 * Created by jingbin on 2018/12/23.
 */

public class BannerInfo implements Serializable {


    /**
     * id : 74
     * title : null
     * img : http://qg.bshu.com/uploads/banner_1.png
     * type : null
     * type_value : null
     */

    private int id;
    private String title;
    private String img;
    private int type;
    private String type_value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }
}
