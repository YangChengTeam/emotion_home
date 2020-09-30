package com.yc.emotion.home.message.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.MessageInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/15 14:03.
 */
class MessageModel(override var context: Context?) : IModel(context) {


    /**
     * 获取消息信息
     *
     * @return
     */
    fun getMessageInfoList(): Observable<ResultInfo<List<MessageInfo>>> {


        return request.getMessageInfoList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 通知详情
     *
     * @param id
     * @return
     */
    fun getNotificationDetail(id: String): Observable<ResultInfo<MessageInfo>> {


        return request.getNotificationDetail(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}