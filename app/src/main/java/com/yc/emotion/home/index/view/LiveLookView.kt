package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.domain.bean.LiveInfo

/**
 *
 * Created by suns  on 2020/6/9 13:45.
 */
interface LiveLookView : IView {
    fun showLiveInfo(data: LiveInfo?)
    fun showUserSeg(usersig: String)
}