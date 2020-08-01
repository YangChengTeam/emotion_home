package com.yc.emotion.home.base.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.qq.e.ads.nativ.NativeExpressADView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.common.PrivacyPolicyFragment
import com.yc.emotion.home.constant.Constant
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.model.util.CheckNetwork
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UIUtils
import kotlinx.android.synthetic.main.activity_specialized.*
import yc.com.tencent_adv.AdvDispatchManager
import yc.com.tencent_adv.OnAdvStateListener

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
                        switchMain(0)
                        isFirstOpen = false
                    }
                })
            }, 100)

        } else {
            switchMain(100)
        }

//        AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splash_container, skip_view, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this)
    }


    private fun startNextActivity() {

//        if (isFirstGuide) {
//            startActivity(Intent(this, GuideActivity::class.java))
//            isFirstGuide = false
//        } else {
//
//        }
        startActivity(Intent(this, MainActivity::class.java))
        //        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);  //开屏动画
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

        AdvDispatchManager.getManager().onResume()
    }

    override fun onPause() {
        super.onPause()

        AdvDispatchManager.getManager().onPause()

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


    override fun onShow() {
        rl_top_guide.visibility = View.GONE
        skip_view.visibility = View.VISIBLE
    }

    override fun onDismiss(delayTime: Long) {
        switchMain(delayTime)
    }

    override fun onError() {
        startNextActivity()
    }

    override fun onNativeExpressDismiss(view: NativeExpressADView) {}

    override fun onNativeExpressShow(mDatas: Map<NativeExpressADView, Int>) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    //防止用户返回键退出 APP
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

}
