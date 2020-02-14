package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/14 10:51.
 */
class LoveCaseDetailModel(override var context: Context?) : IModel {


    /**
     * 实战案例详情
     */

    fun detailLoveCase(id: String, userId: String): Observable<AResultInfo<LoveByStagesDetailsBean>> {
        val params = HashMap<String, String>()
        params["id"] = id
        params["user_id"] = userId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.VERBAL_PRATICE_DETAIL_URL, object : TypeReference<AResultInfo<LoveByStagesDetailsBean>>() {

        }.type, params, true, true, true) as Observable<AResultInfo<LoveByStagesDetailsBean>>
    }

    /**
     * 实战案例收藏/取消收藏
     */

    fun collectLoveCase(userId: String, exampleId: String, url: String): Observable<AResultInfo<String>> {
        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["example_id"] = exampleId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + url, object : TypeReference<AResultInfo<String>>() {

        }.type, params, true,
                true, true) as Observable<AResultInfo<String>>
    }
}