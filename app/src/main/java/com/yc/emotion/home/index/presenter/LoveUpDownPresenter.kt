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

    fun recommendLovewords(userId: String?, page: String?, page_size: String?, url: String?, isRefresh: Boolean) {

        mModel?.recommendLovewords(userId, page, page_size, url)?.getData(mView, { it, _ ->
            it?.let {
                mView.showRecommendWords(it)
            }
        }, { _, _ -> }, page?.toInt() == 1 && !isRefresh)

    }


    fun collectLovewords(userId: String?, lovewordsId: String?, url: String?) {

        mModel?.collectLovewords(userId, lovewordsId, url)?.getData(mView, { it, msg ->
            mView.showCollectSuccess(msg)
        }, { _, _ -> })

    }

}