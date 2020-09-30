package com.yc.emotion.home.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suns  on 2020/8/25 18:14.
 */
public class LessonInfo implements Parcelable {

    private int lesson_id;
    private int chapter_id;
    private String lesson_title;
    private String lesson_desp;
    private String lesson_image;
    private String lesson_url;
    private int need_pay;
    private String duration;
    private String price;
    private String m_price;

    private int good_id;

    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getLesson_title() {
        return lesson_title;
    }

    public void setLesson_title(String lesson_title) {
        this.lesson_title = lesson_title;
    }

    public String getLesson_desp() {
        return lesson_desp;
    }

    public void setLesson_desp(String lesson_desp) {
        this.lesson_desp = lesson_desp;
    }

    public String getLesson_image() {
        return lesson_image;
    }

    public void setLesson_image(String lesson_image) {
        this.lesson_image = lesson_image;
    }

    public String getLesson_url() {
        return lesson_url;
    }

    public void setLesson_url(String lesson_url) {
        this.lesson_url = lesson_url;
    }

    public int getNeed_pay() {
        return need_pay;
    }

    public void setNeed_pay(int need_pay) {
        this.need_pay = need_pay;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getM_price() {
        return m_price;
    }

    public void setM_price(String m_price) {
        this.m_price = m_price;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.lesson_id);
        dest.writeInt(this.chapter_id);
        dest.writeString(this.lesson_title);
        dest.writeString(this.lesson_desp);
        dest.writeString(this.lesson_image);
        dest.writeString(this.lesson_url);
        dest.writeInt(this.need_pay);
        dest.writeString(this.duration);
        dest.writeString(this.price);
        dest.writeString(this.m_price);
        dest.writeInt(this.good_id);
    }

    public LessonInfo() {
    }

    protected LessonInfo(Parcel in) {
        this.lesson_id = in.readInt();
        this.chapter_id = in.readInt();
        this.lesson_title = in.readString();
        this.lesson_desp = in.readString();
        this.lesson_image = in.readString();
        this.lesson_url = in.readString();
        this.need_pay = in.readInt();
        this.duration = in.readString();
        this.price = in.readString();
        this.m_price = in.readString();
        this.good_id = in.readInt();
    }

    public static final Parcelable.Creator<LessonInfo> CREATOR = new Parcelable.Creator<LessonInfo>() {
        @Override
        public LessonInfo createFromParcel(Parcel source) {
            return new LessonInfo(source);
        }

        @Override
        public LessonInfo[] newArray(int size) {
            return new LessonInfo[size];
        }
    };
}
