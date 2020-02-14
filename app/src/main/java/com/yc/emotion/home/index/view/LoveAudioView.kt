package com.yc.emotion.home.index.view

import com.music.player.lib.bean.MusicInfo
import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.AudioDataInfo

/**
 *
 * Created by suns  on 2019/11/20 11:04.
 */
interface LoveAudioView : IView, IDialog, StateDefaultImpl {
    fun showAudioListInfo(list: List<MusicInfo>, b: Boolean) {}
    fun showAudioDetailInfo(musicInfo: MusicInfo?) {}
    fun showAudioCollectSuccess(collect: Boolean) {}
    fun showAudioCategoryList(list: List<AudioDataInfo>) {}
}