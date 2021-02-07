package com.yc.emotion.home.base.domain.engine

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference


import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

import java.util.*

import io.reactivex.Observable

/**
 * Created by mayn on 2019/5/9.
 */
class BaseModel(context: Context?) : IModel(context) {
    fun recommendLovewords(userId: String?, page: String?, page_size: String?, url: String): Flowable<ResultInfo<List<LoveHealingBean>>> {

        return request.recommendLovewords(userId, page, page_size, URLConfig.debugBaseUrl + url)
    }


    /**
     * 分享得会员
     *
     * @param userId
     * @return
     */
    fun shareReward(userId: String?): Flowable<ResultInfo<String>> {

        return request.shareReward(userId)
    }

    /**
     * 获取微信信息
     *
     * @param tutor_id
     * @param article_id
     * @param example_id
     * @return
     */
    fun getWechatInfo(tutor_id: String?, article_id: String?, example_id: String?, position: String?): Observable<ResultInfo<WetChatInfo>> {
        val params: MutableMap<String, String?> = HashMap()
        if (!TextUtils.isEmpty(tutor_id)) params["tutor_id"] = tutor_id
        if (!TextUtils.isEmpty(article_id)) {
            params["article_id"] = article_id
        }
        if (!TextUtils.isEmpty(example_id)) {
            params["example_id"] = example_id
        }
        if (!TextUtils.isEmpty(position)) params["position"] = position


        return request.getWechatInfo(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getShareLink(): Observable<ResultInfo<ShareInfo>>? {
        return request.getShareLink("${UserInfoHelper.instance.getUid()}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}