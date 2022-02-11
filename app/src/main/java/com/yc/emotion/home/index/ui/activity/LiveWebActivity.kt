package com.yc.emotion.home.index.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.danikula.videocache.HttpProxyCacheServer
import com.video.player.lib.constants.VideoConstants
import com.video.player.lib.manager.VideoPlayerManager
import com.video.player.lib.manager.VideoWindowManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.EmApplication
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment
import com.yc.emotion.home.model.bean.LessonInfo
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_live_web.*
import kotlinx.android.synthetic.main.activity_live_web.videoPlayer


class LiveWebActivity : BaseSameActivity() {
    private var liveUrl = "https://m.douyu.com/8247567?fromuid=347698042&share_source=4&from=singlemessage"
    private var wx = "pai201807"
    private var isLive = true//是否直播
    private var cover = ""

    companion object {
        fun startActivity(context: Context?, imgUrl: String?, cover: String?, wx: String?) {
            val intent = Intent(context, LiveWebActivity::class.java)
            intent.putExtra("img", imgUrl)
            intent.putExtra("wx", wx)
            intent.putExtra("cover", cover)

            context?.startActivity(intent)
        }
    }

    override fun offerActivityTitle(): String {
        return "直播"
    }

    override fun hindActivityBar(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
//        invadeStatusBar() //侵入状态栏
//        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_live_web
    }

    override fun initViews() {
        //"https://www.douyu.com/room/share/8247567"
        if (intent != null) {
            liveUrl = intent.getStringExtra("img")
            if (intent.hasExtra("wx"))
                wx = intent.getStringExtra("wx")
            cover = intent.getStringExtra("cover")
        }

//        liveUrl="https://www.douyu.com/6266000"

        if (liveUrl.endsWith("mp4") || liveUrl.endsWith("flv")) {
            isLive = false
        }

        if (isLive) {
            videoPlayer.visibility = View.GONE
            webView_live.visibility = View.VISIBLE
            initWebView()

        } else {
            videoPlayer.visibility = View.VISIBLE
            webView_live.visibility = View.GONE
            startPlayer()
        }





        live_to_wx.clickWithTrigger {
            val addWxFragment = AddWxFragment()
            addWxFragment.setWX(wx)
            addWxFragment.setListener(object : AddWxFragment.OnToWxListener {
                override fun onToWx() {
                    val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val myClip = ClipData.newPlainText("text", wx)
                    myClipboard.setPrimaryClip(myClip)
                    openWeiXin()
                }

            })
            addWxFragment.show(supportFragmentManager, "")
        }
    }

    private fun initWebView() {
        val settings = webView_live.settings

        settings.loadWithOverviewMode = true//设置WebView是否使用预览模式加载界面。
        webView_live.isVerticalScrollBarEnabled = false//不能垂直滑动
        webView_live.isHorizontalScrollBarEnabled = false//不能水平滑动

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局
        settings.javaScriptCanOpenWindowsAutomatically = true
        webView_live.webViewClient = object : WebViewClient() {
            //
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null) return false

                try {
                    if (!url.startsWith("https://") || !url.startsWith("http://")) //支付//微信
                    //其他自定义的scheme
                    {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                view?.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {

                //证书错误
                handler.proceed()
                //1.用户选择继续加载
                // handler.proceed();
                //2.用户取消
                //handler.cancel()
//                super.onReceivedSslError(view, handler, error)
            }
        }
//
        webView_live.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                live_progress.progress = newProgress
                if (newProgress == 100) {
                    live_progress.visibility = View.GONE
                }
            }
        }
        webView_live.loadUrl(liveUrl)
    }


    private fun startPlayer() {
        val proxy: HttpProxyCacheServer? = EmApplication.instance.getProxy()
        proxy?.let {
            liveUrl = proxy.getProxyUrl(liveUrl)
        }

        Glide.with(this).load(cover).apply(RequestOptions().error(R.mipmap.efficient_course_example_pic)).into(videoPlayer.coverController.mVideoCover)
        videoPlayer.setVideoDisplayType(VideoConstants.VIDEO_DISPLAY_TYPE_ZOOM)
        videoPlayer.startPlayVideo(liveUrl, "")
    }


    override fun onResume() {
        super.onResume()
        VideoPlayerManager.getInstance().onResume()
    }

    override fun onPause() {
        super.onPause()
        VideoPlayerManager.getInstance().onPause()
    }

    override fun onBackPressed() {
        //尝试返回
        if (VideoPlayerManager.getInstance().isBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VideoPlayerManager.getInstance().onDestroy()
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy()
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}