package com.yc.emotion.home.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object AssetUtils {
    fun getAssetData(context: Context?): String? {
        try {
            val `is` = context?.assets?.open("liveData.json")
            val br = BufferedReader(InputStreamReader(`is`))
            var line: String?
            val result = StringBuilder()
            while (br.readLine().also { line = it } != null) {
                result.append(line)
            }
            br.close()
            `is`?.close()
            return result.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}