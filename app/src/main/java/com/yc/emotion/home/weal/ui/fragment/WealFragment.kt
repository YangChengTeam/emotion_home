package com.yc.emotion.home.weal.ui.fragment

import android.annotation.SuppressLint
import android.content.*
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.net.contains.HttpConfig
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.activity.MainActivity.OnChildDisposeMainKeyDownListent
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.MenuadvInfoBean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.CommonInfoHelper.OnParseListener
import kotlinx.android.synthetic.main.fragment_main_t4.*
import rx.Subscriber

/**
 * Created by mayn on 2019/5/22.
 * 福利
 */
class WealFragment : BaseFragment<BasePresenter<*, *>>() {
//    private var mWebView: WebView? = null

    //    private var mProgressBar: ProgressBar? = null
    private var homeUrl: String? = null
    private var mWechat: String? = null
    private var mLoadDialog: LoadDialog? = null
    private var mLoveEngine: LoveEngine? = null
    private var mMainActivity: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_t4
    }

    override fun initViews() {
        val viewBar = rootView.findViewById<View>(R.id.main_t4_view_bar)
        mMainActivity?.setStateBarHeight(main_t4_view_bar, 1)
        mLoveEngine = LoveEngine(mMainActivity)
//        mProgressBar = rootView.findViewById(R.id.main_t4_pb_progress)
//        mWebView = rootView.findViewById(R.id.main_t4_webview)
        //        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String) {
        main_t4_webview.isClickable = true
        val settings = main_t4_webview.settings
        settings.useWideViewPort = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //        settings.setDefaultTextEncodingName("gb2312");
        settings.saveFormData = true
        settings.domStorageEnabled = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setAppCacheEnabled(true)
        settings.loadWithOverviewMode = true
        settings.databaseEnabled = true
        main_t4_webview.addJavascriptInterface(JsInterface(), "android")
        settings.loadsImagesAutomatically = true
        main_t4_webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                //首先我们的进度条是隐藏的
                main_t4_pb_progress.visibility = View.VISIBLE //把网页加载的进度传给我们的进度条
                main_t4_pb_progress.progress = newProgress
                if (newProgress >= 95) { //加载完毕让进度条消失
                    if (main_t4_pb_progress.visibility != View.GONE) {
                        main_t4_pb_progress.visibility = View.GONE
                    }
                }
                super.onProgressChanged(view, newProgress)
            } /*@Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                Log.d("mylog", "onReceivedIcon: icon " + icon);
                super.onReceivedIcon(view, icon);
            }*/
        }
        mMainActivity?.setOnChildDisposeMainKeyDownListent(object : OnChildDisposeMainKeyDownListent {
            private var mIsCanBack = false
            override fun onChildDisposeMainKeyDown(): Boolean {
                Log.d("mylog", "onChildDisposeMainKeyDown: mWebView.canGoBack " + main_t4_webview.canGoBack())
                return if (main_t4_webview.canGoBack()) {
                    main_t4_webview.goBack()
                    true
                } else {
                    Log.d("mylog", "onChildDisposeMainKeyDown: mIsCanBack $mIsCanBack")
                    if (mIsCanBack) {
                        return false
                    }
                    main_t4_webview.loadUrl(homeUrl)
                    mIsCanBack = true
                    main_t4_webview.postDelayed({ mIsCanBack = false }, 2500)
                    true
                }
            }
        })
        main_t4_webview.loadUrl(url)
        /**
         * VOVO 8.0手机莫名闪退
         */
        main_t4_webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        main_t4_webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_WELFEAR_ID)
        netData()
    }

    private val cache: Unit
        private get() {
            CommonInfoHelper.getO(mMainActivity, "main4_menuadv_info", object : TypeReference<MenuadvInfoBean?>() {}.type,
                    object : OnParseListener<MenuadvInfoBean> {
                        override fun onParse(o: MenuadvInfoBean?) {
                            if (o != null) {
                                loadWebViewData(o)
                                mLoadDialog?.dismissLoadingDialog()
                            }
                        }
//
                    })
        }

    private fun netData() {
        mLoadDialog = LoadDialog(mMainActivity)
        mLoadDialog?.showLoadingDialog()
        mLoveEngine?.menuadvInfo("menuadv/info")?.subscribe(object : Subscriber<AResultInfo<MenuadvInfoBean>?>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
//                getCache();
                mLoadDialog?.dismissLoadingDialog()
            }

            override fun onNext(menuadvInfoBeanAResultInfo: AResultInfo<MenuadvInfoBean>?) {
                mLoadDialog?.dismissLoadingDialog()
                if (menuadvInfoBeanAResultInfo != null && menuadvInfoBeanAResultInfo.code == HttpConfig.STATUS_OK && menuadvInfoBeanAResultInfo.data != null) {
                    val menuadvInfoBean = menuadvInfoBeanAResultInfo.data

                    //不需要重复加载
                    loadWebViewData(menuadvInfoBean)
                    //                    mCacheWorker.setCache("main4_menuadv_info", menuadvInfoBean);
                    CommonInfoHelper.setO(mMainActivity, menuadvInfoBean, "main4_menuadv_info")
                }
            }


        })
    }

    private fun loadWebViewData(menuadvInfoBean: MenuadvInfoBean) {
        val url = menuadvInfoBean.url
        mWechat = menuadvInfoBean.wechat
        //                url = "http://en.upkao.com";
        //        String url = "https://fir.im/cloudreader";
        Log.d("mylog", "onNetNext: url $url")
        //        mWebView.loadUrl(url);

        initWebView(url)
        homeUrl = url
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyWebView()
    }

    fun destroyWebView() {
        main_t4_pb_progress.clearAnimation()
        main_t4_webview.clearHistory()
        main_t4_webview.clearCache(true)
        main_t4_webview.loadUrl("about:blank") // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
        main_t4_webview.destroy()
    }

    inner class JsInterface {
        //JS交互
        @JavascriptInterface
        fun toNext() {
            MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_WECHAT_ID)
            Log.d("mylog", "toNext: ---------------")
            //            mMainActivity.showToastShort("123456");
            if (!TextUtils.isEmpty(mWechat)) {
                val myClipboard = mMainActivity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip = ClipData.newPlainText("text", mWechat)
                myClipboard.primaryClip = myClip
                //                mMainActivity.showToastShort("微信号已复制到剪切板");
                mMainActivity?.let {
                    val alertDialog = AlertDialog.Builder(it).create()
                    alertDialog.setMessage("微信号已复制到剪切板,去添加好友吧")
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定") { dialogInterface: DialogInterface?, i: Int -> openWeiXin() }
                    val listent: DialogInterface.OnClickListener? = null
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent)
                    alertDialog.show()
                }

            }
        }

        private fun openWeiXin() {
            try {
                val intent = Intent()
                val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.component = cmp
                mMainActivity?.startActivity(intent)
            } catch (e: Exception) {
                val names = arrayOf("b", "a", "aa")
                mMainActivity?.showToast("未安装微信", duration = Toast.LENGTH_SHORT, test = *names)


            }
        }
    }


}