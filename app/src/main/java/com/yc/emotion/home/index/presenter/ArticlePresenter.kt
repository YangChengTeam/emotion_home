package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.ArticleModel
import com.yc.emotion.home.index.view.ArticleView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.AticleTagInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import kotlinx.android.synthetic.main.fragment_efficient_course.*
import rx.Subscriber

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
        getArticleTagInfos()

    }


    override fun isLoadingCache(): Boolean {
        return false
    }

    override fun getCache() {
        CommonInfoHelper.getO<List<AticleTagInfo>>(mContext, "more_article_infos", object : TypeReference<List<AticleTagInfo>>() {}.type) { infos ->
            infos?.let {
                if (infos.isNotEmpty()) {
                    mView.showArticleCategory(infos)
                }
            }
        }
    }


    /**
     * 更多文章分类类别
     */
    fun getArticleTagInfos() {
        mModel?.getArticleTagInfos()?.subscribe(object : Subscriber<ResultInfo<List<AticleTagInfo>>>() {
            override fun onNext(t: ResultInfo<List<AticleTagInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showArticleCategory(t.data)
                        CommonInfoHelper.setO(mContext, t.data, "more_article_infos")
                    } else {
                        getCache()
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                getCache()
            }

        })
    }


    fun getArticleInfoList(cat_id: Int,  page: Int, page_size: Int) {
        mView.showLoadingDialog()
        mModel?.getArticleInfoList(cat_id, 0, page, page_size)?.subscribe(object : Subscriber<ResultInfo<List<ArticleDetailInfo>>>() {
            override fun onNext(t: ResultInfo<List<ArticleDetailInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null) {
                            mView.showArticleInfoList(t.data)

                        } else {
                            mView.onEnd()
                        }
                    }
                }

            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
                mView.onComplete()


            }

            override fun onError(e: Throwable?) {
            }

        })
    }


}