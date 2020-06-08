package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveHealingBean
import rx.Observable
import java.util.*

/**
 *
 * Created by suns  on 2020/4/28 16:01.
 */
class LoveUpDownModel(override var context: Context?) : IModel {


    fun recommendLovewords(userId: String?, page: String?, page_size: String?, url: String?): Observable<AResultInfo<List<LoveHealingBean>>> {
        val params: MutableMap<String?, String?> = HashMap()
        params["user_id"] = userId
        params["page"] = page
        params["page_size"] = page_size

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.debugBaseUrl + url, object : TypeReference<AResultInfo<List<LoveHealingBean>>>() {}.type,
                params,
                true,
                true, true) as Observable<AResultInfo<List<LoveHealingBean>>>
    }

    fun collectLovewords(userId: String?, lovewordsId: String?, url: String?): Observable<AResultInfo<String>> {
        val params: MutableMap<String?, String?> = HashMap()
        params["user_id"] = userId
        params["lovewords_id"] = lovewordsId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getUrlV1(url), object : TypeReference<AResultInfo<String?>?>() {}.type,
                params,
                true,
                true, true) as Observable<AResultInfo<String>>
    }

}