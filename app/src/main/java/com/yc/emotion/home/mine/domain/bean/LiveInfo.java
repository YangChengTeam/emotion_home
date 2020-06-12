package com.yc.emotion.home.mine.domain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

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
    private int roomId;

    @JSONField(name = "stream_id")
    private String streamId;

    private String usersig;

    private int sdkappid;

    private String ppt_img;
    private List<String> weixin;

    //主播id
    private String user_id;

    //直播状态
    private int status;
    //直播封面
    private String live_cover;
    //直播标题
    private String live_title;

    @JSONField(name = "teacher")
    public String liveType;//直播分类 关系经营导师

    private int people_num;
    //回看地址
    private String record_url;
    //直播开始时间
    private long start_time;
    //直播结束时间
    private long end_time;

    private String pre_cover;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
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

    public String getUsersig() {
        return usersig;
    }

    public void setUsersig(String usersig) {
        this.usersig = usersig;
    }

    public int getSdkappid() {
        return sdkappid;
    }

    public void setSdkappid(int sdkappid) {
        this.sdkappid = sdkappid;
    }

    public String getLiveType() {
        return liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public String getPpt_img() {
        return ppt_img;
    }

    public void setPpt_img(String ppt_img) {
        this.ppt_img = ppt_img;
    }

    public List<String> getWeixin() {
        return weixin;
    }

    public void setWeixin(List<String> weixin) {
        this.weixin = weixin;
    }

    public int getPeople_num() {
        return people_num;
    }

    public void setPeople_num(int people_num) {
        this.people_num = people_num;
    }

    public String getRecord_url() {
        return record_url;
    }

    public void setRecord_url(String record_url) {
        this.record_url = record_url;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getPre_cover() {
        return pre_cover;
    }

    public void setPre_cover(String pre_cover) {
        this.pre_cover = pre_cover;
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
        dest.writeInt(this.roomId);
        dest.writeString(this.streamId);
        dest.writeString(this.user_id);
        dest.writeInt(this.status);
        dest.writeString(this.live_cover);
        dest.writeString(this.live_title);
        dest.writeString(this.record_url);
        dest.writeString(this.ppt_img);
        dest.writeLong(this.start_time);
        dest.writeLong(this.end_time);
        dest.writeString(this.pre_cover);
    }

    protected LiveInfo(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.nickname = in.readString();
        this.face = in.readString();
        this.addTime = (Long) in.readValue(Long.class.getClassLoader());
        this.roomId = in.readInt();
        this.streamId = in.readString();
        this.user_id = in.readString();
        this.status = in.readInt();
        this.live_cover = in.readString();
        this.live_title = in.readString();
        this.record_url = in.readString();
        this.ppt_img = in.readString();
        this.start_time = in.readLong();
        this.end_time = in.readLong();
        this.pre_cover = in.readString();
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
