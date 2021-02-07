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

        mModel?.phoneLogin(mobile, pwd, code)?.getData(mView, { it, msg ->
            if (it != null) {

                if (!TextUtils.isEmpty(pwd)) it.pwd = pwd


                UserInfoHelper.instance.saveUserInfo(it)

                mView.showPhoneLoginSuccess(it)

            } else {
                ToastUtils.showCenterToast(msg)
            }
        }, { _, _ -> })


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

        mModel?.sendCode(mobile)?.getData(mView, { it, msg ->
            mView.sendCodeSuccess()
            ToastUtils.showCenterToast(msg)
        }, { _, msg -> ToastUtils.showCenterToast(msg) }, false)

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

        mModel?.phoneRegister(mobile, password, code, relation_code)?.getData(mView, { it, msg ->
            if (it != null) {
                if (!TextUtils.isEmpty(password)) it.pwd = password
                UserInfoHelper.instance.saveUserInfo(it)
                mView.showPhoneRegisterSuccess(it)
            } else {
                msg?.let {
                    if (!TextUtils.isEmpty(msg) && msg.contains("已经注册")) {
                        phoneLogin(mobile, "", code)
                    } else {
                        ToastUtils.showCenterToast(msg)
                    }
                }


            }
        }, { _, _ -> })

    }


    fun resetPwd(mobile: String?, code: String?, pwd: String?) {

        mModel?.resetPwd(mobile, code, pwd)?.getData(mView, { _, _ ->
            ToastUtils.showCenterToast("重置密码成功")
            mView.showResetPwdSuccess()
        }, { _, _ -> })


    }


    fun consultAppoint(phone: String?, wx: String?, code: String?) {

        mModel?.consultAppoint(phone, wx, code)?.getData(mView, { it, _ ->
            it?.let {
                ToastUtils.showCenterToast("预约成功,请保持电话畅通！")
            }
        }, { _, _ -> })

    }

    fun thirdLogin(access_token: String?, account_type: Int, face: String?, sex: String?, nick_name: String?, finish: Boolean?) {
        mModel?.thridLogin(access_token, account_type, face, sex, nick_name)?.getData(mView, { userInfo, s ->
            if (userInfo != null) {
                UserInfoHelper.instance.saveUserInfo(userInfo)
                mView.thirdLoginSuccess(userInfo, finish)
            } else {
                ToastUtils.showCenterToast(s)
            }
        }, { _, _ -> ToastUtils.showCenterToast(HttpConfig.NET_ERROR) }, false)


    }

    fun userInfo(listener: OnUserInfoListener?) {
        val userId = UserInfoHelper.instance.getUid()

        if (userId <= 0) {
            return
        }
        mModel?.userInfo("$userId")?.getData(mView, { it, _ ->
            it?.let { userInfo ->

                val info = UserInfoHelper.instance.getUserInfo()

                info?.let {
                    if (!TextUtils.isEmpty(info.pwd)) userInfo.pwd = info.pwd
                }

                UserInfoHelper.instance.saveUserInfo(userInfo)
                mView.getUserInfoSuccess(userInfo, listener)
            }
        }, { _, _ -> }, false)


    }


    fun setPwd(pwd: String?) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空")
            return
        }
        mModel?.setPwd(pwd)?.getData(mView, { _, _ ->
            val info = UserInfoHelper.instance.getUserInfo()
            info?.pwd = pwd

            UserInfoHelper.instance.saveUserInfo(info)
            mView.setPwdSuccess()
        }, { _, msg -> ToastUtils.showCenterToast(msg) })

    }

    fun modifyPwd(pwd: String?, new_pwd: String?) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空")
            return
        }

        if (TextUtils.isEmpty(new_pwd)) {
            ToastUtils.showCenterToast("新密码不能为空")
        }


        mModel?.modifyPwd(pwd, new_pwd)?.getData(mView, { _, _ ->
            val userInfo = UserInfoHelper.instance.getUserInfo()
            userInfo?.pwd = new_pwd
            UserInfoHelper.instance.saveUserInfo(userInfo)
            mView.setPwdSuccess()
        }, { _, _ -> })


    }


}