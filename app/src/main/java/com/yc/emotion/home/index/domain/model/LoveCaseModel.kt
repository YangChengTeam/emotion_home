package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.ExampDataBean
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/20 13:29.
 */
class LoveCaseModel(override var context: Context?) : IModel(context) {

    fun exampLists(userId: String, page: Int, pageSize: Int): Flowable<ResultInfo<ExampDataBean>> {


        return request.exampLists(userId, page, pageSize)
    }
}