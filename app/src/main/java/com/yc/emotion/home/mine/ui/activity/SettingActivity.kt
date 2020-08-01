package com.yc.emotion.home.mine.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.tencent.bugly.beta.Beta
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.util.DataCleanManagerUtils
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.UserLoginManager
import kotlinx.android.synthetic.main.activity_setting.*
import org.greenrobot.eventbus.EventBus

class SettingActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }

    override fun initViews() {

        val userId = UserInfoHelper.instance.getUid() as Int
        if (userId <= 0) {
            setting_tv_exit.visibility = View.GONE
        } else {
            setting_tv_exit.visibility = View.VISIBLE
        }

        setting_tv_exit.setOnClickListener(this)

        mineItemView_clear_cache.setOnClickListener(this)
        mineItemView_update.setOnClickListener(this)
        mineItemView_score.setOnClickListener(this)

        mineItemView_about_us.setOnClickListener(this)

        mineItemView_update.setMoreText("v${packageManager.getPackageInfo(packageName, 0).versionName}")

        try {
            val totalCacheSize = DataCleanManagerUtils.getTotalCacheSize(this@SettingActivity)
            mineItemView_clear_cache.setMoreText(totalCacheSize)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.setting_tv_exit -> clearExit()
            R.id.mineItemView_update -> Beta.checkUpgrade(true, false)
            R.id.mineItemView_about_us -> startActivity(Intent(this, AboutUsActivity::class.java))

            R.id.mineItemView_clear_cache -> clearCache()
            R.id.mineItemView_score -> {
                toMarket()
            }
        }
    }

    private fun toMarket() {
        try {
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showCenterToast("你手机安装的应用市场没有上线该应用，请前往其他应用市场进行点评")
        }

    }

    private fun clearExit() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage("是否退出登录？")
        val listent: DialogInterface.OnClickListener? = null
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出") { dialogInterface, i ->
            //                SPUtils.put(SettingActivity.this, SPUtils.LOGIN_PWD, "");
            UserInfoHelper.instance.clearData()
            UserLoginManager.get()?.deleteOauth(SHARE_MEDIA.WEIXIN)
            UserLoginManager.get()?.deleteOauth(SHARE_MEDIA.QQ)
            EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_EXIT))
            //                showToastShort("退出登录成功");
            finish()
        }
        alertDialog.show()
    }

    private fun clearCache() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage("是否清空缓存？")
        val listent: DialogInterface.OnClickListener? = null
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "清空缓存") { dialogInterface, i ->
            DataCleanManagerUtils.clearAllCache(this@SettingActivity)
            val loadingView = LoadDialog(this@SettingActivity)
            loadingView.showLoadingDialog()
            mineItemView_clear_cache.postDelayed({
                try {
                    val totalCacheSize = DataCleanManagerUtils.getTotalCacheSize(this@SettingActivity)
                    mineItemView_clear_cache.setMoreText(totalCacheSize)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                loadingView.dismissLoadingDialog()
                showToast("清除成功")
            }, 600)
        }
        alertDialog.show()
    }

    override fun offerActivityTitle(): String {
        return "系统设置"
    }
}
