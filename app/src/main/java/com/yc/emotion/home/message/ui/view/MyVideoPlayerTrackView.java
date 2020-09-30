package com.yc.emotion.home.message.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.video.player.lib.R;
import com.video.player.lib.base.BaseVideoPlayer;
import com.video.player.lib.controller.DefaultCoverController;
import com.video.player.lib.controller.DefaultGestureController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MyVideoPlayerTrackView extends BaseVideoPlayer<MyVideoController,
        DefaultCoverController, DefaultGestureController> {

    @Override
    protected int getLayoutID() {
        return R.layout.video_default_track_layout;
    }

    public MyVideoPlayerTrackView(@NonNull Context context) {
        this(context,null);
    }

    public MyVideoPlayerTrackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyVideoPlayerTrackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}