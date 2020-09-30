package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.LoveHealingBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2020/4/28 16:01.
 */
class LoveUpDownModel(override var context: Context?) : IModel(context) {


    fun recommendLovewords(userId: String?, page: String?, page_size: String?, url: String?): io.reactivex.Observable<ResultInfo<List<LoveHealingBean>>> {

        return request.recommendLovewords(userId, page, page_size, URLConfig.debugBaseUrl + url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun collectLovewords(userId: String?, lovewordsId: String?, url: String?): io.reactivex.Observable<ResultInfo<String>> {

        return request.collectLovewords(userId, lovewordsId, URLConfig.getUrlV1(url)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}