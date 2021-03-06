package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.IndexHotInfoWrapper
import com.yc.emotion.home.model.bean.LoveHealDateBean
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.SearchDialogueBean
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/13 13:48.
 */
class IndexVerbalModel(override var context: Context?) : IModel(context) {

    /**
     * 首页下拉热词
     *
     * @return
     */

    fun getIndexDropInfos(keyword: String?): Flowable<ResultInfo<IndexHotInfoWrapper>> {


        return request.getIndexDropInfos(keyword)
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
    fun searchVerbalTalk(userId: String, keyword: String?, page: Int, pageSize: Int): Flowable<ResultInfo<SearchDialogueBean>> {

        return request.searchVerbalTalk(userId, keyword, page, pageSize)
    }


    /**
     * 统计搜索次数
     *
     * @param userId
     * @param keyword
     * @return
     */
    fun searchCount(userId: String, keyword: String?): Flowable<ResultInfo<String>> {

        return request.searchCount(userId, keyword)
    }

    /**
     * 场景话术和应用话术
     */
    fun loveCategory(sence: String): Flowable<ResultInfo<List<LoveHealDateBean>>> {

        return request.loveCategory(sence)
    }


    /**
     * 话术详情
     */
    fun loveListCategory(userId: String, category_id: String?, page: Int, page_size: Int): Flowable<ResultInfo<List<LoveHealDetBean>>> {


        return request.loveListCategory(userId, category_id, page, page_size)
    }
}