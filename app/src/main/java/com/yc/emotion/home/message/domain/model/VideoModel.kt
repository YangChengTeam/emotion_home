package com.yc.emotion.home.message.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.message.domain.bean.VideoItemInfoWrapper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2020/8/6 17:25.
 */
class VideoModel(override var context: Context?) : IModel(context) {
    fun getVideoItemInfos(page: Int, page_size: Int): Flowable<ResultInfo<VideoItemInfoWrapper>> {

        return request.getVideoItemInfos(page, page_size)


    }

    fun statisticsVideo(id: String): Flowable<ResultInfo<String>> {


        return request.statisticsVideo(id)
    }
}