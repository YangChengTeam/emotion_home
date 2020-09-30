package com.yc.emotion.home.index.presenter

import android.content.Context
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.LoveUpDownModel
import com.yc.emotion.home.index.view.LoveUpDownView
import com.yc.emotion.home.model.bean.LoveHealingBean
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2020/4/28 15:59.
 */
class LoveUpDownPresenter(context: Context?, view: LoveUpDownView) : BasePresenter<LoveUpDownModel, LoveUpDownView>(context, view) {

    init {
        mModel = LoveUpDownModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun recommendLovewords(userId: String?, page: String?, page_size: String?, url: String?) {
        mView.showLoadingDialog()
        mModel?.recommendLovewords(userId, page, page_size, url)?.subscribe(object : DisposableObserver<ResultInfo<List<LoveHealingBean>>>() {
            override fun onNext(t: ResultInfo<List<LoveHealingBean>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showRecommendWords(data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }
        })

    }


    fun collectLovewords(userId: String?, lovewordsId: String?, url: String?) {
        mView.showLoadingDialog()
        mModel?.collectLovewords(userId, lovewordsId, url)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        mView.showCollectSuccess(t.message)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {
            }
        })


    }

}