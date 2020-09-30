package com.yc.emotion.home.message.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.message.domain.bean.VideoItemInfo

/**
 *
 * Created by suns  on 2020/8/6 17:33.
 */
interface VideoView : IView, IDialog, StateDefaultImpl {
    fun showVideoInfoList(videoItemInfoList: MutableList<VideoItemInfo>, size: Int)
}