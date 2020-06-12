package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.music.player.lib.util.ToastUtils
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.mine.domain.model.LiveModel
import com.yc.emotion.home.mine.view.LiveView
import rx.Subscriber

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
        val subscription = mModel?.liveLogin(username, password)?.subscribe(object : Subscriber<ResultInfo<LiveInfo>>() {
            override fun onNext(t: ResultInfo<LiveInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showLoginSuccess(data)
                    } else {
                        ToastUtils.showCenterToast("您没有直播权限，请联系管理员！")
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }
        })

        subScriptions?.add(subscription)
    }


    fun createRoom(userId: String?) {
        val subscription = mModel?.createRoom(userId)?.subscribe(object : Subscriber<ResultInfo<LiveInfo>>() {
            override fun onNext(t: ResultInfo<LiveInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.showCreateRoomSuccess(data)
                    } else {
                        ToastUtils.showCenterToast(t.message)
                    }
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }
        })
        subScriptions?.add(subscription)
    }

    fun liveEnd(roomId: String?) {
        val subscription = mModel?.liveEnd(roomId)?.subscribe(object : Subscriber<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>?) {

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }
        })
        subScriptions?.add(subscription)
    }

    fun dismissGroup(group_id: String) {
        val subscription = mModel?.dismissGroup(group_id)?.subscribe(object : Subscriber<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>?) {

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }
}