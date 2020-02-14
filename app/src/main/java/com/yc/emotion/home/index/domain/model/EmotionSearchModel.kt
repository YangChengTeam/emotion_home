package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.IndexSearchInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/19 14:54.
 */
class EmotionSearchModel(override var context: Context?) : IModel {

    /**
     * 首页搜索内容
     *
     * @param keyword
     * @param type    1搜索内容   2搜索导师
     * @return
     */
    fun searchIndexInfo(keyword: String?, type: Int): Observable<ResultInfo<IndexSearchInfo>> {
        val params = HashMap<String, String?>()

        params["keyword"] = keyword
        params["type"] = type.toString() + ""

        return HttpCoreEngin.get(context).rxpost(URLConfig.INDEX_SEARCH_URL, object : TypeReference<ResultInfo<IndexSearchInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<IndexSearchInfo>>
    }
}