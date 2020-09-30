package com.yc.emotion.home.mine.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.utils.UIUtils
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }


    override fun initViews() {
        super.initViews()
        try {
            // 判断当前的版本与服务器上的最版版本是否一致
            val packageInfo = packageManager.getPackageInfo(application.packageName, 0)
            about_us_tv_version.text = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        about_us_tv_name.text=UIUtils.getAppName(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()


    }

    override fun offerActivityTitle(): String {
        return "关于我们"
    }
}
