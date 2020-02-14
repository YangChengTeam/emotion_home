package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.utils.ToastUtil
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.CollectModel
import com.yc.emotion.home.mine.view.CollectView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/15 16:08.
 */
class CollectPresenter(context: Context?, view: CollectView) : BasePresenter<CollectModel, CollectView>(context, view) {
    private var handler: Handler? = null

    init {
        mModel = CollectModel(context)
        handler = Handler(Looper.getMainLooper())
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {
    }


    fun getCourseCollectList(page: Int, page_size: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()

        val subscription = mModel?.getCourseCollectList("$userId", page, page_size)?.subscribe(object : Subscriber<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            mView.showCollectCourseList(t.data)

                        } else {
                            if (page == 1) mView.onNoData()
                        }
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun getCollectLoveHeals(limit: Int, offset: Int) {
        mView.showLoadingDialog()
        handler?.postDelayed({
            val subscription = mModel?.getCollectLoveHeals(limit, offset)?.subscribe(object : Subscriber<List<LoveHealDetDetailsBean>>() {
                override fun onNext(t: List<LoveHealDetDetailsBean>?) {
                    t?.let {
                        mView.showCollectVerbalList(t)
                    }
                }

                override fun onCompleted() {
                    mView.hideLoadingDialog()
                    mView.onComplete()
                }

                override fun onError(e: Throwable?) {
                    mView.onError()
                }

            })
            subScriptions?.add(subscription)
        }, 1500)
    }

    fun getArticleCollectList(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()

        val subscription = mModel?.getArticleCollectList("$userId", page, pageSize)?.subscribe(object : Subscriber<ResultInfo<List<ArticleDetailInfo>>>() {
            override fun onNext(t: ResultInfo<List<ArticleDetailInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        val mExampListsBeans = t.data
                        if (mExampListsBeans != null && mExampListsBeans.isNotEmpty()) {
                            mView.showCollectArticleList(mExampListsBeans)
                        } else {
                            if (page == 1) {
                                mView.onNoData()
                            }
                        }
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun deleteCollectLoveHeals(detDetailsBean: LoveHealDetDetailsBean?) {
        val subscription = mModel?.deleteCollectLoveHeals(detDetailsBean)?.subscribe(object : Subscriber<String>() {
            override fun onNext(t: String?) {
                if (TextUtils.equals("success", t)) {
                    ToastUtil.toast2(mContext, "已取消收藏！")
                }

                mView.showDeleteSuccess()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun collectLoveHeal(detDetailsBean: LoveHealDetDetailsBean?) {
        val subscription = mModel?.collectLoveHeal(detDetailsBean)?.subscribe(object : Subscriber<String>() {
            override fun onNext(t: String?) {
                if (TextUtils.equals("success", t)) {
                    ToastUtil.toast2(mContext, "已经收藏成功，快去查看吧！")
                }
                mView.showCollectSuccess()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }
}