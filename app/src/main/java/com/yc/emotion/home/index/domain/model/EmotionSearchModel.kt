package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.IndexSearchInfo
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/19 14:54.
 */
class EmotionSearchModel(override var context: Context?) : IModel(context) {

    /**
     * 首页搜索内容
     *
     * @param keyword
     * @param type    1搜索内容   2搜索导师
     * @return
     */
    fun searchIndexInfo(keyword: String?, type: Int): Flowable<ResultInfo<IndexSearchInfo>> {


        return request.searchIndexInfo(keyword, type)
    }
}