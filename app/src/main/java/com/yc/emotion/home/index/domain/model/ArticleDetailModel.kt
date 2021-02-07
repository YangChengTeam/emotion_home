package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/12 11:08.
 */
class ArticleDetailModel(override var context: Context?) : IModel(context) {


    fun getArticleDetai(id: String, userId: String): Flowable<ResultInfo<LoveByStagesDetailsBean>> {

        return request.getArticleDetail(id, userId)
    }


    /**
     * 文章收藏/点赞
     *
     * @param userId
     * @param exampleId
     * @param url
     * @return
     */
    fun articleCollect(userId: String, exampleId: String, url: String): Flowable<ResultInfo<String>> {

        return request.articleCollect(userId, exampleId, URLConfig.getBaseUrl() + url)
    }
}