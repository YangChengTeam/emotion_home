package com.yc.emotion.home.index.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.EmotionSearchModel
import com.yc.emotion.home.index.view.EmotionSearchView
import com.yc.emotion.home.model.bean.IndexSearchInfo
import rx.Subscriber

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
        mView.showLoadingDialog()
        val subscription = mModel?.searchIndexInfo(keyword, type)?.subscribe(object : Subscriber<ResultInfo<IndexSearchInfo>>() {
            override fun onNext(t: ResultInfo<IndexSearchInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showEmotionSearchResult(t.data)
                    }else{
                        mView.onNoData()
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })

        subScriptions?.add(subscription)
    }
}