package com.yc.emotion.home.model.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2020/8/3 17:20.
 */
public class VideoItem implements Parcelable {

    /**
     * id : 4
     * cover : http://qg.bshu.com/uploads/20200806/451abefacbee3e0113ffd3213487eb3b.jpeg
     * title : 测试1
     * view_count : 0
     * view_count_sub : 0
     * url :
     * tutor_id : 16
     * tutor_name : 椰子
     * tutor_face : http://qg.bshu.com/ueditor/php/up/file/20200731/1596166087786500.jpg
     * tutor_weixin : pai201807
     */

    private String id;
    @JSONField(name = "url")
    private String videoUrl;
    @JSONField(name = "view_count")
    private int playCount;
    @JSONField(name = "cover")
    private String picCover;

    private String title;

    private int tutor_id;
    private String tutor_name;
    private String tutor_face;
    private String tutor_weixin;
    private String created_at;
    private String tutor_profession;


    public VideoItem() {
    }

    public VideoItem(int playCount, String picCover) {
        this.playCount = playCount;
        this.picCover = picCover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getPicCover() {
        return picCover;
    }

    public void setPicCover(String picCover) {
        this.picCover = picCover;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getTutor_id() {
        return tutor_id;
    }

    public void setTutor_id(int tutor_id) {
        this.tutor_id = tutor_id;
    }

    public String getTutor_name() {
        return tutor_name;
    }

    public void setTutor_name(String tutor_name) {
        this.tutor_name = tutor_name;
    }

    public String getTutor_face() {
        return tutor_face;
    }

    public void setTutor_face(String tutor_face) {
        this.tutor_face = tutor_face;
    }

    public String getTutor_weixin() {
        return tutor_weixin;
    }

    public void setTutor_weixin(String tutor_weixin) {
        this.tutor_weixin = tutor_weixin;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTutor_profession() {
        return tutor_profession;
    }

    public void setTutor_profession(String tutor_profession) {
        this.tutor_profession = tutor_profession;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.videoUrl);
        dest.writeInt(this.playCount);
        dest.writeString(this.picCover);
        dest.writeString(this.title);
        dest.writeInt(this.tutor_id);
        dest.writeString(this.tutor_name);
        dest.writeString(this.tutor_face);
        dest.writeString(this.tutor_weixin);
        dest.writeString(this.created_at);
        dest.writeString(this.tutor_profession);
    }

    protected VideoItem(Parcel in) {
        this.id = in.readString();
        this.videoUrl = in.readString();
        this.playCount = in.readInt();
        this.picCover = in.readString();
        this.title = in.readString();
        this.tutor_id = in.readInt();
        this.tutor_name = in.readString();
        this.tutor_face = in.readString();
        this.tutor_weixin = in.readString();
        this.created_at = in.readString();
        this.tutor_profession =in.readString();
    }

    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel source) {
            return new VideoItem(source);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
}
