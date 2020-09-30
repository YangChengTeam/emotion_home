package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.LiveInfoWrapper
import com.yc.emotion.home.mine.domain.bean.LiveVideoInfoWrapper
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.model.bean.IndexInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    fun getIndexInfo(): Observable<ResultInfo<IndexInfo>> {

        return request.getIndexInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取在线直播列表
     */
    fun getOnlineLiveList(): Observable<ResultInfo<LiveInfoWrapper>> {

        return request.getOnlineLiveList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取视频直播列表
     */
    fun getLiveVideoInfoList(): Observable<ResultInfo<LiveVideoInfoWrapper>?> {

        return request.getLiveVideoInfoList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun statisticsLive(id: String?): Observable<ResultInfo<String>?> {

        return request.statisticsLive(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun getRewardInfo(): Observable<ResultInfo<RewardInfo>>? {
        return request.getRewardInfo("${UserInfoHelper.instance.getUid()}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}