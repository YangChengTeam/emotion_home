package com.yc.emotion.home.utils

import android.app.Activity
import android.util.Log

import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yc.emotion.home.base.listener.ThirdLoginListener
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.model.bean.UserAccreditInfo
import yc.com.rthttplibrary.util.LogUtil
import yc.com.rthttplibrary.util.ToastUtil

/**
 * Created by wanglin  on 2018/11/19 13:39.
 */
class UserLoginManager {
    private var mActivity: Activity? = null
    private var mLoginListener: ThirdLoginListener? = null
    private var loadingView: LoadDialog? = null
    fun setCallBack(activity: Activity?, loginListener: ThirdLoginListener?): UserLoginManager {
        mActivity = activity
        mLoginListener = loginListener
        loadingView = LoadDialog(activity)
        return this
    }

    /**
     * 授权并登陆
     *
     * @param shareMedia
     */
    fun oauthAndLogin(shareMedia: SHARE_MEDIA?) {
        val isAuth = UMShareAPI.get(mActivity).isAuthorize(mActivity, shareMedia)
        if (isAuth) {
            UMShareAPI.get(mActivity).getPlatformInfo(mActivity, shareMedia, umAuthListener)
        } else {
            UMShareAPI.get(mActivity).doOauthVerify(mActivity, shareMedia, umAuthListener)
        }
    }

    private val umAuthListener: UMAuthListener = object : UMAuthListener {
        override fun onStart(share_media: SHARE_MEDIA) {
            showProgressDialog("正在登录中，请稍候...")
        }

        override fun onComplete(share_media: SHARE_MEDIA, action: Int, data: Map<String, String>) {
            try {
                when (action) {
                    UMAuthListener.ACTION_AUTHORIZE -> oauthAndLogin(share_media)
                    UMAuthListener.ACTION_GET_PROFILE -> if (data.isNotEmpty()) {
                        Log.e("TAG", "onComplete: $data")
                        val userDataInfo = UserAccreditInfo()
                        userDataInfo.nickname = data["name"]
                        userDataInfo.city = data["city"]
                        userDataInfo.iconUrl = data["iconurl"]
                        userDataInfo.gender = data["gender"]
                        userDataInfo.province = data["province"]
                        userDataInfo.openid = data["openid"]
                        userDataInfo.accessToken = data["accessToken"]
                        userDataInfo.uid = data["uid"]
                        mLoginListener?.onLoginResult(userDataInfo)
                        closeProgressDialog()
                    } else {
                        ToastUtil.toast(mActivity, "登录失败，请重试!")
                        closeProgressDialog()
                    }
                }
            } catch (e: Exception) {
                LogUtil.msg("complete:-->" + e.message)
                closeProgressDialog()
                ToastUtil.toast(mActivity, "登录失败，请重试!")
                deleteOauth(share_media)
            }
        }

        override fun onError(share_media: SHARE_MEDIA, action: Int, throwable: Throwable) {
            closeProgressDialog()
            LogUtil.msg("login error->>" + throwable.message)
            ToastUtil.toast(mActivity, "登录失败,请重试！")
            deleteOauth(share_media)
        }

        override fun onCancel(share_media: SHARE_MEDIA, action: Int) {
            ToastUtil.toast(mActivity, "登录取消")
            closeProgressDialog()
        }
    }

    /**
     * 取消授权
     *
     * @param shareMedia
     */
    fun deleteOauth(shareMedia: SHARE_MEDIA?) {
        if (null == mActivity) {
            return
        }
        UMShareAPI.get(mActivity).deleteOauth(mActivity, shareMedia, null)
    }

    /**
     * 显示进度框
     *
     * @param message
     */
    private fun showProgressDialog(message: String) {
        mActivity?.let {
            if (!it.isFinishing) {
                if (null == loadingView) {
                    loadingView = LoadDialog(mActivity)
                }
                loadingView?.setMessage(message)
                loadingView?.show()
            }
        }

    }

    /**
     * 关闭进度框
     */
    fun closeProgressDialog() {
        try {
            mActivity?.let {
                if (!it.isFinishing) {
                    loadingView?.let { lv ->
                        if (lv.isShowing) {
                            lv.dismiss()
                        }
                    }

                    loadingView = null
                }
            }

        } catch (e: Exception) {
            LogUtil.msg("close dialog error->>" + e.message)
        }
    }

    companion object {
        private var instance: UserLoginManager? = null
        fun get(): UserLoginManager? {
            synchronized(UserLoginManager::class.java) {
                if (instance == null) {
                    synchronized(UserLoginManager::class.java) { instance = UserLoginManager() }
                }
            }
            return instance
        }
    }
}