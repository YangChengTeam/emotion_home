package com.yc.emotion.home.utils

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSON
import com.yc.emotion.home.base.EmApplication
import com.yc.emotion.home.index.ui.fragment.WxLoginFragment
import com.yc.emotion.home.mine.ui.activity.LoginRegisterActivity
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
        userInfo?.let {
            return it
        }

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
            val mobile = it.mobile
            mobile?.let {
                if (!TextUtils.isEmpty(mobile)) {
                    phone = mobile
                }
            }

        }
        try {
            userInfoStr = JSON.toJSONString(userInfo)
            EmApplication.instance.setVistorInfo()
        } catch (e: Exception) {
            Log.e("userinfo", "保存用户数据错误")
        }

    }


    fun goToLogin(context: Context?): Boolean {
        var isGoLogin = false
        val uId = getUid()
        if (uId <= 0) {
//            val intent = Intent(context, LoginRegisterActivity::class.java)
//            intent.putExtra("direct_finish", true)
//            context?.startActivity(intent)
            val wxLoginFragment = WxLoginFragment()
            context?.let {
                if (it is AppCompatActivity) {
                    wxLoginFragment.show(it.supportFragmentManager, "")
                }
            }

            isGoLogin = true
        }
        return isGoLogin
    }

    fun clearData() {

        this.userInfo = null
        userInfoStr = ""
    }

    fun getUid() = getUserInfo()?.id ?: 0
}