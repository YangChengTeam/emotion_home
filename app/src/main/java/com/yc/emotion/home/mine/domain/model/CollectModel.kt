package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.ModelApp
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.model.dao.LoveHealDetDetailsBeanDao
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field


import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/15 16:08.
 */
class CollectModel(override var context: Context?) : IModel(context) {
    private var detailsBeanDao: LoveHealDetDetailsBeanDao? = null

    init {
        detailsBeanDao = ModelApp.getDaoSession().loveHealDetDetailsBeanDao
    }

    /**
     * 获取课程收藏列表
     *
     * @param userId
     * @param page
     * @param page_size
     * @return
     */
    fun getCourseCollectList(userId: String, page: Int, page_size: Int): Observable<ResultInfo<List<CourseInfo>>> {


        return request.getCourseCollectList(userId, page, page_size)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取话术收藏列表
     */
    fun getCollectLoveHeals(limit: Int, offset: Int): Observable<List<LoveHealDetDetailsBean>> {
        val userId = UserInfoHelper.instance.getUid()
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map { detailsBeanDao?.queryBuilder()?.where(LoveHealDetDetailsBeanDao.Properties.UserId.eq("$userId"))?.offset(offset * limit)?.limit(limit)?.orderDesc(LoveHealDetDetailsBeanDao.Properties.SaveTime)?.list() }
    }

    fun getVerbalList(page: Int, page_size: Int): Observable<ResultInfo<List<LoveHealDetDetailsBean>>>? {
        return request.getVerbalList("${UserInfoHelper.instance.getUid()}", page, page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取文章收藏列表
     *
     * @param userId
     * @return
     */
    fun getArticleCollectList(userId: String, page: Int, pageSize: Int): Observable<ResultInfo<List<ArticleDetailInfo>>> {


        return request.getArticleCollectList(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 取消收藏
     */
    fun deleteCollectLoveHeals(detDetailsBean: LoveHealDetDetailsBean?): Observable<String>? {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map { s ->
            if (detDetailsBean == null) return@map ""
            detailsBeanDao?.delete(detDetailsBean)
            return@map "success"
        }
    }


    /**
     * 收藏
     */

    fun collectLoveHeal(detDetailsBean: LoveHealDetDetailsBean?): Observable<String> {

        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map { s ->
            if (detDetailsBean == null) return@map ""

            val queryBean = getCollectLoveHealById(detDetailsBean.content)
            if (queryBean == null) {
                detDetailsBean.saveTime = System.currentTimeMillis()
                val instance = UserInfoHelper.instance

                val uId = instance.getUid()
                detDetailsBean.userId = "$uId"
                detailsBeanDao?.insert(detDetailsBean)
            } else {
                queryBean.saveTime = System.currentTimeMillis()
                detailsBeanDao?.update(queryBean)
            }

            "success"
        }


    }

    private fun getCollectLoveHealById(content: String): LoveHealDetDetailsBean? {
        return detailsBeanDao?.queryBuilder()?.where(LoveHealDetDetailsBeanDao.Properties.Content.eq(content))?.unique()
    }
}


