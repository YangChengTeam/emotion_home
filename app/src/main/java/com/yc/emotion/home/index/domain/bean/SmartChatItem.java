package com.yc.emotion.home.index.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.emotion.home.model.bean.LoveHealDateBean;
import com.yc.emotion.home.model.bean.LoveHealDetBean;
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean;

import java.util.List;

/**
 * Created by suns  on 2020/9/1 10:44.
 */
public class SmartChatItem implements MultiItemEntity {
    private int type;


    public static final int CHAT_ITEM_SELF = 0;
    public static final int CHAT_ITEM_VERBAL = 1;
    public static final int CHAT_ITEM_VIP = 2;
    private String content;
    @JSONField(name = "ai")
    private List<AIItem> aiItems;
    @JSONField(name = "dialogue")
    private List<LoveHealDetBean> loveHealDetBeans;


    public SmartChatItem() {
    }

    public SmartChatItem(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AIItem> getAiItems() {
        return aiItems;
    }

    public void setAiItems(List<AIItem> aiItems) {
        this.aiItems = aiItems;
    }

    public List<LoveHealDetBean> getLoveHealDetBeans() {
        return loveHealDetBeans;
    }

    public void setLoveHealDetBeans(List<LoveHealDetBean> loveHealDetBeans) {
        this.loveHealDetBeans = loveHealDetBeans;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }


}
