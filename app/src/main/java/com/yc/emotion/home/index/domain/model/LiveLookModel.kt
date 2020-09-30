package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.index.domain.bean.UserSeg
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 *
 * Created by suns  on 2020/6/9 13:39.
 */
class LiveLookModel(override var context: Context?) : IModel(context) {

    fun getLiveLookInfo(roomId: String?): io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>> {


        return request.getLiveLookInfo(roomId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserSeg(user_id: String?): io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<UserSeg>> {

        return request.getUserSeg(user_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}