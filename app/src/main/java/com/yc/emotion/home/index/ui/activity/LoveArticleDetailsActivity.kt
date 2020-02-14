package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import com.kk.utils.LogUtil
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.presenter.SkillPresenter
import com.yc.emotion.home.index.view.SkillView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_love_by_stages_details.*
import kotlinx.android.synthetic.main.layout_bottom_wechat.*

/**
 * 秘技文章详情
 */

class LoveArticleDetailsActivity : BaseSameActivity(), SkillView {


    private var mId: Int = 0
    private var mPostTitle: String? = null
    private var mIsCollectArticle = false
    private var mIsDigArticle: Boolean = false
    private var mCategoryId: Int = 0


    override fun initIntentData() {
        val intent = intent
        mId = intent.getIntExtra("love_id", -1)
        mPostTitle = intent.getStringExtra("post_title")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_love_by_stages_details
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.activity_base_same_tv_sub -> netCollectArticle(mIsCollectArticle)
            R.id.love_by_stages_details_iv_like -> netDigArticle(mIsDigArticle)
            R.id.ll_get_wechat -> showToWxServiceDialog(exampleId = "$mId")
            R.id.ll_collect -> netCollectArticle(mIsCollectArticle)
        }
    }

    private fun netDigArticle(isDigArticle: Boolean) {


        if (!UserInfoHelper.instance.goToLogin(this)) {

            (mPresenter as? SkillPresenter)?.digSkillArticle("$mCategoryId", isDigArticle)

        }
    }

    private fun netCollectArticle(isCollectArticle: Boolean) {


        if (!UserInfoHelper.instance.goToLogin(this)) {

            (mPresenter as? SkillPresenter)?.collectSkillArticle("$mCategoryId", isCollectArticle)

        }
    }

    override fun initViews() {
        mPresenter = SkillPresenter(this, this)



        love_by_stages_details_tv_name.text = mPostTitle
        love_by_stages_details_iv_like.setOnClickListener(this)

        ll_get_wechat.setOnClickListener(this)
        ll_collect.setOnClickListener(this)


        love_by_stages_details_scroll_view.setOnScrollChangeListener { l, t, oldl, oldt ->
            if (t > love_by_stages_details_ll_title_con.measuredHeight) {
                setBarTitle(mPostTitle)
                //                    mToolbar.setTitle(title);
            } else {
                setBarTitle("问答")
                //                    mToolbar.setTitle("");
            }
        }
        netData()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = rlBottom.layoutParams as ConstraintLayout.LayoutParams

        val bottomLayoutParams = ll_bottom_container.layoutParams as LinearLayout.LayoutParams
        var bottom = 0
        var llBottom = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
            llBottom = SizeUtils.dp2px(this, 40f)
        }
        layoutParams.bottomMargin = bottom
        rlBottom.layoutParams = layoutParams

        bottomLayoutParams.bottomMargin = llBottom
        ll_bottom_container.layoutParams = bottomLayoutParams

    }


    private fun initWebView(data: String) {
        var data = data
        val settings = love_by_stages_details_webview.settings


        love_by_stages_details_webview.addJavascriptInterface(MyJavaScript(), "android")
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局

        data = formatting(data)


        love_by_stages_details_webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)


        love_by_stages_details_webview.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //                javascript:window.android.resize(document.body.getBoundingClientRect().height)
                love_by_stages_details_webview.loadUrl("javascript:window.android.resize(document.body.getScrollHeight())")
            }
        }
        love_by_stages_details_webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                //首先我们的进度条是隐藏的
                love_by_stages_details_pb_progress.visibility = View.VISIBLE//把网页加载的进度传给我们的进度条
                love_by_stages_details_pb_progress.progress = newProgress
                if (newProgress >= 95) { //加载完毕让进度条消失
                    if (love_by_stages_details_pb_progress.visibility != View.GONE) {
                        love_by_stages_details_pb_progress.visibility = View.GONE
                    }
                    if (love_by_stages_details_cl_like_con.visibility != View.VISIBLE) {
                        love_by_stages_details_cl_like_con.visibility = View.VISIBLE
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }


        love_by_stages_details_webview.setOnLongClickListener { v ->
            // 长按事件监听（注意：需要实现LongClickCallBack接口并传入对象）

            val htr = love_by_stages_details_webview.hitTestResult//获取所点击的内容
            if (htr.type == WebView.HitTestResult.IMAGE_TYPE
                    || htr.type == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                    || htr.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                //判断被点击的类型为图片

                //                    showQRCodeDialog(htr.getExtra());

            }

            LogUtil.msg("url: " + htr.extra)

            false
        }
    }


    private fun netData() {


        (mPresenter as? SkillPresenter)?.detailArticle("$mId")

    }

    private fun changCollectStaty(collectNum: Int, isCollectArticle: Boolean, isClick: Boolean) {
        var collEctResult = collectNum

        if (collEctResult < 0) {
            collEctResult = Integer.parseInt(tv_collect_count.text.toString().trim { it <= ' ' })
        }

        if (isCollectArticle) {
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
            love_by_stages_details_iv_like.setImageDrawable(resources.getDrawable(R.mipmap.icon_like_s))
        } else {
            love_by_stages_details_iv_like.setImageDrawable(resources.getDrawable(R.mipmap.icon_like_gray))
        }
    }

    override fun offerActivityTitle(): String {
        return "问答"
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyWebView()
    }

    private fun destroyWebView() {
        if (love_by_stages_details_pb_progress != null) {
            love_by_stages_details_pb_progress.clearAnimation()
        }
        if (love_by_stages_details_webview != null) {
            love_by_stages_details_webview.clearHistory()
            love_by_stages_details_webview.clearCache(true)
            love_by_stages_details_webview.loadUrl("about:blank") // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
            love_by_stages_details_webview.destroy()
        }
    }


    inner class MyJavaScript {
        @JavascriptInterface
        fun resize(height: Float) {
            //            Log.e("TAG", "resize: " + height);
            runOnUiThread {
                val layoutParams = LinearLayout.LayoutParams(resources.displayMetrics.widthPixels - SizeUtils.dp2px(this@LoveArticleDetailsActivity, 12f), (height * resources.displayMetrics.density).toInt())
                layoutParams.leftMargin = SizeUtils.dp2px(this@LoveArticleDetailsActivity, 12f)

                love_by_stages_details_webview.layoutParams = layoutParams
            }
        }
    }

    override fun showSkillArticleDetailInfo(data: LoveByStagesDetailsBean?) {
        data?.let {


            //收藏
            val isCollect = data.is_collect
            if (isCollect == 1) mIsCollectArticle = true

            changCollectStaty(data.collect_num, mIsCollectArticle, false)
            mBaseSameTvSub.setOnClickListener(this@LoveArticleDetailsActivity)

            //点赞
            val isLike = data.is_like
            if (isLike == 1) mIsDigArticle = true

            changLikeStaty(mIsDigArticle)
            //                mClLikeCon.setVisibility(View.VISIBLE);

            mCategoryId = data.id

            val postContent = data.post_content
            initWebView(postContent)
        }
    }

    override fun showSkillArticleCollectResult(num: Int, collectArticle: Boolean) {
        mIsCollectArticle = collectArticle
        changCollectStaty(num, collectArticle, mIsCollectArticle)
    }

    override fun showSkillArticleDigResult(digArticle: Boolean) {
        mIsDigArticle = digArticle
        changLikeStaty(digArticle)
    }


    companion object {

        fun startLoveByStagesDetailsActivity(context: Context?, id: Int, postTitle: String) {
            val intent = Intent(context, LoveArticleDetailsActivity::class.java)
            intent.putExtra("love_id", id)
            intent.putExtra("post_title", postTitle)
            context?.startActivity(intent)
        }
    }
}
