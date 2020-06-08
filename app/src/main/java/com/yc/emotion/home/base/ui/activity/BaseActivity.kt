package com.yc.emotion.home.base.ui.activity

import android.content.*
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.listener.OnUserInfoListener
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.WetChatInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.StatusBarUtil
import org.jetbrains.annotations.NotNull
import rx.Subscriber

/**
 * Created by mayn on 2019/4/25.
 */

abstract class BaseActivity : AppCompatActivity(), IView, IDialog, UserInfoView {

    var mLoadingDialog: LoadDialog? = null
    protected var mLoveEngine: LoveEngine? = null
    protected var mHandler: Handler? = null
    private var taskRunnable: MyRunnable? = null

    protected var mPresenter: BasePresenter<out IModel, out IView>? = null

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private var contentViewGroup: View? = null

    /**
     * 定时任务，模拟倒计时广告
     */
    private var totalTime = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(getLayoutId())

//        mLoadingDialog = LoadDialog(this)
        mLoadingDialog = LoadDialog(this)
        mLoveEngine = LoveEngine(this)
        mHandler = Handler()
        mPresenter = UserInfoPresenter(this, this)
//        initViews()


    }

    override fun onStart() {
        super.onStart()
        mPresenter?.subscribe()
    }


    /**
     * 侵入状态栏
     * 让背景图片可以利用系统状态栏的空间，从而能够让背景图和状态栏融为一体。
     */
    fun invadeStatusBar() {

        StatusBarUtil.setMaterialStatus(this)
    }

    /**
     * 谷歌原生方式改变状态栏文字颜色
     *
     * @param dark 一旦用谷歌原生设置状态栏文字颜色的方法进行设置的话，因为一直会携带SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN这个flag，
     * 那么默认界面会变成全屏模式，需要在根布局中设置FitsSystemWindows属性为true
     */
    fun setAndroidNativeLightStatusBar(dark: Boolean) {
        val decor = window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    fun setAndroidNativeLightStatusBar() {
        StatusBarUtil.setStatusTextColor1(true, this)
        //        setAndroidNativeLightStatusBar(true);
    }

    @JvmOverloads
    fun setStateBarHeight(viewBar: View, addHeight: Int = 0) {
        var result = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
        if (result <= 0) {
            return
        }
        val layoutParams = viewBar.layoutParams
        layoutParams.height = result + addHeight
        viewBar.layoutParams = layoutParams
        Log.d("ClassName", "setStateBarHeight: layoutParams.height " + layoutParams.height)
    }


    /**
     * 全透状态栏
     */
    protected fun setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected fun setHalfTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected fun setFitSystemWindow(fitSystemWindow: Boolean) {
        if (contentViewGroup == null) {
            contentViewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        }
        contentViewGroup?.fitsSystemWindows = fitSystemWindow
    }

    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
    protected fun setDrawerLayoutFitSystemWindow() {
        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
            val statusBarHeight = getStatusHeight(this)
            if (contentViewGroup == null) {
                contentViewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
            }
            if (contentViewGroup is DrawerLayout) {
                val drawerLayout = contentViewGroup as DrawerLayout
                drawerLayout.clipToPadding = true
                drawerLayout.fitsSystemWindows = false
                for (i in 0 until drawerLayout.childCount) {
                    val child = drawerLayout.getChildAt(i)
                    child.fitsSystemWindows = false
                    child.setPadding(0, statusBarHeight, 0, 0)
                }

            }
        }
    }

    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height").get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    fun hindKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun formatting(data: String?): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<html>")
        stringBuilder.append("<head><style>img{max-width: 100%!important;height:auto!important;}body{background:#fff;position: relative;line-height:1.6;font-family:Microsoft YaHei,Helvetica,Tahoma,Arial,\\5FAE\\8F6F\\96C5\\9ED1,sans-serif}</style></head>")
        stringBuilder.append("<body>")
        stringBuilder.append(data)
        stringBuilder.append("</body></html>")
        return stringBuilder.toString()
    }


    fun showToWxServiceDialog(tutorId: String? = "", articelId: String? = "", exampleId: String? = "", listener: OnWxListener? = null) {
        MobclickAgent.onEvent(this, ConstantKey.UM_CONTACT_US_CLICK_ID)
        val mWechat = arrayOf("pai201807")


        mLoveEngine?.getWechatInfo(tutorId, articelId, exampleId)?.subscribe(object : Subscriber<ResultInfo<WetChatInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                getWechatInfoSuccess(mWechat[0], listener)
            }

            override fun onNext(wetChatInfoResultInfo: ResultInfo<WetChatInfo>?) {
                if (wetChatInfoResultInfo != null && wetChatInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    mWechat[0] = wetChatInfoResultInfo.data.weixin
                    getWechatInfoSuccess(mWechat[0], listener)
                }
            }
        })


    }


    override fun getWechatInfoSuccess(wx: String, listener: OnWxListener?) {

        if (null != listener) {
            listener.onWx(wx)
        } else {
            runOnUiThread {
                showService(wx)
//                                               showServieDialog(mWechat[0]));
            }
        }
    }

    private var centerToast: Toast? = null
    private lateinit var sMtvText: TextView

    fun showToast(@NotNull mess: String, duration: Int = Toast.LENGTH_SHORT, vararg test: String) = run { //        val toast = Toast.makeText(BaseActivity.this, mess, duration)
//        toast.gravity=Gravity.CENTER
        if (null == centerToast) {
            centerToast = Toast(this@BaseActivity)
            centerToast?.duration = duration
            centerToast?.setGravity(Gravity.NO_GRAVITY, 0, 0)
            val view = View.inflate(this@BaseActivity, R.layout.toast_center_layout, null)
            sMtvText = view.findViewById<View>(R.id.tv_text) as TextView
            sMtvText.text = if (TextUtils.isEmpty(mess)) "null" else mess
            centerToast?.view = view
        } else {
            sMtvText.text = if (TextUtils.isEmpty(mess)) "null" else mess
        }
        centerToast?.show()

    }


    private fun showService(wechat: String) {
        val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", wechat)
        myClipboard.primaryClip = myClip

        val addWxFragment = AddWxFragment()
        addWxFragment.setWX(wechat)
        addWxFragment.show(supportFragmentManager, "")
        addWxFragment.setListener(object : AddWxFragment.OnToWxListener {
            override fun onToWx() {
                openWeiXin()
                addWxFragment.dismiss()
            }
        })
    }


    private fun openWeiXin() {

        try {
            MobclickAgent.onEvent(this, ConstantKey.UM_CONTACT_US_TO_WECHAT_ID)
            val intent = Intent()
            val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            startActivity(intent)
        } catch (exception: Exception) {
            showToast("未安装微信")
        }

    }


    private inner class MyRunnable(internal var mTv: TextView) : Runnable {

        override fun run() {
            mTv.text = "${totalTime}s"
            totalTime--
            if (totalTime < 0) {
                //还原
                initGetCodeBtn(mTv)
                return
            }
            mHandler?.postDelayed(this, 1000)
        }
    }


    /**
     * 还原获取验证码按钮状态
     */
    private fun initGetCodeBtn(textView: TextView) {
        totalTime = 0
        if (null != taskRunnable && null != mHandler) {
            mHandler?.removeCallbacks(taskRunnable)
            mHandler?.removeMessages(0)
        }
        textView.text = "重新获取"
        textView.isClickable = true

    }

    /**
     * 改变获取验证码按钮状态
     */
    fun showGetCodeDisplay(textView: TextView) {
        taskRunnable = MyRunnable(textView)
        if (null != mHandler) {
            mHandler?.removeCallbacks(taskRunnable)
            mHandler?.removeMessages(0)
            totalTime = 60
            textView.isClickable = false
            //            textView.setTextColor(ContextCompat.getColor(R.color.coment_color));
            //            textView.setBackgroundResource(R.drawable.bg_btn_get_code);
            mHandler?.postDelayed(taskRunnable, 0)
        }
    }


    override fun showLoadingDialog() {
        mLoadingDialog?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mLoadingDialog?.dismissLoadingDialog()
    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)

    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (null != mHandler)
            mHandler = null

        mLoadingDialog?.let {
            if (it.isShowing) {
                it.dismissLoadingDialog()
            }
            mLoadingDialog = null
        }


        mPresenter?.unSubscribe()

//        windowManager
    }

    interface OnWxListener {
        fun onWx(wx: String)
    }

    /**
     * 根据百分比改变颜色透明度
     */
    fun changeAlpha(color: Int, fraction: Float): Int {
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, 255, 255, 255)
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(listener: OnUserInfoListener? = null) {

        (mPresenter as? UserInfoPresenter)?.userInfo(listener)
    }

    override fun getUserInfoSuccess(userInfo: UserInfo, listener: OnUserInfoListener?) {
        listener?.onUserInfo(userInfo)
    }


}
/**
 * 获取状态栏高度 直接获取属性，通过getResource
 *
 * @return
 */
