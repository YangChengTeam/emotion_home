package com.yc.emotion.home.index.presenter

import android.content.Context

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.MonographModel
import com.yc.emotion.home.index.view.MonagraphView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2020/4/29 10:55.
 */
class MonagraphPresenter(context: Context?, view: MonagraphView) : BasePresenter<MonographModel, MonagraphView>(context, view) {
    init {
        mModel = MonographModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getMonographArticles(series: String?, page: Int, page_size: Int) {
        if (page == 1) mView.showLoadingDialog()

        mModel?.getMonographArticles(series, page, page_size)?.subscribe(object : DisposableObserver<ResultInfo<List<ArticleDetailInfo>>>() {


            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(t: ResultInfo<List<ArticleDetailInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null) {
                            val data = t.data
                            mView.showMonagraphInfos(data)
                        } else {
                            mView.onEnd()
                        }

                    }
                }
            }
        })


    }
}