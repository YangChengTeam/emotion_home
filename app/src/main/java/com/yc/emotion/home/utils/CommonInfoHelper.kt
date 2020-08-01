package com.yc.emotion.home.utils

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference

import com.video.player.lib.utils.Logger
import com.yc.emotion.home.utils.CacheUtils.SubmitRunable
import com.yc.emotion.home.utils.CacheUtils.readCache
import com.yc.emotion.home.utils.CacheUtils.writeCache
import java.lang.reflect.Type

/**
 * Created by wanglin  on 2018/2/9 11:34.
 */
object CommonInfoHelper {
    fun <T> getO(context: Context?, key: String?, type: Type?, listener: OnParseListener<T>?) {
        try {
            readCache(context, key, object : SubmitRunable() {
                override fun run() {
                    val json = json
                    UIUtils.post(Runnable {
                        val t: T = parseData(json, type)
                        listener?.onParse(t)
                    })
                }
            })
        } catch (e: Exception) {
            Logger.e(CommonInfoHelper::class.java.name, "error:->>" + e.message)
        }
    }

    fun <T> setO(context: Context?, t: T, key: String?) {
        try {
            writeCache(context, key, JSON.toJSONString(t))
        } catch (e: Exception) {
            Logger.e(CommonInfoHelper::class.java.name, "error:->>" + e.message)
        }
    }

    private fun <T> parseData(result: String?, type: Type?): T {
        if (type.toString() == "java.lang.String") {
            return result as T
        }
        return if (type != null) {
            JSON.parseObject(result, type)
        } else {
            JSON.parseObject(result, object : TypeReference<T>() {}.type)
        }
    }

    interface OnParseListener<T> {
        fun onParse(o: T?)
    }
}