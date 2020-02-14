package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.ExampDataBean
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/20 13:29.
 */
class LoveCaseModel(override var context: Context?) : IModel {

    fun exampLists(userId: String, page: Int, pageSize: Int): Observable<ResultInfo<ExampDataBean>> {
        val params = HashMap<String, String>()
        params["page"] = "$page"
        params["user_id"] = userId
        params["page_size"] = "$pageSize"
        //        requestParams(params);
        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.PRACTICE_LIS_URL, object : TypeReference<ResultInfo<ExampDataBean>>() {

        }.type, params,
                true,
                true, true) as Observable<ResultInfo<ExampDataBean>>
    }
}