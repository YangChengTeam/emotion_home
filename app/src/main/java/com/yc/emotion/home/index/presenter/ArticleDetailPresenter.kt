package com.yc.emotion.home.index.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.ArticleDetailModel
import com.yc.emotion.home.index.view.ArticleDetailView
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/12 11:01.
 */
class ArticleDetailPresenter(context: Context, view: ArticleDetailView) : BasePresenter<ArticleDetailModel, ArticleDetailView>(context, view) {

    init {
        mModel = ArticleDetailModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return
    }

    override fun getCache() {
    }

    fun getArticleDetai(id: String) {

        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        mModel?.getArticleDetai(id, "$userId")?.subscribe(object : Subscriber<AResultInfo<LoveByStagesDetailsBean>>() {
            override fun onNext(t: AResultInfo<LoveByStagesDetailsBean>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showArticleDetailInfo(t.data)
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
     * 文章收藏
     *
     *
     */
    fun articleCollect(exampleId: String, isCollected: Boolean) {

        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        mModel?.articleCollect("$userId", exampleId, "article.article/collect")?.subscribe(object : Subscriber<AResultInfo<String>>() {
            override fun onNext(t: AResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.msg
                        ToastUtils.showCenterToast(msg)
                        var isCollected = isCollected
                        isCollected = !isCollected
                        mView.collectArticle(-1, isCollected)

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
     *
     * 点赞或者取消点赞
     *
     */
    fun articleDig(exampleId: String, isDigArticle: Boolean) {

        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        mModel?.articleCollect("$userId", exampleId, "article.article/dig")?.subscribe(object : Subscriber<AResultInfo<String>>() {
            override fun onNext(t: AResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.msg
                        ToastUtils.showCenterToast(msg)
                        var isDigArticle = isDigArticle
                        isDigArticle = !isDigArticle

                        mView.digArticle(isDigArticle)
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