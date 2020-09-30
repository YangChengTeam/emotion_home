package com.yc.emotion.home.mine.domain.model

import android.content.Context
import android.text.TextUtils
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo
import java.util.*

/**
 *
 * Created by suns  on 2019/11/19 15:13.
 */
class UserInfoModel(override var context: Context?) : IModel(context) {

    /**
     * 手机号登录
     *
     * @param context
     * @param mobile  手机号
     * @param pwd     密码
     * @param code    验证码
     * @return
     */
    fun phoneLogin(mobile: String?, pwd: String?, code: String?): Observable<ResultInfo<UserInfo>> {

        val params = HashMap<String, String?>()
        params["mobile"] = mobile
        if (!TextUtils.isEmpty(pwd)) {
            params["password"] = pwd
        }
        if (!TextUtils.isEmpty(code))
            params["code"] = code


        return request.phoneLogin(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * 发送验证码
     *
     * @param
     * @param mobile 手机号
     * @return
     */
    fun sendCode(mobile: String?): Observable<ResultInfo<String>> {


        return request.sendCode(mobile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 注册
     *
     * @param context
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     * @return
     */
    fun phoneRegister(mobile: String?, password: String?, code: String?, relation_code: String?): Observable<ResultInfo<UserInfo>> {
        val params = HashMap<String, String?>()
        params["mobile"] = mobile
        if (!TextUtils.isEmpty(password))
            params["password"] = password
        params["code"] = code
        if (!TextUtils.isEmpty(relation_code))
            params["relation_code"] = relation_code


        return request.phoneRegister(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }


    /**
     * 重置密码
     *
     * @param mobile
     * @param code
     * @param pwd
     * @return
     */
    fun resetPwd(mobile: String?, code: String?, pwd: String?): Observable<ResultInfo<String>> {

        return request.resetPwd(mobile, code, pwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 预约咨询
     *
     * @param phone
     * @param wx
     * @param code
     * @return
     */
    fun consultAppoint(phone: String?, wx: String?, code: String?): Observable<ResultInfo<String>> {

        val params = HashMap<String, String?>()
        params["phone"] = phone
        if (!TextUtils.isEmpty(wx)) params["wx"] = wx
        params["code"] = code

        return request.consultAppoint(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 第三方登录
     *
     * @param access_token 登录token
     * @param account_type 登录类型 1 qq 2 微信
     * @param face         图像
     * @param nick_name    昵称
     * @return
     */

    fun thridLogin(access_token: String?, account_type: Int, face: String?, sex: String?, nick_name: String?): Observable<ResultInfo<UserInfo>> {


        return request.thridLogin(access_token, account_type, face, sex, nick_name).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }


    /**
     * 获取用户资料
     *
     * @param userId
     * @return
     */
    fun userInfo(userId: String): Observable<ResultInfo<UserInfo>> {


        return request.userInfo(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun setPwd(pwd: String?): Observable<ResultInfo<UserInfo>> {


        return request.setPwd("${UserInfoHelper.instance.getUid()}", pwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    fun modifyPwd(pwd: String?, new_pwd: String?): Observable<ResultInfo<UserInfo>> {

        return request.modifyPwd("${UserInfoHelper.instance.getUid()}", pwd, new_pwd).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getRewardInfo(): Observable<ResultInfo<RewardInfo>>? {
        return request.getRewardInfo("${UserInfoHelper.instance.getUid()}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}