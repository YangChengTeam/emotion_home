package com.yc.emotion.home.message.presenter

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer
import com.yc.emotion.home.base.EmApplication
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.message.domain.bean.VideoItemInfo
import com.yc.emotion.home.message.domain.bean.VideoItemInfoWrapper
import com.yc.emotion.home.message.domain.model.VideoModel
import com.yc.emotion.home.message.view.VideoView
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2020/8/6 17:32.
 */
class VideoPresenter(context: Context?, view: VideoView) : BasePresenter<VideoModel, VideoView>(context, view) {
    private val proxy: HttpProxyCacheServer? = EmApplication.instance.getProxy()

    init {
        mModel = VideoModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getVideoItemInfos(page: Int, page_size: Int) {
        mModel?.getVideoItemInfos(page, page_size)?.getData(mView, { it, _ ->
            if (it != null) {
                createNewData(page, page_size, it)
            } else {
                mView.onError()
            }
        }, { _, _ -> })


    }

    fun statisticsVideo(id: String) {
        mModel?.statisticsVideo(id)?.getData(mView,{it,_->},{_,_->},false)


    }

    private fun createNewData(page: Int, pageSize: Int, data: VideoItemInfoWrapper) {

        val videoItemInfoList = mutableListOf<VideoItemInfo>()

        val banners = data.banners
        val lessons = data.lessons
        val videos = data.videos
        if (page == 1) {
            var videoItemInfo = VideoItemInfo(VideoItemInfo.ITEM_TOP_BANNER)
            videoItemInfo.banners = banners
            videoItemInfoList.add(videoItemInfo)

            videoItemInfo = VideoItemInfo(VideoItemInfo.ITEM_TITLE)
            videoItemInfo.title = "高效课程"
            videoItemInfoList.add(videoItemInfo)

            for (lesson in lessons) {
                videoItemInfo = VideoItemInfo(VideoItemInfo.ITEM_COURSE)
                videoItemInfo.courseInfo = lesson
                videoItemInfoList.add(videoItemInfo)

            }
            videoItemInfo = VideoItemInfo(VideoItemInfo.ITEM_DIVIDER)

            videoItemInfoList.add(videoItemInfo)

            videoItemInfo = VideoItemInfo(VideoItemInfo.ITEM_TITLE)
            videoItemInfo.title = "小视频"
            videoItemInfoList.add(videoItemInfo)
        }


        for (video in videos) {
            val videoItemInfo = VideoItemInfo(VideoItemInfo.ITEM_VIDEO)

            var proxyUrl = video.videoUrl
//            proxyUrl="http://qg-bshu.zhuoyi52.com/videos/mda-kapy4qefphqd6ijn.mp4"
            proxy?.let {
                proxyUrl = proxy.getProxyUrl(proxyUrl)
            }
            video.videoUrl = proxyUrl
            videoItemInfo.videoItem = video
            videoItemInfoList.add(videoItemInfo)
        }

        mView.showVideoInfoList(videoItemInfoList, lessons.size)

        if (videos.size == pageSize) {
            mView.onComplete()
        } else {
            mView.onEnd()
        }


    }
}