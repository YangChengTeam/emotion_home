package com.yc.emotion.home.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.jetbrains.annotations.NotNull;

/**
 * Created by suns  on 2019/10/30 11:13.
 */
public class MessageInfo implements MultiItemEntity, Parcelable {

    public static final int TYPE_MESSAGE = 1;
    public static final int TYPE_NOTIFICATION = 2;

    public static final int TYPE_COMMUNITY = 3;

    private int id;
    private int type;
    private int img;
    @JSONField(name = "title")
    @NotNull
    private String name;
    @JSONField(name = "desp")
    @NotNull
    private String desc;

    private String content;

    public MessageInfo() {
    }

    public MessageInfo(int type, int img, @NotNull String name, @NotNull String desc) {
        this.type = type;
        this.img = img;
        this.name = name;
        this.desc = desc;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getDesc() {
        return desc;
    }

    public void setDesc(@NotNull String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeInt(this.img);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.content);
    }

    protected MessageInfo(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.img = in.readInt();
        this.name = in.readString();
        this.desc = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<MessageInfo> CREATOR = new Parcelable.Creator<MessageInfo>() {
        @Override
        public MessageInfo createFromParcel(Parcel source) {
            return new MessageInfo(source);
        }

        @Override
        public MessageInfo[] newArray(int size) {
            return new MessageInfo[size];
        }
    };
}
