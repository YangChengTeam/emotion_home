package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.*
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/13 13:48.
 */
class IndexVerbalModel(override var context: Context?) : IModel {

    /**
     * 首页下拉热词
     *
     * @return
     */

    fun getIndexDropInfos(keyword: String?): Observable<ResultInfo<IndexHotInfoWrapper>> {
        val params = HashMap<String, String?>()
        params["keyword"] = keyword

        return HttpCoreEngin.get(context).rxpost(URLConfig.INDEX_DROP_URL, object : TypeReference<ResultInfo<IndexHotInfoWrapper>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<IndexHotInfoWrapper>>
    }


    /**
     * 搜索话术
     *
     * @param userId
     * @param keyword
     * @param page
     * @param pageSize
     * @param url
     * @return
     */
    fun searchVerbalTalk(userId: String, keyword: String?, page: Int, pageSize: Int): Observable<AResultInfo<SearchDialogueBean>> {
        val params = HashMap<String, String?>()
        params["user_id"] = userId
        params["page"] = "$page"
        params["keyword"] = keyword
        params["page_size"] = "$pageSize"

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.VERBAL_SEARCH_URL, object : TypeReference<AResultInfo<SearchDialogueBean>>() {

        }.type, params, true,
                true, true) as Observable<AResultInfo<SearchDialogueBean>>
    }


    /**
     * 统计搜索次数
     *
     * @param userId
     * @param keyword
     * @return
     */
    fun searchCount(userId: String, keyword: String?): Observable<ResultInfo<String>> {
        val params = HashMap<String, String?>()

        params["user_id"] = userId
        params["keyword"] = keyword

        return HttpCoreEngin.get(context).rxpost(URLConfig.SEARCH_COUNT_URL, object : TypeReference<ResultInfo<String>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<String>>
    }

    /**
     * 场景话术和应用话术
     */
    fun loveCategory(sence: String): Observable<AResultInfo<List<LoveHealDateBean>>> {
        val params = HashMap<String, String>()
        params["sence"] = sence

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.DIALOGUE_CATEGORY_URL, object : TypeReference<AResultInfo<List<LoveHealDateBean>>>() {

        }.type, params,
                true,
                true, true) as Observable<AResultInfo<List<LoveHealDateBean>>>
    }


    /**
     * 话术详情
     */
    fun loveListCategory(userId: String, category_id: String?, page: Int, page_size: Int): Observable<AResultInfo<List<LoveHealDetBean>>> {
        val params = HashMap<String, String?>()
        params["user_id"] = userId
        params["category_id"] = category_id
        params["page"] = "$page"
        params["page_size"] = "$page_size"

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.DIALOGUE_DETAIL_LISTS_URL, object : TypeReference<AResultInfo<List<LoveHealDetBean>>>() {

        }.type,
                params,
                true,
                true, true) as Observable<AResultInfo<List<LoveHealDetBean>>>
    }
}