package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.index.domain.bean.UserSeg
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import rx.Observable

/**
 *
 * Created by suns  on 2020/6/9 13:39.
 */
class LiveLookModel(override var context: Context?) : IModel {

    fun getLiveLookInfo(roomId: String?): Observable<ResultInfo<LiveInfo>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.LOOK_LIVE_URL, object : TypeReference<ResultInfo<LiveInfo>>() {}.type, mutableMapOf(
                "room_id" to roomId
        ), true, true, true) as Observable<ResultInfo<LiveInfo>>

    }

    fun getUserSeg(user_id: String): Observable<ResultInfo<UserSeg>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.USER_SIGN_URL, object : TypeReference<ResultInfo<UserSeg>>() {}.type, mutableMapOf(
                "user_id" to user_id
        ), true, true, true) as Observable<ResultInfo<UserSeg>>
    }
}