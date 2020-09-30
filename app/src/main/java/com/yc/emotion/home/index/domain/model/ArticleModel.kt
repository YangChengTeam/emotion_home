package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.AticleTagInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/8 11:22.
 */
class ArticleModel(override var context: Context?) : IModel(context) {

    /**
     * 更多文章分类类别
     */
    fun getArticleTagInfos(): Observable<ResultInfo<List<AticleTagInfo>>> {


        return request.getArticleTagInfos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取文章分类列表
     *
     * @param cat_id
     * @return
     */
    fun getArticleInfoList(cat_id: Int?, sex: Int, page: Int, page_size: Int): Observable<ResultInfo<List<ArticleDetailInfo>>> {


        return request.getArticleInfoList(cat_id, sex, page, page_size).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}