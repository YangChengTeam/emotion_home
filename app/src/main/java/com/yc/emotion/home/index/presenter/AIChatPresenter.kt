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
        mModel?.getAIChatContent(key)?.getData(mView, { it, _ ->
            mView.showAIContent(it)
        }, { code, msg ->
            when (code) {
                2 -> {
                    msg?.let {
                        mView.showUseCountUp(msg)
                        ToastUtils.showCenterToast(msg)
                    }
                }
                -1 -> {
                    ToastUtils.showCenterToast(HttpConfig.NET_ERROR)
                }
                else -> {
                    ToastUtils.showCenterToast(msg)
                }
            }
        }, false)

    }

    fun smartSearchVerbal(keyword: String?, section: Int, refresh: Boolean) {
        mModel?.smartSearchVerbal(keyword, section)?.getData(mView, { it, _ ->
            it?.let {
                createNewData(it, refresh)
            }
        }, { code, msg ->
            when (code) {
                2 -> {
                    msg?.let {
                        mView.showUseCountUp(msg)
                    }
                    ToastUtils.showCenterToast(msg)
                }
                -1 -> {
                    ToastUtils.showCenterToast(HttpConfig.NET_ERROR)
                }
                else -> {
                    ToastUtils.showCenterToast(msg)
                }
            }
        }, false)

    }

    fun aiPraise(id: String?) {
        mModel?.aiPraise(id)?.getData(mView, { _, _ -> }, { _, _ -> }, false)

    }

    fun aiCollect(id: String?) {
        mModel?.aiCollect(id)?.getData(mView, { _, _ -> }, { _, _ -> }, false)
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