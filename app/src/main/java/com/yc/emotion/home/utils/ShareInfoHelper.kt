package com.yc.emotion.home.utils

import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSON
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.YcApplication
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.model.util.SPUtils
import rx.Subscriber

/**
 * Created by wanglin  on 2019/7/9 18:18.
 */
object ShareInfoHelper {
    private const val TAG = "ShareInfoHelper"
    private var mShareInfo: ShareInfo? = null

    var shareInfo: ShareInfo?
        get() {
            if (mShareInfo != null) {
                return mShareInfo
            }
            try {
                val str = SPUtils.get(YcApplication.getInstance().applicationContext, SPUtils.SHARE_INFO, "") as String
                mShareInfo = JSON.parseObject(str, ShareInfo::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "getShareInfo: 解析json失败" + e.message)
            }
            return mShareInfo
        }
        set(ShareInfo) {
            mShareInfo = ShareInfo
            try {
                val str = JSON.toJSONString(ShareInfo)
                SPUtils.put(YcApplication.getInstance().applicationContext, SPUtils.SHARE_INFO, str)
            } catch (e: Exception) {
                Log.e(TAG, "setShareInfo: 保存json失败" + e.message)
            }
        }

    @JvmStatic
    fun getNetShareInfo(context: Context?) {
        val loveEngin = LoveEngine(context)
        loveEngin.getShareInfo(context).subscribe(object : Subscriber<ResultInfo<List<ShareInfo>>?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {}
            override fun onNext(shareInfoResultInfo: ResultInfo<List<ShareInfo>>?) {
                if (shareInfoResultInfo != null && shareInfoResultInfo.code == HttpConfig.STATUS_OK && shareInfoResultInfo.data != null) {
                    val shareInfos = shareInfoResultInfo.data
                    shareInfos?.let {
                        if (shareInfos.isNotEmpty()) {
                            val shareInfo = shareInfos[0]
                            ShareInfoHelper.shareInfo=shareInfo

                        }
                    }

                }
            }
        })
    }
}