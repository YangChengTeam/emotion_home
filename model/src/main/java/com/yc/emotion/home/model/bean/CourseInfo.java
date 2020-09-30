package com.yc.emotion.home.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by suns  on 2019/10/24 17:42.
 */
public class CourseInfo implements Parcelable {

    @JSONField(name = "chapter_id")
    @Nullable
    private String id;
    @JSONField(name = "chapter_image")
    @Nullable
    private String img;
    @JSONField(name = "study_people")
    private int count;
    @JSONField(name = "chapter_title")
    @Nullable
    private String title;
    @JSONField(name = "chapter_price")
    @Nullable
    private String price;
    @JSONField(name = "tutor_name")//导师名字
    @Nullable
    private String name;

    private int tutor_id;
    private int lesson_total;
    @Nullable
    private String cat_id;
    @NotNull
    private String cat_name;
    @Nullable
    private String face;
    private GoodsInfo goods_info;
    @Nullable
    private String chapter_content;

    private String tutor_face;//导师图像

    private int goods_id;//商品id

    private int is_collect;//是否收藏

    private long duration;//时长

    private int has_buy;//0 是没有购买 | 1. 购买


    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getImg() {
        return img;
    }

    public void setImg(@Nullable String img) {
        this.img = img;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getPrice() {
        return price;
    }

    public void setPrice(@Nullable String price) {
        this.price = price;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }


    public int getTutor_id() {
        return tutor_id;
    }

    public void setTutor_id(int tutor_id) {
        this.tutor_id = tutor_id;
    }

    public int getLesson_total() {
        return lesson_total;
    }

    public void setLesson_total(int lesson_total) {
        this.lesson_total = lesson_total;
    }

    @Nullable
    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(@Nullable String cat_id) {
        this.cat_id = cat_id;
    }

    @NotNull
    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(@NotNull String cat_name) {
        this.cat_name = cat_name;
    }

    @Nullable
    public String getFace() {
        return face;
    }

    public void setFace(@Nullable String face) {
        this.face = face;
    }

    public GoodsInfo getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(GoodsInfo goods_info) {
        this.goods_info = goods_info;
    }

    @Nullable
    public String getChapter_content() {
        return chapter_content;
    }

    public void setChapter_content(@Nullable String chapter_content) {
        this.chapter_content = chapter_content;
    }


    public String getTutor_face() {
        return tutor_face;
    }

    public void setTutor_face(String tutor_face) {
        this.tutor_face = tutor_face;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getHas_buy() {
        return has_buy;
    }

    public void setHas_buy(int has_buy) {
        this.has_buy = has_buy;
    }

    public CourseInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.img);
        dest.writeInt(this.count);
        dest.writeString(this.title);
        dest.writeString(this.price);
        dest.writeString(this.name);

        dest.writeInt(this.tutor_id);
        dest.writeInt(this.lesson_total);
        dest.writeString(this.cat_id);
        dest.writeString(this.cat_name);
        dest.writeString(this.face);
        dest.writeSerializable(this.goods_info);
        dest.writeString(this.chapter_content);
        dest.writeString(this.tutor_face);
        dest.writeInt(this.goods_id);
        dest.writeInt(is_collect);
        dest.writeLong(duration);
        dest.writeInt(this.has_buy);
    }

    protected CourseInfo(@NotNull Parcel in) {
        this.id = in.readString();
        this.img = in.readString();
        this.count = in.readInt();
        this.title = in.readString();
        this.price = in.readString();
        this.name = in.readString();

        this.tutor_id = in.readInt();
        this.lesson_total = in.readInt();
        this.cat_id = in.readString();
        this.cat_name = in.readString();
        this.face = in.readString();
        this.goods_info = (GoodsInfo) in.readSerializable();
        this.chapter_content = in.readString();
        this.tutor_face = in.readString();
        this.goods_id = in.readInt();
        this.is_collect = in.readInt();
        this.duration = in.readLong();
        this.has_buy = in.readInt();
    }

    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel source) {
            return new CourseInfo(source);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };
}
