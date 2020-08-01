package com.yc.emotion.home.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.sunfusheng.marqueeview.MarqueeView

import java.util.*


/**
 * Created by wanglin  on 2018/2/5 17:37.
 */
object UIUtils {
    private val handler = Handler(Looper.getMainLooper())
    fun post(runnable: Runnable?) {
        handler.post(runnable)
    }

    fun postDelay(runnable: Runnable?, i: Long) {
        handler.postDelayed(runnable, i)
    }

    fun setMarqueList(marqueeView: MarqueeView<*>) {
        post(Runnable {
            val messages = ArrayList<String>()
            messages.add("如何挽回那个人的心")
            messages.add("失恋怎么办")
            messages.add("老公不思上进怎么办")
            messages.add("生活没有激情怎么办")
            messages.add("挽回婚姻靠哪几招？")
            marqueeView.startWithList(messages as List<Nothing>?)
        })
    }


    fun getAppName(context: Context?): String {
        return if (context == null) {
            ""
        } else {
            var appName = ""
            try {
                val packageManager = context.packageManager
                var applicationInfo: ApplicationInfo? = null
                try {
                    applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                appName = if (null != applicationInfo) {
                    packageManager.getApplicationLabel(applicationInfo) as String
                } else {
                    context.resources.getString(context.applicationInfo.labelRes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            appName
        }

    }
}