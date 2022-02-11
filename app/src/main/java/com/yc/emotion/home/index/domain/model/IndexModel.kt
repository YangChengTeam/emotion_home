package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.LiveInfoWrapper
import com.yc.emotion.home.mine.domain.bean.LiveVideoInfoWrapper
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.model.bean.IndexInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field
import yc.com.rthttplibrary.bean.ResultInfo


/**
 *
 * Created by suns  on 2019/11/7 16:38.
 */
class IndexModel(override var context: Context?) : IModel(context) {

    /**
     * 获取首页数据
     *
     * @return
     */
    fun getIndexInfo(): Flowable<ResultInfo<IndexInfo>> {

        return request.getIndexInfo()
    }

    /**
     * 获取在线直播列表
     */
    fun getOnlineLiveList(): Flowable<ResultInfo<LiveInfoWrapper>> {

        return request.getOnlineLiveList()
    }


    /**
     * 获取视频直播列表
     */
    fun getLiveVideoInfoList(): Flowable<ResultInfo<LiveVideoInfoWrapper>> {

        return request.getLiveVideoInfoList()
    }

    fun statisticsLive(id: String?): Flowable<ResultInfo<String>> {

        return request.statisticsLive(id)
    }


    fun getRewardInfo(): Flowable<ResultInfo<RewardInfo>> {
        return request.getRewardInfo("${UserInfoHelper.instance.getUid()}")
    }

    fun dailyCount(message_id: Int): Flowable<ResultInfo<String>> {

        return request.dailyCount(message_id)
    }
}