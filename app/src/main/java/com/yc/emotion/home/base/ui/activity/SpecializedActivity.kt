package com.yc.emotion.home.base.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bytedance.sdk.openadsdk.TTNativeExpressAd

import com.yc.emotion.home.R
import com.yc.emotion.home.base.constant.Constant
import com.yc.emotion.home.base.ui.fragment.common.PrivacyPolicyFragment
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.model.util.CheckNetwork
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UIUtils
import kotlinx.android.synthetic.main.activity_specialized.*
import yc.com.toutiao_adv.OnAdvStateListener
import yc.com.toutiao_adv.TTAdDispatchManager
import yc.com.toutiao_adv.TTAdType


class SpecializedActivity : BaseActivity(), OnAdvStateListener {


    private var isFirstGuide by Preference(ConstantKey.IS_FIRST_GUIDE, true)
    private val isClick = false
    private var isFirstOpen by Preference(Constant.first_open, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialized)
        initViews()

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_specialized
    }

    override fun initViews() {
        tv_app_name.text = UIUtils.getAppName(this)
        invadeStatusBar()
        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        if (isFirstOpen) {
            mHandler?.postDelayed({
                val privacyFragment = PrivacyPolicyFragment();
                privacyFragment.show(supportFragmentManager, "")
                privacyFragment.setOnClickBtnListener(object : PrivacyPolicyFragment.OnClickBtnListener {
                    override fun onBtnClick() {
//                        switchMain(0)
                        TTAdDispatchManager.getManager().init(this@SpecializedActivity, TTAdType.SPLASH, splash_container, Constant.TOUTIAO_SPLASH_ADV_ID, 0, null, 0, null, 0, this@SpecializedActivity)
                        isFirstOpen = false
                    }
                })
            }, 100)

        } else {
//            switchMain(100)
            TTAdDispatchManager.getManager().init(this@SpecializedActivity, TTAdType.SPLASH, splash_container, Constant.TOUTIAO_SPLASH_ADV_ID, 0, null, 0, null, 0, this@SpecializedActivity)
        }


    }


    private fun startNextActivity() {

//        if (isFirstGuide) {
//            startActivity(Intent(this, GuideActivity::class.java))
//            isFirstGuide = false
//        } else {
//
//        }
        startActivity(Intent(this, MainActivity::class.java))
//                overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);  //开屏动画
        finish()
    }

    private fun checkNetwork() {
        val networkConnected = CheckNetwork.isNetworkConnected(this@SpecializedActivity)
        val connected = CheckNetwork.isWifiConnected(this@SpecializedActivity)
        Log.d("mylog", "checkNetwork: networkConnected $networkConnected")
        Log.d("mylog", "checkNetwork: connected $connected")
        if (!networkConnected) {
            showNotNetworkDialog()
        } else {
            startNextActivity()
        }
    }

    override fun onResume() {
        super.onResume()

//        AdvDispatchManager.getManager().onResume();
        TTAdDispatchManager.getManager().onResume()
    }

    override fun onPause() {
        super.onPause()

//        AdvDispatchManager.getManager().onPause();
        TTAdDispatchManager.getManager().onStop()

    }

    private fun showNotNetworkDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage("网络连接失败，请重试")
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "重试") { dialogInterface, i -> checkNetwork() }
        alertDialog.show()
    }

    private fun switchMain(delay: Long) {

        UIUtils.postDelay(Runnable { startNextActivity() }, delay)
    }


    //防止用户返回键退出 APP
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun clickAD() {
        switchMain(0)
    }

    override fun loadSuccess() {

        switchMain(0)
    }

    override fun loadFailed() {
        switchMain(0)
    }

    override fun onTTNativeExpressed(ads: MutableList<TTNativeExpressAd>?) {

    }

}
