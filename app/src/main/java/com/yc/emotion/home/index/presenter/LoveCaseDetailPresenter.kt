package com.yc.emotion.home.index.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.LoveCaseDetailModel
import com.yc.emotion.home.index.view.LoveCaseDetailView
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

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

        mView.showLoadingDialog()
        val subscription = mModel?.detailLoveCase(id, "$userId")?.subscribe(object : Subscriber<AResultInfo<LoveByStagesDetailsBean>>() {
            override fun onNext(t: AResultInfo<LoveByStagesDetailsBean>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showLoveCaseDetail(t.data)
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

    fun collectLoveCase(exampleId: String, isCollected: Boolean) {
        val userId = UserInfoHelper.instance.getUid()

        val url = if (isCollected) {
            "article.example/uncollect"
        } else {
            "article.example/collect"
        }

        mView.showLoadingDialog()

        mModel?.collectLoveCase("$userId", exampleId, url)?.subscribe(object : Subscriber<AResultInfo<String>>() {
            override fun onNext(t: AResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.msg
                        ToastUtils.showCenterToast(msg)

                        var isCollected = isCollected
                        isCollected = !isCollected
                        mView.showLoveCaseCollectResult(-1, isCollected)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
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

        mView.showLoadingDialog()

        mModel?.collectLoveCase("$userId", exampleId, url)?.subscribe(object : Subscriber<AResultInfo<String>>() {
            override fun onNext(t: AResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.msg
                        ToastUtils.showCenterToast(msg)

                        var isDig = isDig
                        isDig = !isDig
                        mView.showLoveCaseDigResult(isDig)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
    }
}