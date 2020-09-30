package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.model.MineModel
import com.yc.emotion.home.mine.view.MineView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/15 15:04.
 */
class MinePresenter(context: Context, view: MineView) : BasePresenter<MineModel, MineView>(context, view) {

    init {
        mModel = MineModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun userInfo() {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        mModel?.userInfo("$userId")?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showUserInfo(t.data)
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


    fun updateUserInfo(nickName: String?, face: String?, password: String, birthday: String, sex: Int, profession: String?, age: String?, signature: String?, interested_id: String?) {

        if (TextUtils.isEmpty(nickName) && (sex == 0) && TextUtils.isEmpty(profession) && TextUtils.isEmpty(age) && TextUtils.isEmpty(signature) && TextUtils.isEmpty(interested_id)) {
            ToastUtils.showCenterToast("请填写用户资料")
            return
        }


        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()

        mModel?.updateUserInfo("$userId", nickName, face, password, birthday, sex, profession, age, signature, interested_id)
                ?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
                    override fun onNext(t: ResultInfo<UserInfo>) {
                        t.let {
                            if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                                mView.showUpdateUserInfo(t.data)
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


    fun getUserInterseInfo() {
        mModel?.getUserInterseInfo()?.subscribe(object : DisposableObserver<ResultInfo<List<UserInterInfo>>>() {
            override fun onNext(t: ResultInfo<List<UserInterInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showUserInterseInfo(t.data)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
            }

        })

    }


    fun addSuggestion(content: String?, qq: String?, wechat: String?) {


        if (!UserInfoHelper.instance.goToLogin(mContext)) {

            val userId = UserInfoHelper.instance.getUid()

            mView.showLoadingDialog()

            mModel?.addSuggestion("$userId", content, qq, wechat, "user.suggestion/add")?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
                override fun onNext(t: ResultInfo<String>) {
                    t.let {
                        if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                            val message = t.message
                            ToastUtils.showCenterToast(message)
                            mView.showSuggestionSuccess()
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

    }

}