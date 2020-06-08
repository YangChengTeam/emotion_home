package com.yc.emotion.home.mine.domain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by suns  on 2020/6/5 10:51.
 */
public class LiveInfo implements Parcelable {

    /**
     * id : 8
     * username : 789456
     * nickname : 革新的
     * face : http://qg.bshu.com/uploads/20200605/7d8e9b2cfd4ace13c54a5cb14104cfee.jpeg
     * add_time : 1591174201
     */

    private String id;
    private String username;
    private String nickname;
    private String face;
    @JSONField(name = "add_time")
    private Long addTime;

    @JSONField(name = "room_id")
    private String roomId;

    @JSONField(name = "stream_id")
    private String streamId;


    //主播id
    private String user_id;

    //直播状态
    private int status;
    //直播封面
    private String live_cover;
    //直播标题
    private String live_title;

    @JSONField(name = "tutor_cat")
    public String liveType;//直播分类 关系经营导师

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLive_cover() {
        return live_cover;
    }

    public void setLive_cover(String live_cover) {
        this.live_cover = live_cover;
    }

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public LiveInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.nickname);
        dest.writeString(this.face);
        dest.writeValue(this.addTime);
        dest.writeString(this.roomId);
        dest.writeString(this.streamId);
        dest.writeString(this.user_id);
        dest.writeInt(this.status);
        dest.writeString(this.live_cover);
        dest.writeString(this.live_title);
    }

    protected LiveInfo(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.nickname = in.readString();
        this.face = in.readString();
        this.addTime = (Long) in.readValue(Long.class.getClassLoader());
        this.roomId = in.readString();
        this.streamId = in.readString();
        this.user_id = in.readString();
        this.status = in.readInt();
        this.live_cover = in.readString();
        this.live_title = in.readString();
    }

    public static final Creator<LiveInfo> CREATOR = new Creator<LiveInfo>() {
        @Override
        public LiveInfo createFromParcel(Parcel source) {
            return new LiveInfo(source);
        }

        @Override
        public LiveInfo[] newArray(int size) {
            return new LiveInfo[size];
        }
    };
}
