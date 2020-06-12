package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.index.presenter.LoveCaseDetailPresenter
import com.yc.emotion.home.index.view.LoveCaseDetailView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_example_detail.*
import kotlinx.android.synthetic.main.layout_bottom_wechat.*

/**
 * 恋爱实例详情页 Maint2 click to me
 */

class LoveCaseDetailActivity : BaseSameActivity(), LoveCaseDetailView {


    private var mActivityTitle: String? = null

    private var mId: Int = 0
    private var mIsCollectLovewords = false
    private var mDetailId: Int = 0
    private var mIsDigArticle: Boolean = false


    override fun initIntentData() {
        val intent = intent
        mId = intent.getIntExtra("id", -1)
        mActivityTitle = intent.getStringExtra("title")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_example_detail
    }

    override fun initViews() {

        mPresenter = LoveCaseDetailPresenter(this, this)

        netData()
        mBaseSameTvSub.setOnClickListener(this)
        example_detail_iv_like.setOnClickListener(this)
        ll_get_wechat.setOnClickListener(this)
        ll_collect.setOnClickListener(this)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = rlBottom.layoutParams as ConstraintLayout.LayoutParams

        val scroolLayoutParams = example_detail_scroll_view.layoutParams as ConstraintLayout.LayoutParams
        var marginBottom = 0
        var bottom = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
            marginBottom = SizeUtils.dp2px(this, 30f)
        }

        layoutParams.bottomMargin = bottom
        rlBottom.layoutParams = layoutParams
        scroolLayoutParams.bottomMargin = marginBottom
        example_detail_scroll_view.layoutParams = scroolLayoutParams
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.example_detail_iv_like -> digExample(mIsDigArticle)
            R.id.activity_base_same_tv_sub, R.id.ll_collect -> {

                if (!UserInfoHelper.instance.goToLogin(this))
                    netCollect(mIsCollectLovewords)
            }
            R.id.ll_get_wechat -> {
                MobclickAgent.onEvent(this, "chat_practice_id", "聊天实战微信添加")
                showToWxServiceDialog(exampleId = "$mId")
            }
        }
    }


    private fun initWebView(data: String) {
//        var data = data

        val settings = example_detail_webview.settings

        example_detail_webview.addJavascriptInterface(AndroidJavaScript(), "android")//设置js接口
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局
//        data = formatting(data)

        example_detail_webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)


        example_detail_webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //                webView.loadUrl("javascript:window.android.resize(document.body.getBoundingClientRect().height)");
                example_detail_webview.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())")
            }
        }

        example_detail_webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                //首先我们的进度条是隐藏的
                example_detail_pb_progress.visibility = View.VISIBLE//把网页加载的进度传给我们的进度条
                example_detail_pb_progress.progress = newProgress

                if (newProgress >= 95) { //加载完毕让进度条消失
                    if (example_detail_pb_progress.visibility != View.GONE) {
                        example_detail_pb_progress.visibility = View.GONE
                    }
                    if (example_detail_cl_like_con.visibility != View.VISIBLE) {
                        example_detail_cl_like_con.visibility = View.VISIBLE
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }

    }


    private inner class AndroidJavaScript {


        @JavascriptInterface
        fun returnAndroid(name: String) {//从网页跳回到APP，这个方法已经在上面的HTML中写上了
            if (name.isEmpty() || name == "") {
                return
            }
            Toast.makeText(application, name, Toast.LENGTH_SHORT).show()
            //这里写你的操作///////////////////////
            //MainActivity就是一个空页面，不影响
            val intent = Intent(this@LoveCaseDetailActivity, MainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }

        @JavascriptInterface
        fun resize(height: Float) {
            //            Log.e("TAG", "resize: " + height);
            runOnUiThread {
                val layoutParams = RelativeLayout.LayoutParams(resources.displayMetrics.widthPixels - SizeUtils.dp2px(this@LoveCaseDetailActivity, 10f), (height * resources.displayMetrics.density).toInt())
                layoutParams.leftMargin = SizeUtils.dp2px(this@LoveCaseDetailActivity, 12f)
                //                layoutParams.rightMargin = SizeUtils.dp2px(LoveCaseDetailActivity.this, 10f);
                example_detail_webview.layoutParams = layoutParams
            }
        }


    }


    private fun netData() {
        (mPresenter as? LoveCaseDetailPresenter)?.detailLoveCase("$mId")

    }


    private fun digExample(isDigArticle: Boolean) {
        if (!UserInfoHelper.instance.goToLogin(this)) {

            (mPresenter as? LoveCaseDetailPresenter)?.digLoveCase("$mDetailId", isDigArticle)

        }
    }

    private fun netCollect(isCollect: Boolean) {


        (mPresenter as? LoveCaseDetailPresenter)?.collectLoveCase("$mDetailId", isCollect)


    }

    private fun changSubImg(collEctResult: Int, isCollect: Boolean, isClick: Boolean) {
        var collEctResult = collEctResult

        if (collEctResult < 0) {
            collEctResult = Integer.parseInt(tv_collect_count.text.toString().trim { it <= ' ' })
        }

        if (isCollect) {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0)
            iv_collect.setImageResource(R.mipmap.search_knack_collected_icon)
            if (isClick) {
                collEctResult += 1
            }

        } else {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0)
            iv_collect.setImageResource(R.mipmap.search_knack_collect_icon)
            if (isClick) {
                collEctResult -= 1
            }
        }

        var result = collEctResult.toString()
        if (collEctResult >= 99) {
            result = "99"
        } else if (collEctResult <= 1) {
            result = "1"
        }
        tv_collect_count.text = result
    }


    private fun changLikeStaty(isLikeArticle: Boolean) {
        if (isLikeArticle) {
            example_detail_iv_like.setImageDrawable(resources.getDrawable(R.mipmap.icon_like_s))
        } else {
            example_detail_iv_like.setImageDrawable(resources.getDrawable(R.mipmap.icon_like_gray))
        }
    }

    override fun showLoveCaseDetail(data: LoveByStagesDetailsBean?) {

        data?.let {
            val postContent = data.post_content
            initWebView(postContent)

            val collectNum = data.collect_num

            mDetailId = data.id

            val isCollect = data.is_collect
            if (isCollect > 0) { //是否收藏
                mIsCollectLovewords = true
            }

            changSubImg(collectNum, mIsCollectLovewords, false)

            //点赞
            val isLike = data.is_like
            when (isLike) {
                0 -> {
                }
                1 -> mIsDigArticle = true
            }
            changLikeStaty(mIsDigArticle)
        }

    }

    override fun showLoveCaseCollectResult(i: Int, collected: Boolean) {
        mIsCollectLovewords = collected
        changSubImg(i, collected, true)
    }


    override fun showLoveCaseDigResult(dig: Boolean) {
        mIsDigArticle = dig
        changLikeStaty(dig)
    }

    override fun offerActivityTitle(): String? {
        return mActivityTitle
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyWebView()
    }

    private fun destroyWebView() {
        if (example_detail_pb_progress != null) {
            example_detail_pb_progress.clearAnimation()
        }
        if (example_detail_webview != null) {
            example_detail_webview.clearHistory()
            example_detail_webview.clearCache(true)
            example_detail_webview.loadUrl("about:blank") // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
            example_detail_webview.destroy()
        }
    }

    companion object {


        fun startExampleDetailActivity(context: Context?, id: Int, postTitle: String) {
            val intent = Intent(context, LoveCaseDetailActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", postTitle)
            context?.startActivity(intent)
        }
    }
}
