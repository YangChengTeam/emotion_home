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
 * Created by suns  on 2019/11/12 11:08.
 */
class ArticleDetailModel(override var context: Context?) : IModel {


    fun getArticleDetai(id: String, userId: String): Observable<AResultInfo<LoveByStagesDetailsBean>> {
        val params = HashMap<String, String>()
        params["article_id"] = id
        params["user_id"] = userId
        //        requestParams(params);
        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.ARTICLE_DETAIL_URL, object : TypeReference<AResultInfo<LoveByStagesDetailsBean>>() {

        }.type, params, true, true, true) as Observable<AResultInfo<LoveByStagesDetailsBean>>
    }


    /**
     * 文章收藏/点赞
     *
     * @param userId
     * @param exampleId
     * @param url
     * @return
     */
    fun articleCollect(userId: String, exampleId: String, url: String): Observable<AResultInfo<String>> {
        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["article_id"] = exampleId
        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + url, object : TypeReference<AResultInfo<String>>() {
        }.type, params, true, true, true) as Observable<AResultInfo<String>>
    }
}