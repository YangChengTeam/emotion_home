package com.yc.emotion.home.base.domain.engine

import android.content.Context
import android.util.Log

/**
 * Created by wanglin  on 2018/10/25 13:54.
 */
open class BaseEngine(protected var mContext: Context?) {
    fun requestParams(params: Map<String?, String?>) {
        val stringBuffer = StringBuffer()
        for (s in params.keys) {
            stringBuffer.append(s).append(":").append(params[s]).append("   ")
        }
        Log.d("mylog", "requestParams: $stringBuffer")
    }

}