package com.yc.emotion.home.utils

import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSON

import com.yc.emotion.home.base.EmApplication
import com.yc.emotion.home.base.domain.engine.BaseModel
import com.yc.emotion.home.mine.domain.model.RewardModel
import com.yc.emotion.home.model.bean.ShareInfo
import com.yc.emotion.home.model.util.SPUtils
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig


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
                val str = SPUtils.get(EmApplication.instance.applicationContext, SPUtils.SHARE_INFO, "") as String
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
                SPUtils.put(EmApplication.instance.applicationContext, SPUtils.SHARE_INFO, str)
            } catch (e: Exception) {
                Log.e(TAG, "setShareInfo: 保存json失败" + e.message)
            }
        }

    @JvmStatic
    fun getNetShareInfo(context: Context?) {

        val baseModel = BaseModel(context)
        baseModel.getShareLink()?.subscribe(object : DisposableObserver<ResultInfo<ShareInfo>>() {
            override fun onComplete() {}
            override fun onNext(shareInfoResultInfo: ResultInfo<ShareInfo>) {
                if (shareInfoResultInfo.code == HttpConfig.STATUS_OK && shareInfoResultInfo.data != null) {
                    val shareInfos = shareInfoResultInfo.data
                    shareInfos?.let {
                        shareInfo = shareInfos
                    }
                }
            }
            override fun onError(e: Throwable) {}
        })

//        val loveEngin = LoveEngine(context)
//        loveEngin.getShareInfo().subscribe(object : DisposableObserver<ResultInfo<List<ShareInfo>>?>() {
//            override fun onComplete() {}
//            override fun onError(e: Throwable) {}
//            override fun onNext(shareInfoResultInfo: ResultInfo<List<ShareInfo>>) {
//                if (shareInfoResultInfo.code == HttpConfig.STATUS_OK && shareInfoResultInfo.data != null) {
//                    val shareInfos = shareInfoResultInfo.data
//                    shareInfos?.let {
//                        if (shareInfos.isNotEmpty()) {
//                            val shareInfo = shareInfos[0]
//                            ShareInfoHelper.shareInfo = shareInfo
//
//                        }
//                    }
//
//                }
//            }
//        })
    }
}