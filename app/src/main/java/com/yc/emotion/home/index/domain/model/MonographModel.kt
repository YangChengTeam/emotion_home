package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2020/4/29 10:49.
 */
class MonographModel(override var context: Context?) : IModel(context) {

    fun getMonographArticles(series: String?, page: Int, page_size: Int): Observable<ResultInfo<List<ArticleDetailInfo>>> {
        //series=jituixiaosan&user_id=1&page=1&page_size=10

        return request.getMonographArticles(series,"${UserInfoHelper.instance.getUid()}",page, page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}