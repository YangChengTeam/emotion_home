package com.yc.emotion.home.index.presenter


import android.content.Context
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.ArticleDetailModel
import com.yc.emotion.home.index.view.ArticleDetailView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

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
        mModel?.getArticleDetai(id, "$userId")?.subscribe(object : DisposableObserver<ResultInfo<LoveByStagesDetailsBean>>() {
            override fun onNext(t: ResultInfo<LoveByStagesDetailsBean>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showArticleDetailInfo(t.data)
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


    /**
     * 文章收藏
     *
     *
     */
    fun articleCollect(exampleId: String, isCollected: Boolean) {

        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        mModel?.articleCollect("$userId", exampleId, "article.article/collect")?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.message
                        ToastUtils.showCenterToast(msg)
                        var isCollected = isCollected
                        isCollected = !isCollected
                        mView.collectArticle(-1, isCollected)

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


    /**
     *
     * 点赞或者取消点赞
     *
     */
    fun articleDig(exampleId: String, isDigArticle: Boolean) {

        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        mModel?.articleCollect("$userId", exampleId, "article.article/dig")?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val msg = t.message
                        ToastUtils.showCenterToast(msg)
                        var isDigArticle = isDigArticle
                        isDigArticle = !isDigArticle

                        mView.digArticle(isDigArticle)
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