package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/14 11:49.
 */
class SkillModel(override var context: Context?) : IModel(context) {


    /**
     * 获取秘技主页数据
     */
    fun exampleTsCategory(): Flowable<ResultInfo<ExampleTsCategory>> {


        return request.exampleTsCategory()
    }


    fun categoryArticle(): Flowable<ResultInfo<List<CategoryArticleBean>>> {

        return request.categoryArticle()
    }

    /**
     * 秘技列表详情
     */
    fun exampleTsList(id: String?, page: Int, pageSize: Int): Flowable<ResultInfo<ExampDataBean>> {

        return request.exampleTsList(id, page, pageSize)
    }

    /**
     * 秘技列表
     */
    fun listsArticle(categoryId: String?, page: Int, pageSize: Int, url: String): Flowable<ResultInfo<List<LoveByStagesBean>>> {

        return request.listsArticle(categoryId, page, pageSize, URLConfig.getBaseUrl() + url)
    }


    fun detailArticle(id: String, userId: String, url: String): Flowable<ResultInfo<LoveByStagesDetailsBean>> {


        return request.detailArticle(id, userId, URLConfig.getBaseUrl() + url)
    }

    /**
     * 收藏秘技下的文章详情
     */
    fun collectSkillArticle(userId: String, articleId: String?, url: String): Flowable<ResultInfo<String>> {

        return request.collectSkillArticle(userId, articleId, URLConfig.getBaseUrl() + url)
    }

}