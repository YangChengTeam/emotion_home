package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils


import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.mine.domain.model.LiveModel
import com.yc.emotion.home.mine.view.LiveView
import com.yc.emotion.home.utils.ToastUtils
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.config.HttpConfig


/**
 * Created by suns  on 2020/6/5 09:34.
 */
class LivePresenter(context: Context?, view: LiveView) : BasePresenter<LiveModel, LiveView>(context, view) {

    init {
        mModel = LiveModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {}
    override fun getCache() {}

    fun liveLogin(username: String?, password: String?) {
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showCenterToast("账号不能为空")
            return
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showCenterToast("密码不能为空")
            return
        }
        mView.showLoadingDialog()
        mModel?.liveLogin(username, password)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showLoginSuccess(data)
                    } else {
                        ToastUtils.showCenterToast(t.message)
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


    fun createRoom(userId: String?) {
        mModel?.createRoom(userId)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<LiveInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showCreateRoomSuccess(data)
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

    fun liveEnd(roomId: String?) {
        mModel?.liveEnd(roomId)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<String>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<String>) {

            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })

    }

    fun dismissGroup(group_id: String) {
        mModel?.dismissGroup(group_id)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<String>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<String>) {

            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

        })

    }
}