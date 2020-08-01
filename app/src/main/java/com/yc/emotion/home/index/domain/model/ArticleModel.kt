package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.AticleTagInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/8 11:22.
 */
class ArticleModel(override var context: Context?) : IModel {

    /**
     * 更多文章分类类别
     */
    fun getArticleTagInfos(): Observable<ResultInfo<List<AticleTagInfo>>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.ARTICLE_CATEGORY_URL, object : TypeReference<ResultInfo<List<AticleTagInfo>>>() {

        }.type, null, true, true, true) as Observable<ResultInfo<List<AticleTagInfo>>>
    }


    /**
     * 获取文章分类列表
     *
     * @param cat_id
     * @return
     */
    fun getArticleInfoList(cat_id: Int?, sex: Int, page: Int, page_size: Int): Observable<ResultInfo<List<ArticleDetailInfo>>> {
        val params = HashMap<String, String>()

        params["cat_id"] = "$cat_id"
        params["sex"] = "$sex"
        params["page"] = "$page"
        params["page_size"] = "$page_size"


        return HttpCoreEngin.get(context).rxpost(URLConfig.ARTICLE_LIST_URL, object : TypeReference<ResultInfo<List<ArticleDetailInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<ArticleDetailInfo>>>
    }
}