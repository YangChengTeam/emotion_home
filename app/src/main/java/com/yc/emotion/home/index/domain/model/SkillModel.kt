package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.*
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/14 11:49.
 */
class SkillModel(override var context: Context?) : IModel {


    /**
     * 获取秘技主页数据
     */
    fun exampleTsCategory(): Observable<AResultInfo<ExampleTsCategory>> {
        val params = HashMap<String, String>()

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.SKILL_MAIN_URL, object : TypeReference<AResultInfo<ExampleTsCategory>>() {

        }.type,
                params,
                true,
                true, true) as Observable<AResultInfo<ExampleTsCategory>>
    }


    fun categoryArticle(): Observable<AResultInfo<List<CategoryArticleBean>>> {
        val params = HashMap<String, String>()
        //        params.put("password", password);

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + "article.example/article_category", object : TypeReference<AResultInfo<List<CategoryArticleBean>>>() {

        }.type,
                params,
                true,
                true, true) as Observable<AResultInfo<List<CategoryArticleBean>>>
    }

    /**
     * 秘技列表详情
     */
    fun exampleTsList(id: String?, page: Int, pageSize: Int): Observable<AResultInfo<ExampDataBean>> {
        val params = HashMap<String, String?>()
        params["category_id"] = id
        params["page"] = "$page"
        params["page_size"] = "$pageSize"

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getUrl("article.example/ts_lists"), object : TypeReference<AResultInfo<ExampDataBean>>() {

        }.type, params, true, true, true) as Observable<AResultInfo<ExampDataBean>>
    }

    /**
     * 秘技列表
     */
    fun listsArticle(categoryId: String?, page: Int, pageSize: Int, url: String): Observable<AResultInfo<List<LoveByStagesBean>>> {
        val params = HashMap<String, String?>()
        params["category_id"] = categoryId
        params["page"] = "$page"
        params["page_size"] = "$pageSize"

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + url, object : TypeReference<AResultInfo<List<LoveByStagesBean>>>() {
        }.type, params,
                true,
                true, true) as Observable<AResultInfo<List<LoveByStagesBean>>>
    }


    fun detailArticle(id: String, userId: String, url: String): Observable<AResultInfo<LoveByStagesDetailsBean>> {
        val params = HashMap<String, String>()
        params["id"] = id
        params["user_id"] = userId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + url, object : TypeReference<AResultInfo<LoveByStagesDetailsBean>>() {

        }.type,
                params,
                true,
                true, true) as Observable<AResultInfo<LoveByStagesDetailsBean>>
    }

    /**
     * 收藏秘技下的文章详情
     */
    fun collectSkillArticle(userId: String, articleId: String?, url: String): Observable<AResultInfo<String>> {
        val params = HashMap<String, String?>()
        params["user_id"] = userId
        params["example_id"] = articleId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + url, object : TypeReference<AResultInfo<String>>() {

        }.type,
                params,
                true,
                true, true) as Observable<AResultInfo<String>>
    }

}