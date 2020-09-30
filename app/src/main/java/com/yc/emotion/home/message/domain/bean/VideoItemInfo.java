package com.yc.emotion.home.message.domain.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.emotion.home.model.bean.BannerInfo;
import com.yc.emotion.home.model.bean.CourseInfo;
import com.yc.emotion.home.model.bean.VideoItem;

import java.util.List;

/**
 * Created by suns  on 2020/8/5 08:47.
 */
public class VideoItemInfo implements MultiItemEntity {
    private int type;

    public static final int ITEM_TOP_BANNER = 1;
    public static final int ITEM_TITLE = 2;
    public static final int ITEM_COURSE = 3;
    public static final int ITEM_VIDEO = 4;
    public static final int ITEM_DIVIDER = 5;

    private CourseInfo courseInfo;
    private VideoItem videoItem;
    private List<VideoBannerInfo> banners;

    private String title;

    public VideoItemInfo() {
    }

    public VideoItemInfo(int type) {
        this.type = type;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public VideoItem getVideoItem() {
        return videoItem;
    }

    public void setVideoItem(VideoItem videoItem) {
        this.videoItem = videoItem;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<VideoBannerInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<VideoBannerInfo> banners) {
        this.banners = banners;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
