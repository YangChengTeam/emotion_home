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

        mModel?.getCourseCollectList("$userId", page, page_size)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showCollectCourseList(it)

            } else {
                if (page == 1) mView.onNoData()
            }
        }, { _, _ -> mView.onComplete() }, page == 1)


    }


    fun getCollectLoveHeals(limit: Int, offset: Int) {

        mModel?.getVerbalList(limit, offset)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showCollectVerbalList(it)
            } else {
                if (limit == 1) {
                    mView.onNoData()
                }
            }
        }, { _, _ -> mView.onError() })


    }

    fun getArticleCollectList(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.getArticleCollectList("$userId", page, pageSize)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showCollectArticleList(it)
            } else {
                if (page == 1) {
                    mView.onNoData()
                }
            }
        }, { _, _ -> mView.onComplete() }, page == 1)


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