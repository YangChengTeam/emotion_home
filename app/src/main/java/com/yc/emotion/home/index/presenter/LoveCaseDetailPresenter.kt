package com.yc.emotion.home.index.presenter

import android.content.Context

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.LoveCaseDetailModel
import com.yc.emotion.home.index.view.LoveCaseDetailView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/14 10:50.
 */
class LoveCaseDetailPresenter(context: Context, view: LoveCaseDetailView) : BasePresenter<LoveCaseDetailModel, LoveCaseDetailView>(context, view) {


    init {
        mModel = LoveCaseDetailModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun detailLoveCase(id: String) {

        val userId = UserInfoHelper.instance.getUid()

        mModel?.detailLoveCase(id, "$userId")?.getData(mView, { it, _ ->
            it?.let {
                mView.showLoveCaseDetail(it)
            }
        }, { _, _ -> })


    }

    fun collectLoveCase(exampleId: String, isCollected: Boolean) {
        val userId = UserInfoHelper.instance.getUid()

        val url = if (isCollected) {
            "article.example/uncollect"
        } else {
            "article.example/collect"
        }

        mModel?.collectLoveCase("$userId", exampleId, url)?.getData(mView, { it, msg ->
            it?.let {

                ToastUtils.showCenterToast(msg)

                var isCollected = isCollected
                isCollected = !isCollected
                mView.showLoveCaseCollectResult(-1, isCollected)
            }
        }, { _, _ -> })
    }


    /**
     * 点赞案例
     */
    fun digLoveCase(exampleId: String, isDig: Boolean) {
        val userId = UserInfoHelper.instance.getUid()

        val url = if (isDig) {
            "article.example/undig"
        } else {
            "article.example/dig"
        }

        mModel?.collectLoveCase("$userId", exampleId, url)?.getData(mView, { it, msg ->
            it?.let {
                ToastUtils.showCenterToast(msg)

                var isDig = isDig
                isDig = !isDig
                mView.showLoveCaseDigResult(isDig)
            }
        }, { _, _ -> })
    }
}