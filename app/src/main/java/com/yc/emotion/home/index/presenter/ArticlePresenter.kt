package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.ArticleModel
import com.yc.emotion.home.index.view.ArticleView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.AticleTagInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/8 11:21.
 */
class ArticlePresenter(context: Context?, view: ArticleView) : BasePresenter<ArticleModel, ArticleView>(context, view) {


    init {
        mModel = ArticleModel(context)
    }


    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return

    }


    override fun isLoadingCache(): Boolean {
        return false
    }

    override fun getCache() {
        CommonInfoHelper.getO(mContext, "more_article_infos", object : TypeReference<List<AticleTagInfo>>() {}.type,
                object : CommonInfoHelper.OnParseListener<List<AticleTagInfo>> {
                    override fun onParse(o: List<AticleTagInfo>?) {
                        if (o != null && o.isNotEmpty()) {
                            mView.showArticleCategory(o)
                        }
                    }
                })

    }


    /**
     * 更多文章分类类别
     */
    fun getArticleTagInfos() {
        mModel?.getArticleTagInfos()?.getData(mView, { it, _ ->
            it?.let {
                mView.showArticleCategory(it)
                CommonInfoHelper.setO(mContext, it, "more_article_infos")
            }
        }, { _, _ -> getCache() }, false)

    }


    fun getArticleInfoList(cat_id: Int?, page: Int, page_size: Int) {

        mModel?.getArticleInfoList(cat_id, 0, page, page_size)?.getData(mView, { it, msg ->
            if (it != null) {
                mView.showArticleInfoList(it)
            } else {
                mView.onEnd()
            }
        }, { _, _ -> mView.onComplete() }, page == 1)
    }


}