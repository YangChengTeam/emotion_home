package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/14 10:51.
 */
class LoveCaseDetailModel(override var context: Context?) : IModel(context) {


    /**
     * 实战案例详情
     */

    fun detailLoveCase(id: String, userId: String): Flowable<ResultInfo<LoveByStagesDetailsBean>> {


        return request.detailLoveCase(id, userId)
    }

    /**
     * 实战案例收藏/取消收藏
     */

    fun collectLoveCase(userId: String, exampleId: String, url: String): Flowable<ResultInfo<String>> {

        return request.collectLoveCase(userId, exampleId, URLConfig.getBaseUrl() + url)
    }
}