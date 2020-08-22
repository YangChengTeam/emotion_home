package com.yc.emotion.home.message.ui.activity

import android.os.Bundle
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity

/**
 * Created by suns  on 2020/8/4 15:03.
 */
class VideoDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        invadeStatusBar()
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_short_video_play
    }

    override fun initViews() {
//        short_video_view
    }
}