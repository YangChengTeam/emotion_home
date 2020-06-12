package com.yc.emotion.home.mine.domain.bean;

import java.util.List;

/**
 * Created by suns  on 2020/6/6 10:27.
 */
public class LiveInfoWrapper {
    private List<LiveInfo> list;
    private List<LiveInfo> recording;

    public List<LiveInfo> getList() {
        return list;
    }

    public void setList(List<LiveInfo> list) {
        this.list = list;
    }

    public List<LiveInfo> getRecording() {
        return recording;
    }

    public void setRecording(List<LiveInfo> recording) {
        this.recording = recording;
    }
}
