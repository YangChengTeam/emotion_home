package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Observable

/**
 *
 * Created by suns  on 2020/4/29 10:49.
 */
class MonographModel(override var context: Context?) : IModel {

    fun getMonographArticles(series: String?, page: Int, page_size: Int): Observable<ResultInfo<List<ArticleDetailInfo>>> {
        //series=jituixiaosan&user_id=1&page=1&page_size=10
        return HttpCoreEngin.get(context).rxpost(URLConfig.MONOGRAPH_ARTICLE_URL, object : TypeReference<ResultInfo<List<ArticleDetailInfo>>>() {}.type,
                mutableMapOf(
                        "series" to series,
                        "user_id" to "${UserInfoHelper.instance.getUid()}",
                        "page" to "$page",
                        "page_size" to "$page_size"
                ), true, true, true) as Observable<ResultInfo<List<ArticleDetailInfo>>>
    }
}