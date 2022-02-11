package com.yc.emotion.home.base.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.yc.emotion.home.R
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.common_topbar_view.*
import yc.com.rthttplibrary.util.LogUtil
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Pattern

/**
 * Created by wanglin  on 2018/3/15 15:56.
 */
class WebActivity : BaseActivity() {

    private var url: String? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invadeStatusBar()
        setContentView(getLayoutId())

        initViews()
    }


    override fun initViews() {

        url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        activity_base_same_tv_title.text = title
        activity_base_same_iv_back.clickWithTrigger {
            finish()
        }

        initWebView(url)
    }

    private var isGoback = false
    private fun initWebView(url: String?) {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

//        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存 //优先使用缓存:
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        webSettings.blockNetworkImage = false //设置是否加载网络图片 true 为不加载 false 为加载
        webView.loadUrl(url)
        progressBar.max = 100
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (!isGoback) {
                    progressBar.progress = newProgress
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (progressBar.visibility == View.VISIBLE) {
                    progressBar.visibility = View.GONE
                }
                isGoback = false
            }


            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    val url1 = URL(url)
                    val req = "/zk/\\d+.html"
                    val compile = Pattern.compile(req) //http://m.upkao.com/zk/index.html
                    val matcher = compile.matcher(url1.path)
                    if (matcher.matches()) {
//                        isShowLoading = true;
                        LogUtil.msg("TAG: " + url + "--" + url1.path)
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                }
                view.loadUrl(url)
                return false
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//                super.onReceivedSslError(view, handler, error);
                Log.e("TAG", "onReceivedSslError: " + error.url)
                handler.proceed() //接受证书
            }
        }
        webView.setDownloadListener { url12, userAgent, contentDisposition, mimetype, contentLength ->
            //在这里进行下载的处理。
            // 如果你没有进行处理，一般APP就不会开始下载行为，在这里可以自己开启一个线程来下载
            Log.i("download", "url: $url12")
            Log.i("download", "contentDisposition: $contentDisposition")
            Log.i("download", "mimetype: $mimetype")
            /**
             * 通过系统下载apk
             */
            if (url12.endsWith(".apk") || contentDisposition.endsWith(".apk")) {
                val uri = Uri.parse(url12)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (!TextUtils.isEmpty(url) && url.startsWith("http://m.upkao.com/zk/")) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            isGoback = true
            return true
        }
        //        }
        return super.onKeyDown(keyCode, event)
    }


    companion object {
        @JvmStatic
        fun startActivity(context: Context, url: String?, title: String?) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }
}