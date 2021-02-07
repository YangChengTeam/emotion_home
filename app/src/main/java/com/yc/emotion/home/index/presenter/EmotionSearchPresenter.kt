package com.yc.emotion.home.index.presenter

import android.content.Context

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.EmotionSearchModel
import com.yc.emotion.home.index.view.EmotionSearchView
import com.yc.emotion.home.model.bean.IndexSearchInfo
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/19 14:52.
 */
class EmotionSearchPresenter(context: Context?, view: EmotionSearchView) : BasePresenter<EmotionSearchModel, EmotionSearchView>(context, view) {

    init {
        mModel = EmotionSearchModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun searchIndexInfo(keyword: String?, type: Int) {

        mModel?.searchIndexInfo(keyword, type)?.getData(mView, { it, _ ->
            if (it != null) {
                mView.showEmotionSearchResult(it)
            } else {
                mView.onNoData()
            }
        }, { _, _ -> mView.onComplete() })

    }
}