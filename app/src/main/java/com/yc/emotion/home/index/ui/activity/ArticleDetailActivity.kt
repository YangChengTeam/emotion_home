package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.webkit.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.index.presenter.ArticleDetailPresenter
import com.yc.emotion.home.index.view.ArticleDetailView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_example_detail.*
import kotlinx.android.synthetic.main.layout_bottom_wechat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 文章详情页
 */

class ArticleDetailActivity : BaseSameActivity(), ArticleDetailView {


    private var mActivityTitle: String? = null

    private var mId: Int = 0
    private var mIsCollectLovewords = false
    private var mDetailId: Int = 0
    private var mIsDigArticle: Boolean = false

    private var isShowTint = true

    override fun initIntentData() {
        val intent = intent
        mId = intent.getIntExtra("id", -1)
        mActivityTitle = intent.getStringExtra("title")
        isShowTint = intent.getBooleanExtra("isShowTint", true)
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
//        mTvTitle.setText()
        mPresenter = ArticleDetailPresenter(this, this)

        netData()
        tv_article_tint.visibility = if (isShowTint) View.VISIBLE else View.GONE
        initListener()
    }

    fun initListener() {
        mBaseSameTvSub.setOnClickListener(this)

        example_detail_iv_like.setOnClickListener(this)
        ll_get_wechat.setOnClickListener(this)
        ll_collect.setOnClickListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = rlBottom.layoutParams as ConstraintLayout.LayoutParams

        val scrollLayoutParams = example_detail_scroll_view.layoutParams as ConstraintLayout.LayoutParams

        var bottom = 0
        var marginBottom = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
            marginBottom = SizeUtils.dp2px(this, 30f)
        }

        layoutParams.bottomMargin = bottom
        rlBottom.layoutParams = layoutParams

        scrollLayoutParams.bottomMargin = marginBottom
        example_detail_scroll_view.layoutParams = scrollLayoutParams
    }

    override fun onClick(v: View) {
        super.onClick(v)

        when (v.id) {
            R.id.example_detail_iv_like -> {
                if (!UserInfoHelper.instance.goToLogin(this))
                    netExample(mIsDigArticle)
            }
            R.id.activity_base_same_tv_sub, R.id.ll_collect -> {

                if (!UserInfoHelper.instance.goToLogin(this))
                    netCollect()
            }
            R.id.ll_get_wechat -> {
                showToWxServiceDialog(articelId = "$mId")
                MobclickAgent.onEvent(this, "preferred_article_id", "优选文章微信添加")
            }
        }
    }

    //点赞
    private fun netExample(isDigArticle: Boolean) {

        (mPresenter as? ArticleDetailPresenter)?.articleDig("$mDetailId", isDigArticle)

    }

    private fun initWebView(data: String) {
        val data = data

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventLoginState) {
        when (event.state) {

            EventLoginState.STATE_LOGINED ->

                netData()

        }
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
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
            val intent = Intent(this@ArticleDetailActivity, MainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }

        @JavascriptInterface
        fun resize(height: Float) {
            //            Log.e("TAG", "resize: " + height);
            runOnUiThread {
                val layoutParams = RelativeLayout.LayoutParams(resources.displayMetrics.widthPixels - SizeUtils.dp2px(this@ArticleDetailActivity, 10f), (height * resources.displayMetrics.density).toInt())
                layoutParams.leftMargin = SizeUtils.dp2px(this@ArticleDetailActivity, 12f)
                //                layoutParams.rightMargin = SizeUtils.dp2px(LoveCaseDetailActivity.this, 10f);
                example_detail_webview.layoutParams = layoutParams
            }
        }


    }


    private fun netData() {

        (mPresenter as? ArticleDetailPresenter)?.getArticleDetai("$mId")

    }

    //收藏
    private fun netCollect() {
        (mPresenter as? ArticleDetailPresenter)?.articleCollect("$mDetailId", mIsCollectLovewords)

    }

    private fun changSubImg(collEctResult: Int, isCollectLovewords: Boolean, isClick: Boolean) {
        var collEctResult = collEctResult
        mIsCollectLovewords = isCollectLovewords

        if (collEctResult < 0) {
            collEctResult = Integer.parseInt(tv_collect_count.text.toString().trim { it <= ' ' })
        }

        if (isCollectLovewords) {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0)
            iv_collect.setImageResource(R.mipmap.search_knack_collected_icon)
            tv_collect.setTextColor(ContextCompat.getColor(this,R.color.gray_999))
            if (isClick) {
                collEctResult += 1
            }

        } else {
            mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0)
            iv_collect.setImageResource(R.mipmap.search_knack_collect_icon)

            tv_collect.setTextColor(ContextCompat.getColor(this,R.color.black))
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

    override fun showArticleDetailInfo(data: LoveByStagesDetailsBean?) {
        data?.let {

            val postContent = data.post_content
            initWebView(postContent)
            val collectNum = data.collect_num

            mDetailId = data.id

            //收藏
            val isCollect = data.is_collect
            if (isCollect > 0) { //是否收藏
                mIsCollectLovewords = true
            }
            changSubImg(collectNum, mIsCollectLovewords, false)

            //点赞
            val isLike = data.is_like
            if (isLike == 1) {
                mIsDigArticle = true
            }
            changLikeStaty(mIsDigArticle)
        }
    }

    override fun digArticle(digArticle: Boolean) {
        mIsDigArticle = digArticle
        changLikeStaty(digArticle)
    }

    override fun collectArticle(i: Int, collected: Boolean) {
        changSubImg(-1, collected, true)
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


        fun startExampleDetailActivity(context: Context?, id: Int?, postTitle: String?, isShowTint: Boolean = true) {
            val intent = Intent(context, ArticleDetailActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", postTitle)
            intent.putExtra("isShowTint", isShowTint)
            context?.startActivity(intent)
        }
    }
}
