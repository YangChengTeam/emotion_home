package com.yc.emotion.home.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by admin on 2018/2/12.
 */
object DateUtils {
    fun getFormatDateInSecond(second: String): String? {
        if (!TextUtils.isEmpty(second)) {
            val seconds = second.toInt()
            var temp = 0
            val sb = StringBuffer()
            temp = seconds / 60
            sb.append(if (temp < 10) "0$temp:" else "$temp:")
            temp = seconds % 60
            sb.append(if (temp < 10) "0$temp" else "" + temp)
            return sb.toString()
        }
        return null
    }

    @JvmOverloads
    fun formatTimeToStr(time: Long, format: String? = "yyyy-MM-dd HH:mm"): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(Date(time * 1000))
    }
}