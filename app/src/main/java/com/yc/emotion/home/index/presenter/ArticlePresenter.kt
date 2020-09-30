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
        mModel?.getArticleTagInfos()?.subscribe(object : DisposableObserver<ResultInfo<List<AticleTagInfo>>>() {
            override fun onNext(t: ResultInfo<List<AticleTagInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showArticleCategory(t.data)
                        CommonInfoHelper.setO(mContext, t.data, "more_article_infos")
                    } else {
                        getCache()
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                getCache()
            }

        })
    }


    fun getArticleInfoList(cat_id: Int?, page: Int, page_size: Int) {
        if (page == 1)
            mView.showLoadingDialog()
        mModel?.getArticleInfoList(cat_id, 0, page, page_size)?.subscribe(object : DisposableObserver<ResultInfo<List<ArticleDetailInfo>>>() {
            override fun onNext(t: ResultInfo<List<ArticleDetailInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null) {
                            mView.showArticleInfoList(t.data)

                        } else {
                            mView.onEnd()
                        }
                    }
                }

            }

            override fun onComplete() {
                if (page == 1)
                    mView.hideLoadingDialog()
                mView.onComplete()


            }

            override fun onError(e: Throwable) {
            }

        })
    }


}