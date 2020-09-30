package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.CollectModel
import com.yc.emotion.home.mine.view.CollectView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig
import yc.com.rthttplibrary.util.ToastUtil

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

        mModel?.getCourseCollectList("$userId", page, page_size)?.subscribe(object : DisposableObserver<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<List<CourseInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            mView.showCollectCourseList(t.data)

                        } else {
                            if (page == 1) mView.onNoData()
                        }
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun getCollectLoveHeals(limit: Int, offset: Int) {
        mView.showLoadingDialog()

        mModel?.getVerbalList(limit, offset)?.subscribe(object : DisposableObserver<ResultInfo<List<LoveHealDetDetailsBean>>>() {


            override fun onComplete() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
                mView.onError()
            }

            override fun onNext(t: ResultInfo<List<LoveHealDetDetailsBean>>) {
                mView.hideLoadingDialog()
                if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.isNotEmpty()) {
                    mView.showCollectVerbalList(t.data)
                } else {
                    if (limit == 1) {
                        mView.onNoData()
                    }
                }
            }

        })

//        handler?.postDelayed({
//            mModel?.getCollectLoveHeals(limit, offset)?.subscribe(object : DisposableObserver<List<LoveHealDetDetailsBean>>() {
//                override fun onNext(t: List<LoveHealDetDetailsBean>) {
//                    t.let {
//                        mView.showCollectVerbalList(t)
//                    }
//                }
//
//                override fun onComplete() {
//                    mView.hideLoadingDialog()
//                    mView.onComplete()
//                }
//
//                override fun onError(e: Throwable) {
//                    mView.onError()
//                }
//
//            })
//
//        }, 1500)
    }

    fun getArticleCollectList(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()

        mModel?.getArticleCollectList("$userId", page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<List<ArticleDetailInfo>>>() {
            override fun onNext(t: ResultInfo<List<ArticleDetailInfo>>) {
                t.let {
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

            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun deleteCollectLoveHeals(detDetailsBean: LoveHealDetDetailsBean?) {
        mModel?.deleteCollectLoveHeals(detDetailsBean)?.subscribe(object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                if (TextUtils.equals("success", t)) {
                    ToastUtil.toast(mContext, "已取消收藏！")
                }

                mView.showDeleteSuccess()
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun collectLoveHeal(detDetailsBean: LoveHealDetDetailsBean?) {
        mModel?.collectLoveHeal(detDetailsBean)?.subscribe(object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                if (TextUtils.equals("success", t)) {
                    ToastUtil.toast(mContext, "已经收藏成功，快去查看吧！")
                }
                mView.showCollectSuccess()
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

        })

    }
}