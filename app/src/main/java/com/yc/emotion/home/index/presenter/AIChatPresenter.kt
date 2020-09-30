package com.yc.emotion.home.index.presenter

import android.content.Context

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.bean.SmartChatItem
import com.yc.emotion.home.index.domain.model.AIChatModel
import com.yc.emotion.home.index.view.AIChatView
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.utils.ToastUtils
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2020/8/11 18:31.
 */
class AIChatPresenter(context: Context, view: AIChatView) : BasePresenter<AIChatModel, AIChatView>(context, view) {
    init {
        mModel = AIChatModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getAIChatContent(key: String) {
        mModel?.getAIChatContent(key)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                when (t.code) {
                    HttpConfig.STATUS_OK -> {
                        mView.showAIContent(t.data)
                    }
                    2 -> {
                        mView.showUseCountUp(t.message)
                        ToastUtils.showCenterToast(t.message)
                    }
                    else -> {
                        ToastUtils.showCenterToast(t.message)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                ToastUtils.showCenterToast(HttpConfig.NET_ERROR)
            }
        })

    }

    fun smartSearchVerbal(keyword: String?, section: Int, refresh: Boolean) {
        mModel?.smartSearchVerbal(keyword, section)?.subscribe(object : DisposableObserver<ResultInfo<SmartChatItem>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<SmartChatItem>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        createNewData(t.data, refresh)
                    } else {
                        if (t.code == 2) {
                            mView.showUseCountUp(t.message)
                            ToastUtils.showCenterToast(t.message)
                        } else {
                            ToastUtils.showCenterToast(t.message)
                        }
                    }
                }
            }

            override fun onError(e: Throwable) {
                ToastUtils.showCenterToast(HttpConfig.NET_ERROR)
            }
        })
    }

    fun aiPraise(id: String?) {
        mModel?.aiPraise(id)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<String>) {
                if (t.code == HttpConfig.STATUS_OK) {
//                    ToastUtils.showCenterToast(t.message)
                }
            }

            override fun onError(e: Throwable) {

            }
        })
    }

    fun aiCollect(id: String?) {
        mModel?.aiCollect(id)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<String>) {
                if (t.code == HttpConfig.STATUS_OK) {
//                    ToastUtils.showCenterToast(t.message)

                }
            }

            override fun onError(e: Throwable) {

            }
        })
    }

    private fun createNewData(data: SmartChatItem?, refresh: Boolean) {
        data?.let {
            val loveHealDetBeans = data.loveHealDetBeans
            for (loveHealDetBean in loveHealDetBeans) {
                var details: MutableList<LoveHealDetDetailsBean>? = loveHealDetBean.details
                if (details == null || details.isEmpty()) {
                    details = loveHealDetBean.detail
                }
                val loveHealDetDetailsBean = LoveHealDetDetailsBean()

                loveHealDetDetailsBean.content = loveHealDetBean.chat_name
                loveHealDetDetailsBean.ans_sex = loveHealDetBean.quiz_sex
                loveHealDetDetailsBean.dialogue_id = loveHealDetBean.id
                details?.add(0, loveHealDetDetailsBean)
            }
            mView.showSmartAiInfos(data, refresh)
        }
    }

}