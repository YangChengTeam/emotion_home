package com.yc.emotion.home.mine.domain.model

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.WetChatInfo
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/19 15:13.
 */
class UserInfoModel(override var context: Context?) : IModel {

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
        return HttpCoreEngin.get(context).rxpost(URLConfig.LOGIN_URL, object : TypeReference<ResultInfo<UserInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<UserInfo>>

    }

    /**
     * 发送验证码
     *
     * @param
     * @param mobile 手机号
     * @return
     */
    fun sendCode(mobile: String?): Observable<ResultInfo<String>> {
        val params = HashMap<String, String?>()
        params["mobile"] = mobile

        return HttpCoreEngin.get(context).rxpost(URLConfig.SEND_CODE_URL, object : TypeReference<ResultInfo<String>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<String>>
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
    fun phoneRegister(mobile: String?, password: String?, code: String?): Observable<ResultInfo<UserInfo>> {
        val params = HashMap<String, String?>()
        params["mobile"] = mobile
        if (!TextUtils.isEmpty(password))
            params["password"] = password
        params["code"] = code

        return HttpCoreEngin.get(context).rxpost(URLConfig.REGISTER_URL, object : TypeReference<ResultInfo<UserInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<UserInfo>>


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
        val params = HashMap<String, String?>()
        params["mobile"] = mobile
        params["code"] = code
        params["new_password"] = pwd
        return HttpCoreEngin.get(context).rxpost(URLConfig.RESET_PWD_URL, object : TypeReference<ResultInfo<String>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<String>>
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

        return HttpCoreEngin.get(context).rxpost(URLConfig.CONSULT_TUTOR_URL, object : TypeReference<ResultInfo<String>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<String>>
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

        val params = HashMap<String, String?>()
        params["token"] = access_token
        params["sns"] = account_type.toString() + ""
        params["face"] = face
        params["sex"] = sex
        params["nick_name"] = nick_name


        return HttpCoreEngin.get(context).rxpost(URLConfig.THRID_LOGIN_URL, object : TypeReference<ResultInfo<UserInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<UserInfo>>

    }


    /**
     * 获取用户资料
     *
     * @param userId
     * @return
     */
    fun userInfo(userId: String): Observable<ResultInfo<UserInfo>> {
        val params = HashMap<String, String>()
        params["user_id"] = userId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.USER_INFO_URL, object : TypeReference<ResultInfo<UserInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<UserInfo>>
    }


    fun setPwd(pwd: String?): Observable<ResultInfo<UserInfo>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.SET_PWD_URL, object : TypeReference<ResultInfo<UserInfo>>() {}.type, mutableMapOf(
                "user_id" to "${UserInfoHelper.instance.getUid()}",
                "password" to pwd
        ), true, true, true) as Observable<ResultInfo<UserInfo>>
    }


    fun modifyPwd(pwd: String?, new_pwd: String?): Observable<ResultInfo<UserInfo>> {
        //user_id=52192&password=123456&new_password=123456
        return HttpCoreEngin.get(context).rxpost(URLConfig.MODIFY_PWD_URL, object : TypeReference<ResultInfo<UserInfo>>() {}.type, mutableMapOf(
                "user_id" to "${UserInfoHelper.instance.getUid()}",
                "password" to pwd,
                "new_password" to new_pwd
        ), true, true, true) as Observable<ResultInfo<UserInfo>>
    }

}