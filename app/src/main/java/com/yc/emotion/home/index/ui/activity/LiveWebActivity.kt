package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import kotlinx.android.synthetic.main.activity_live_web.*

class LiveWebActivity : BaseSameActivity() {
    var liveUrl = "https://m.douyu.com/8247567?fromuid=347698042&share_source=4&from=singlemessage"

    companion object {
        fun startActivity(context: Context?, imgUrl: String) {
            val intent = Intent(context, LiveWebActivity::class.java)
            intent.putExtra("img", imgUrl)

            context?.startActivity(intent)
        }
    }

    override fun offerActivityTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_live_web
    }

    override fun initViews() {
        Log.e("TAG", "initViews")


        if (intent != null) {
            liveUrl = intent.getStringExtra("img")
        }

        val settings = webView_live.settings

        settings.loadWithOverviewMode = true//设置WebView是否使用预览模式加载界面。
        webView_live.isVerticalScrollBarEnabled = false//不能垂直滑动
        webView_live.isHorizontalScrollBarEnabled = false//不能水平滑动

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局
        webView_live.loadUrl(liveUrl)

    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }
}