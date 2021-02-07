package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 * Created by suns  on 2020/6/5 09:21.
 */
class LiveModel(override var context: Context?) : IModel(context) {


    /**
     * 导师登录房间
     */
    fun liveLogin(username: String?, password: String?): Flowable<ResultInfo<LiveInfo>> {


        return request.liveLogin(username, password)
    }

    /**
     * 创建房间
     */
    fun createRoom(userId: String?): Flowable<ResultInfo<LiveInfo>> {

        return request.createRoom(userId)
    }

    /**
     * 直播结束回调
     */

    fun liveEnd(roomId: String?): Flowable<ResultInfo<String>> {

        return request.liveEnd(roomId)
    }

    /**
     * 解散群组
     */
    fun dismissGroup(group_id: String): Flowable<ResultInfo<String>> {

        return request.dismissGroup(group_id)
    }

}