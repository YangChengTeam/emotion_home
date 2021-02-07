package com.yc.emotion.home.index.domain.model

import android.content.Context

import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.index.domain.bean.SmartChatItem
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field
import yc.com.rthttplibrary.bean.ResultInfo


/**
 *
 * Created by suns  on 2020/8/11 18:24.
 */
class AIChatModel(override var context: Context?) : IModel(context) {

    fun getAIChatContent(key: String): Flowable<ResultInfo<String>> {
        val params = hashMapOf<String, String>()
        params["s_key"] = key
        val uid = UserInfoHelper.instance.getUid()
        if (uid > 0) {
            params["user_id"] = "$uid"
        }
        return request.getAIChatContent(params)
    }


    fun smartSearchVerbal(keyword: String?, section: Int): Flowable<ResultInfo<SmartChatItem>> {
        return request.smartSearchVerbal(keyword, "${UserInfoHelper.instance.getUid()}", section)
    }


    fun aiPraise(id: String?): Flowable<ResultInfo<String>> {
        return request.aiPraise(id, "${UserInfoHelper.instance.getUid()}")
    }


    fun aiCollect(id: String?): Flowable<ResultInfo<String>>? {
        return request.aiCollect(id, "${UserInfoHelper.instance.getUid()}")
    }
}