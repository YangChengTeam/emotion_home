package com.yc.emotion.home.index.presenter


import android.content.Context
import android.util.Log
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.ArticleDetailModel
import com.yc.emotion.home.index.view.ArticleDetailView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig
import yc.com.rthttplibrary.util.LogUtil

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

        mModel?.getArticleDetai(id, "$userId")?.getData(mView, { it, _ ->
            it?.let {
                mView.showArticleDetailInfo(it)
            }

        }, { _, _ -> })

    }


    /**
     * 文章收藏
     *
     *
     */
    fun articleCollect(exampleId: String, isCollected: Boolean) {

        val userId = UserInfoHelper.instance.getUid()


        mModel?.articleCollect("$userId", exampleId, "article.article/collect")?.getData(mView, { it, msg ->
            it?.let {

                ToastUtils.showCenterToast(msg)
                var isCollected = isCollected
                isCollected = !isCollected
                mView.collectArticle(-1, isCollected)
            }
        }, { _, _ -> })
    }


    /**
     *
     * 点赞或者取消点赞
     *
     */
    fun articleDig(exampleId: String, isDigArticle: Boolean) {

        val userId = UserInfoHelper.instance.getUid()


        mModel?.articleCollect("$userId", exampleId, "article.article/dig")?.getData(mView, { it, msg ->
            it?.let {

                ToastUtils.showCenterToast(msg)
                var isDigArticle = isDigArticle
                isDigArticle = !isDigArticle

                mView.digArticle(isDigArticle)
            }
        }, { _, _ -> })
    }
}