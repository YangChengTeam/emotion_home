package com.yc.emotion.home.index.presenter

import android.content.Context

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.bean.UserSeg
import com.yc.emotion.home.index.domain.model.LiveLookModel
import com.yc.emotion.home.index.view.LiveLookView
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.utils.ToastUtils
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.config.HttpConfig


/**
 *
 * Created by suns  on 2020/6/9 13:38.
 */
class LiveLookPresenter(context: Context?, view: LiveLookView) : BasePresenter<LiveLookModel, LiveLookView>(context, view) {
    init {
        mModel = LiveLookModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }

    fun getLiveLookInfo(roomId: String?) {
        mModel?.getLiveLookInfo(roomId)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showLiveInfo(data)
                    } else {
                        ToastUtils.showCenterToast(t.message)
                    }
                }
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
            }

        })

    }

    fun getUserSeg(user_id: String?) {
        mModel?.getUserSeg(user_id)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<UserSeg>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<UserSeg>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showUserSeg(t.data.usersig)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })

    }
}