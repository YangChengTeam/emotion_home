package com.yc.emotion.home.utils

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.yc.emotion.home.base.YcApplication
import com.yc.emotion.home.mine.ui.activity.LoginMainActivity
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.constant.ConstantKey

/**
 *
 * Created by suns  on 2019/10/21 15:18.
 */
class UserInfoHelper private constructor() {

    private var userInfoStr by Preference(ConstantKey.USER, "")

    private var userInfo: UserInfo? = null

    private var phone by Preference(ConstantKey.PhHONE, "")

    companion object {

        val instance: UserInfoHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserInfoHelper()
        }
    }


    fun getUserInfo(): UserInfo? {
        if (null != userInfo)
            return userInfo
        try {

            userInfo = JSON.parseObject(userInfoStr, UserInfo::class.java)
        } catch (e: Exception) {
            Log.e("userinfo", "解析用户数据错误")
        }

        return userInfo
    }

    fun saveUserInfo(userInfo: UserInfo?) {
        this.userInfo = userInfo
        userInfo?.let {
            if (!TextUtils.isEmpty(userInfo.mobile)) {
                phone = userInfo.mobile!!
            }
        }
        try {
            userInfoStr = JSON.toJSONString(userInfo)
            YcApplication.getInstance().setVistorInfo()
        } catch (e: Exception) {
            Log.e("userinfo", "保存用户数据错误")
        }

    }


    fun goToLogin(context: Context?): Boolean {
        var isGoLogin = false
        val uId = getUid() as Int
        if (uId <= 0) {
            val intent = Intent(context, LoginMainActivity::class.java)
            intent.putExtra("direct_finish", true)
            context?.startActivity(intent)
            isGoLogin = true
        }
        return isGoLogin
    }

    fun clearData() {

        this.userInfo = null
        userInfoStr = ""
    }

    fun getUid(): Int? {
        userInfo?.let {
            return userInfo?.id
        }
        return 0

    }

}