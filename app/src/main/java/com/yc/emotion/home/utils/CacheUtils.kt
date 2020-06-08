package com.yc.emotion.home.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener
import com.yc.emotion.home.index.ui.view.imgs.FileUtils
import java.io.File

/**
 * Created by wanglin  on 2018/2/9 11:10.
 */
object CacheUtils {
    @JvmStatic
    fun writeCache(context: Context?, key: String?, json: String?) {
        ThreadPoolUtils(ThreadPoolUtils.CachedThread, 5).execute {
            SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : CheckRequestPermissionListener {
                override fun onPermissionOk(permission: Permission) {
                    val path = FileUtils.createDir(makeBaseDir(context) + "/cache")
                    FileIOUtils.writeFileFromString("$path/$key", json)
                }

                override fun onPermissionDenied(permission: Permission) {}
            })
        }
    }

    @JvmStatic
    fun readCache(context: Context?, key: String?, runable: SubmitRunable?) {
        ThreadPoolUtils(ThreadPoolUtils.FixedThread, 5).execute {
            SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,object :CheckRequestPermissionListener{
                override fun onPermissionOk(permission: Permission?) {
                    val path = FileUtils.createDir(makeBaseDir(context) + "/cache")
                    val json = FileIOUtils.readFile2String("$path/$key")
                    if (!TextUtils.isEmpty(json)) {
                        if (runable != null) {
                            runable.json = json
                            runable.run()
                        }
                    }
                }

                override fun onPermissionDenied(permission: Permission?) {

                }
            })

//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//            }
        }
    }

    private fun makeBaseDir(context: Context?): String {
        val dir = File(Environment.getExternalStorageDirectory().toString() + "/" + context?.packageName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.absolutePath
    }

    abstract class SubmitRunable : Runnable {
        var json: String? = null

    }
}