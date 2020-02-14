package com.yc.emotion.home.utils

import android.content.Context
import com.yc.emotion.home.base.YcApplication
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.net.URLEncoder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *
 * Created by wanglin  on 2019/3/22 09:08.
 */

class Preference<T>(val context: Context, val string: String, private val default: T) : ReadWriteProperty<Any?, T> {

    constructor(string: String, default: T) : this(YcApplication.getInstance().applicationContext, string, default)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(string, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(string, value)
    }

    val prefs by lazy { context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE) }

    private fun <A> putPreference(name: String, value: A) = with(prefs.edit()) {
        when (value) {//if语句 现在在kotlin中是表达式
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }

    private fun <U> findPreference(name: String, default: U): U = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can not be saved")
        }
        res as U
    }

    private fun <A> serialize(obj: A): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()

        return serStr
    }

}