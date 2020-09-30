package com.yc.emotion.home.base.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.UMShareAPI
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.factory.MainFragmentFactory
import com.yc.emotion.home.index.domain.bean.GetAppIDConfig
import com.yc.emotion.home.index.ui.fragment.IndexFragment
import com.yc.emotion.home.index.ui.fragment.IndexVerbalMainFragment
import com.yc.emotion.home.message.ui.fragment.VideoFragment
import com.yc.emotion.home.mine.ui.fragment.MineFragment
import com.yc.emotion.home.receiver.NetWorkChangReceiver
import com.yc.emotion.home.skill.ui.fragment.SkillMainFragment
import com.yc.emotion.home.utils.PreferenceUtil
import com.yc.emotion.home.utils.PreferenceUtil.Companion.KEY_APP_ID
import com.yc.emotion.home.utils.PreferenceUtil.Companion.KEY_APP_SIGN

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnClickListener {


    private var netWorkChangReceiver: NetWorkChangReceiver? = null
    private var mPackageVersionName: String? = null
    private var mDownloadIdKey = "mDownloadIdKey"
    private var onChildDisposeMainKeyDownListent: OnChildDisposeMainKeyDownListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_main)
        initViews()
    }


    override fun getLayoutId() = R.layout.activity_main


    override fun initViews() {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
//                checkPermission();
        mPackageVersionName = try {
            // 判断当前的版本与服务器上的最版版本是否一致
            val packageInfo = packageManager.getPackageInfo(application.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.d("mylog", "run: getPackageManager $e")
            "1.0"
        }

        mDownloadIdKey = "download_id$mPackageVersionName"
        Log.d("mylog", "onCreate: download_id mDownloadIdKey $mDownloadIdKey")
        //初始化MusicService

//        MusicPlayerManager.getInstance().bindService(this)
        initView()
        if (PreferenceUtil.instance?.getStringValue(KEY_APP_ID, "") == "") {
            PreferenceUtil.instance?.setStringValue(KEY_APP_ID, java.lang.String.valueOf(GetAppIDConfig.appID))
        }
        if (PreferenceUtil.instance?.getStringValue(KEY_APP_SIGN, "") == "") {
            PreferenceUtil.instance?.setStringValue(KEY_APP_SIGN, GetAppIDConfig.appSign)
        }

        getUserInfo()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val currentItem = comp_main_vp_fragment.currentItem
            return if (MainFragmentFactory.MAIN_FRAGMENT_3 == currentItem && onChildDisposeMainKeyDownListent != null && onChildDisposeMainKeyDownListent!!.onChildDisposeMainKeyDown()) {
                Log.d("mylog", "onKeyDown: WebView goBack")
                true
            } else {
                Log.d("mylog", "onKeyDown: 退出app")

                //                Intent home = new Intent(Intent.ACTION_MAIN);
                //                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                home.addCategory(Intent.CATEGORY_HOME);
                //                startActivity(home);
                finish()
                //                System.exit(0);
                true
            }
        }

        return super.onKeyDown(keyCode, event)
    }


    interface OnChildDisposeMainKeyDownListener {
        fun onChildDisposeMainKeyDown(): Boolean
    }

    fun setOnChildDisposeMainKeyDownListener(onChildDisposeMainKeyDownListener: OnChildDisposeMainKeyDownListener) {
        this.onChildDisposeMainKeyDownListent = onChildDisposeMainKeyDownListener
    }


    private fun initNetWorkChangReceiver() {
        //注册网络状态监听广播
        netWorkChangReceiver = NetWorkChangReceiver()
        val filter = IntentFilter()
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netWorkChangReceiver, filter)
        //        isRegistered = true;
    }


    override fun onDestroy() {
        super.onDestroy()
        //解绑
        //        if (isRegistered) {
        if (netWorkChangReceiver != null) {
            unregisterReceiver(netWorkChangReceiver)
        }
//        MusicPlayerManager.getInstance().unBindService(this)

    }

    private fun initView() {


        comp_main_iv_to_wx.setOnClickListener(this)

        comp_main_index.setOnClickListener(this)
        comp_main_inVerbal.setOnClickListener(this)
        comp_main_community.setOnClickListener(this)
        comp_main_message.setOnClickListener(this)

        comp_main_my.setOnClickListener(this)

        val fragmentList = arrayListOf<Fragment>()
        fragmentList.add(IndexFragment())
        fragmentList.add(IndexVerbalMainFragment())
//        fragmentList.add(CommunityMainFragment())
        fragmentList.add(VideoFragment())
//        fragmentList.add(MessageActivity())
        fragmentList.add(SkillMainFragment())
        fragmentList.add(MineFragment())


        val commonMainPageAdapter = CommonMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, null, fragmentList)

//        val mainPagerAdapter = MainPagerAdapter(supportFragmentManager)

        comp_main_vp_fragment.adapter = commonMainPageAdapter
//        comp_main_vp_fragment.offscreenPageLimit = 4

        comp_main_vp_fragment.currentItem = 0
        comp_main_index.postDelayed({ this.initNetWorkChangReceiver() }, 200)

        comp_main_vp_fragment.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) =
                    Unit

            override fun onPageSelected(position: Int) {
                if (position == MainFragmentFactory.MAIN_FRAGMENT_2) {
                    if (comp_main_iv_to_wx.visibility != View.GONE) {
                        comp_main_iv_to_wx.visibility = View.GONE
                    }
                } else {
                    if (comp_main_iv_to_wx.visibility != View.VISIBLE) {
                        comp_main_iv_to_wx.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.comp_main_index -> {
                setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_0)
                MobclickAgent.onEvent(this, "index_click", "导航首页点击")
            }
            R.id.comp_main_inVerbal -> {
                setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_1)
                MobclickAgent.onEvent(this, "main_verbal_click", "导航话术点击")
            }
            R.id.comp_main_community -> {
                setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_2)
                MobclickAgent.onEvent(this, "main_video_click", "导航视频点击")
            }
            R.id.comp_main_message -> {
                setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3)
                MobclickAgent.onEvent(this, "main_pratice_click", "导航教学点击")

            }
            R.id.comp_main_my -> {
                setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_4)
            }

            R.id.comp_main_iv_to_wx -> {
                showToWxServiceDialog()
                MobclickAgent.onEvent(this, "contact_us_click", "联系导师点击悬浮")
            }

        }
        //                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3, false);
        //                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_3);
    }

    private fun setCurrentItem(pos: Int) {
        comp_main_vp_fragment.setCurrentItem(pos, false)
        iconSelect(pos)
    }


    private fun iconSelect(current: Int) {
        clearIconState()
        when (current) {
            MainFragmentFactory.MAIN_FRAGMENT_0 -> setCompoundDrawablesTop(comp_main_index, R.mipmap.main_icon_tab_01_s)

            MainFragmentFactory.MAIN_FRAGMENT_1 -> setCompoundDrawablesTop(comp_main_inVerbal, R.mipmap.home_speech_sel)
            MainFragmentFactory.MAIN_FRAGMENT_2 -> setCompoundDrawablesTop(comp_main_community, R.mipmap.home_course_sel)
            MainFragmentFactory.MAIN_FRAGMENT_3 -> setCompoundDrawablesTop(comp_main_message, R.mipmap.home_love_sel)
            MainFragmentFactory.MAIN_FRAGMENT_4 -> setCompoundDrawablesTop(comp_main_my, R.mipmap.main_icon_tab_05_s)
        }
    }

    private fun clearIconState() {
        setCompoundDrawablesTop(comp_main_index, R.mipmap.main_icon_tab_01)
        setCompoundDrawablesTop(comp_main_inVerbal, R.mipmap.home_speech_default)
        setCompoundDrawablesTop(comp_main_community, R.mipmap.home_course_default)
        setCompoundDrawablesTop(comp_main_message, R.mipmap.home_love_default)
        //        setCompoundDrawablesTop(mTvTab4, R.mipmap.main_icon_tab_04);
        setCompoundDrawablesTop(comp_main_my, R.mipmap.main_icon_tab_05)

        comp_main_index.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        comp_main_inVerbal.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        comp_main_community.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        comp_main_message.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        //        mTvTab4.setTextColor(getResources().getColor(R.color.text_gray));
        comp_main_my.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
    }

    private fun setCompoundDrawablesTop(tv_icon: TextView?, id: Int) {
        val top22 = ContextCompat.getDrawable(this, id)
        tv_icon?.setCompoundDrawablesWithIntrinsicBounds(null, top22, null, null)
        tv_icon?.setTextColor(ContextCompat.getColor(this, R.color.black))

    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val pos = intent.getIntExtra("pos", 0)

//        setCurrentItem(pos)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}
