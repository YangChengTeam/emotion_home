package com.yc.emotion.home.index.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.LoveUpDownModel
import com.yc.emotion.home.index.view.LoveUpDownView
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveHealingBean
import rx.Subscriber

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
        val subscription = mModel?.recommendLovewords(userId, page, page_size, url)?.subscribe(object : Subscriber<AResultInfo<List<LoveHealingBean>>>() {
            override fun onNext(t: AResultInfo<List<LoveHealingBean>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showRecommendWords(data)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }
        })
        subScriptions?.add(subscription)
    }


    fun collectLovewords(userId: String?, lovewordsId: String?, url: String?) {
        mView.showLoadingDialog()
        val subscription = mModel?.collectLovewords(userId, lovewordsId, url)?.subscribe(object : Subscriber<AResultInfo<String>>() {
            override fun onNext(t: AResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        mView.showCollectSuccess(t.msg)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {
            }
        })

        subScriptions?.add(subscription)
    }

}