package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by suns  on 2020/6/5 09:21.
 */
class LiveModel(override var context: Context?) : IModel(context) {


    /**
     * 导师登录房间
     */
    fun liveLogin(username: String?, password: String?): io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>> {


        return request.liveLogin(username, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 创建房间
     */
    fun createRoom(userId: String?): io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>> {

        return request.createRoom(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 直播结束回调
     */

    fun liveEnd(roomId: String?): io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<String>> {

        return request.liveEnd(roomId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 解散群组
     */
    fun dismissGroup(group_id: String): io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<String>> {

        return request.dismissGroup(group_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}