package com.yc.emotion.home.utils

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

/**
 * Copyright © 2016 Zego. All rights reserved.
 * des: Preference管理工具类.
 * 主要用于存储一些临时数据
 */
class PreferenceUtil private constructor() {
    private val mSharedPreferences: SharedPreferences
    fun setStringValue(key: String?, value: String?) {
        val editor = mSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(key: String?, defaultValue: String?): String {
        return mSharedPreferences.getString(key, defaultValue)
    }

    fun setBooleanValue(key: String?, value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(key: String?, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun setIntValue(key: String?, value: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getIntValue(key: String?, defaultValue: Int): Int {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    fun setLongValue(key: String?, value: Long) {
        val editor = mSharedPreferences.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun getLongValue(key: String?, defaultValue: Long): Long {
        return mSharedPreferences.getLong(key, defaultValue)
    }

    companion object {
        /**
         * 单例.
         */
        var sInstance: PreferenceUtil? = null
        const val SHARE_PREFERENCE_NAME = "ZEGO_DEMO_PLAYGROUND"
        const val KEY_APP_ID = "PLAYGROUND_APP_ID"
        const val KEY_APP_SIGN = "PLAYGROUND_APP_SIGN"
        const val KEY_TEST_ENVIRONMENT = "PLAYGROUND_ENV"
        const val KEY_SCENARIO = "PLAYGROUND_SCENARIO"
        val instance: PreferenceUtil?
            get() {
                if (sInstance == null) {
                    synchronized(PreferenceUtil::class.java) {
                        if (sInstance == null) {
                            sInstance = PreferenceUtil()
                        }
                    }
                }
                return sInstance
            }
    }

    init {
        mSharedPreferences = instance.getSharedPreferences(SHARE_PREFERENCE_NAME, AppCompatActivity.MODE_PRIVATE)
    }
}