package com.yc.emotion.home.base.ui.widget

import android.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.content.ContextCompat

/**
 * Created by wanglin  on 2018/10/8 14:32.
 */
class CommonWebView : WebView {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val webSettings = settings
        setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true // 解决对某些标签的不支持出现白屏
        webSettings.setNeedInitialFocus(false)
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = false
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)
        webSettings.databaseEnabled = true
        val appCaceDir = context.getDir("cache", Context.MODE_PRIVATE).path
        webSettings.setAppCachePath(appCaceDir)


        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

//        wvMain.addJavascriptInterface(new NewsDetailActivity.JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存 //优先使用缓存:
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        webSettings.blockNetworkImage = false //设置是否加载网络图片 true 为不加载 false 为加载

//       removeJavascriptInterface("searchBoxJavaBridge_");
//        // 加载https网页包含http链接的资源，必须要加这句，否则图片资源加载不出来
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        setDownloadListener { url: String, userAgent: String?, contentDisposition: String, mimetype: String, contentLength: Long ->
            //在这里进行下载的处理。
            // 如果你没有进行处理，一般APP就不会开始下载行为，在这里可以自己开启一个线程来下载
            Log.i("download", "url: $url")
            Log.i("download", "contentDisposition: $contentDisposition")
            Log.i("download", "mimetype: $mimetype")
            /**
             * 通过系统下载apk
             */
            /**
             * 通过系统下载apk
             */
            if (url.endsWith(".apk") || contentDisposition.endsWith(".apk")) {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }
        setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
                    goBack()
                    return@setOnKeyListener true
                }
            }
            false
        }
        webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                Log.e(TAG, "onReceivedTitle: $title")
            }
        }
    }

    companion object {
        private const val TAG = "CommonWebView"
    }
}