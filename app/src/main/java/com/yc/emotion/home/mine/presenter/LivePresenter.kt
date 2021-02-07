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

        mModel?.liveLogin(username, password)?.getData(mView, { it, msg ->
            if (it != null) {
                mView.showLoginSuccess(it)
            } else {
                ToastUtils.showCenterToast(msg)
            }
        }, { _, _ -> })


    }


    fun createRoom(userId: String?) {
        mModel?.createRoom(userId)?.getData(mView, { it, msg ->
            if (it != null) {
                mView.showCreateRoomSuccess(it)
            } else {
                ToastUtils.showCenterToast(msg)
            }
        }, { _, _ -> }, false)

    }

    fun liveEnd(roomId: String?) {
        mModel?.liveEnd(roomId)?.getData(mView, { it, _ -> }, { _, _ -> }, false)

    }

    fun dismissGroup(group_id: String) {
        mModel?.dismissGroup(group_id)?.getData(mView, { it, _ -> }, { _, _ -> }, false)

    }
}