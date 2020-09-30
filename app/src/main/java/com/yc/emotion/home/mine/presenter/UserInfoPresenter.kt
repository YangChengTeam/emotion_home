package com.yc.emotion.home.mine.presenter

import android.content.Context
import android.text.TextUtils

import com.yc.emotion.home.base.listener.OnUserInfoListener
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.domain.model.UserInfoModel
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.RegexUtils
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig


/**
 *
 * Created by suns  on 2019/11/19 15:13.
 */
class UserInfoPresenter(context: Context?, view: UserInfoView) : BasePresenter<UserInfoModel, UserInfoView>(context, view) {


    init {
        mModel = UserInfoModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun phoneLogin(mobile: String?, pwd: String?, code: String?) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast("手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            ToastUtils.showCenterToast("手机号格式不正确")
            return
        }
        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("密码或验证码不能为空")
            return
        }

        mView.showLoadingDialog()
        mModel?.phoneLogin(mobile, pwd, code)?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data
                        userInfo?.let {
                            if (!TextUtils.isEmpty(pwd)) it.pwd = pwd
                        }

                        UserInfoHelper.instance.saveUserInfo(userInfo)

                        mView.showPhoneLoginSuccess(userInfo)

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


    fun sendCode(mobile: String?) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast("手机号不能为空")
            return
        }

        if (!RegexUtils.isMobileExact(mobile)) {
            ToastUtils.showCenterToast("手机号格式不正确")
            return
        }

        mModel?.sendCode(mobile)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        mView.sendCodeSuccess()
                    }
                    ToastUtils.showCenterToast(t.message)

                }
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
            }

        })

    }


    fun phoneRegister(mobile: String?, password: String?, code: String?, relation_code: String?) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast("手机号不能为空")
            return
        }

        if (!RegexUtils.isMobileExact(mobile)) {
            ToastUtils.showCenterToast("手机号格式不正确")
            return
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("验证码不能为空")
            return
        }

        mView.showLoadingDialog()
        mModel?.phoneRegister(mobile, password, code, relation_code)?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data
                        userInfo?.let {
                            if (!TextUtils.isEmpty(password)) it.pwd = password
                        }
                        UserInfoHelper.instance.saveUserInfo(userInfo)
                        mView.showPhoneRegisterSuccess(userInfo)
                    } else {
                        val msg = t.message
                        if (!TextUtils.isEmpty(msg) && msg.contains("已经注册")) {
                            phoneLogin(mobile, "", code)
                            //
                        } else {
                            ToastUtils.showCenterToast(t.message)
                        }

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


    fun resetPwd(mobile: String?, code: String?, pwd: String?) {
        mView.showLoadingDialog()
        mModel?.resetPwd(mobile, code, pwd)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        ToastUtils.showCenterToast("重置密码成功")
                        mView.showResetPwdSuccess()
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


    fun consultAppoint(phone: String?, wx: String?, code: String?) {
        mView.showLoadingDialog()
        mModel?.consultAppoint(phone, wx, code)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        ToastUtils.showCenterToast("预约成功,请保持电话畅通！")
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

    fun thirdLogin(access_token: String?, account_type: Int, face: String?, sex: String?, nick_name: String?, finish: Boolean?) {
        mModel?.thridLogin(access_token, account_type, face, sex, nick_name)?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                    UserInfoHelper.instance.saveUserInfo(t.data)
                    mView.thirdLoginSuccess(t.data, finish)
                } else {
                    ToastUtils.showCenterToast(t.message)
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                ToastUtils.showCenterToast(HttpConfig.NET_ERROR)
            }

        })


    }

    fun userInfo(listener: OnUserInfoListener?) {
        val userId = UserInfoHelper.instance.getUid()

        if (userId <= 0) {
            return
        }
        mModel?.userInfo("$userId")?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data
                        val info = UserInfoHelper.instance.getUserInfo()
                        userInfo?.let {
                            info?.let {
                                if (!TextUtils.isEmpty(info.pwd)) userInfo.pwd = info.pwd
                            }
                        }
                        UserInfoHelper.instance.saveUserInfo(userInfo)
                        mView.getUserInfoSuccess(userInfo, listener)
                    }
                }
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {

            }

        })

    }


    fun setPwd(pwd: String?) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空")
            return
        }
        mView.showLoadingDialog()
        val subscription = mModel?.setPwd(pwd)?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {

                        val info = UserInfoHelper.instance.getUserInfo()
                        info?.pwd = pwd

                        UserInfoHelper.instance.saveUserInfo(info)
                        mView.setPwdSuccess()
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

    fun modifyPwd(pwd: String?, new_pwd: String?) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空")
            return
        }

        if (TextUtils.isEmpty(new_pwd)) {
            ToastUtils.showCenterToast("新密码不能为空")
        }

        mView.showLoadingDialog()

        mModel?.modifyPwd(pwd, new_pwd)?.subscribe(object : DisposableObserver<ResultInfo<UserInfo>>() {
            override fun onNext(t: ResultInfo<UserInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        val userInfo = UserInfoHelper.instance.getUserInfo()
                        userInfo?.pwd = new_pwd
                        UserInfoHelper.instance.saveUserInfo(userInfo)
                        mView.setPwdSuccess()
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