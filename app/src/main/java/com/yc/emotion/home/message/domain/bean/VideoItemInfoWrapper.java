package com.yc.emotion.home.message.domain.bean;

import com.yc.emotion.home.model.bean.CourseInfo;
import com.yc.emotion.home.model.bean.VideoItem;

import java.util.List;

/**
 * Created by suns  on 2020/8/6 17:23.
 */
public class VideoItemInfoWrapper {

    private List<VideoBannerInfo> banners;
    private List<CourseInfo> lessons;
    private List<VideoItem> videos;

    public List<VideoBannerInfo> getBanners() {
        return banners;
    }

    public void setBanners(List<VideoBannerInfo> banners) {
        this.banners = banners;
    }

    public List<CourseInfo> getLessons() {
        return lessons;
    }

    public void setLessons(List<CourseInfo> lessons) {
        this.lessons = lessons;
    }

    public List<VideoItem> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoItem> videos) {
        this.videos = videos;
    }
}
