package com.yc.emotion.home.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns  on 2019/10/25 10:26.
 */
public class QuestionInfo implements Parcelable, MultiItemEntity {

    public static final int ITEM_TYPE_TOPIC = 1;
    public static final int ITEM_TYPE_ANSWER = 2;

    private String question_id;
    @Nullable
    private String question;
    private int test_id;
    private int sort;
    @Nullable
    private String image;
    private int qid;
    private int option_id;
    @Nullable
    private String option_text;
    private int score;
    private int re_qid;
    private int aid;

    @Nullable
    private List<QuestionInfo> options;

    private int type = ITEM_TYPE_TOPIC;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    @Nullable
    public String getQuestion() {
        return question;
    }

    public void setQuestion(@Nullable String question) {
        this.question = question;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    @Nullable
    public String getOption_text() {
        return option_text;
    }

    public void setOption_text(@Nullable String option_text) {
        this.option_text = option_text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRe_qid() {
        return re_qid;
    }

    public void setRe_qid(int re_qid) {
        this.re_qid = re_qid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Nullable
    public List<QuestionInfo> getOptions() {
        return options;
    }

    public void setOptions(@Nullable List<QuestionInfo> options) {
        this.options = options;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question_id);
        dest.writeString(this.question);
        dest.writeInt(this.test_id);
        dest.writeInt(this.sort);
        dest.writeString(this.image);
        dest.writeInt(this.qid);
        dest.writeInt(this.option_id);
        dest.writeString(this.option_text);
        dest.writeInt(this.score);
        dest.writeInt(this.re_qid);
        dest.writeInt(this.aid);
        dest.writeList(this.options);
    }

    public QuestionInfo() {
    }

    protected QuestionInfo(Parcel in) {
        this.question_id = in.readString();
        this.question = in.readString();
        this.test_id = in.readInt();
        this.sort = in.readInt();
        this.image = in.readString();
        this.qid = in.readInt();
        this.option_id = in.readInt();
        this.option_text = in.readString();
        this.score = in.readInt();
        this.re_qid = in.readInt();
        this.aid = in.readInt();
        this.options = new ArrayList<QuestionInfo>();
        in.readList(this.options, QuestionInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<QuestionInfo> CREATOR = new Parcelable.Creator<QuestionInfo>() {
        @Override
        public QuestionInfo createFromParcel(Parcel source) {
            return new QuestionInfo(source);
        }

        @Override
        public QuestionInfo[] newArray(int size) {
            return new QuestionInfo[size];
        }
    };

    @Override
    public int getItemType() {
        return type;
    }
}
