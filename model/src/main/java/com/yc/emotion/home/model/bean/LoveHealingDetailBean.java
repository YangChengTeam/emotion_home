package com.yc.emotion.home.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/11.
 */

public class LoveHealingDetailBean implements Serializable, MultiItemEntity {
    /**
     * ans_sex : 1
     * content : 别让我看见你
     * love_id : 4
     * lovewords_id : 2
     */

    public String ans_sex;
    public String content;
    public int id;
    public int lovewords_id;

    public static final int VIEW_ITEM_MEN = 0;
    public static final int VIEW_ITEM_WOMEN = 2;
    public static final int VIEW_PROG = 1;
    public static final int VIEW_TITLE = 3;

    public int type;

    public LoveHealingDetailBean(String ans_sex) {
        this.ans_sex = ans_sex;
    }

    public LoveHealingDetailBean() {
    }

    @Override
    public String toString() {
        return "LoveHealingDetailBean{" +
                "ans_sex='" + ans_sex + '\'' +
                ", content='" + content + '\'' +
                ", love_id=" + id +
                ", lovewords_id=" + lovewords_id +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
