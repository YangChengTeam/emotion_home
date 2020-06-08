package com.yc.emotion.home.index.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.music.player.lib.bean.MusicInfo
import com.music.player.lib.constants.Constants
import com.music.player.lib.listener.OnUserPlayerEventListener
import com.music.player.lib.manager.MusicPlayerManager
import com.music.player.lib.mode.PlayerStatus
import com.music.player.lib.view.MusicPlayerController
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.presenter.LoveAudioPresenter
import com.yc.emotion.home.index.view.LoveAudioView
import com.yc.emotion.home.model.util.SizeUtils
import com.yc.emotion.home.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_audio_detail.*
import kotlinx.android.synthetic.main.layout_bottom_wechat.*


/**
 * Created by admin on 2018/1/26.
 */

class LoveAudioDetailActivity : BaseSameActivity(), OnUserPlayerEventListener, LoveAudioView {


    private var typeId: String? = null

    private var isToTop = false
    private var mScaledTouchSlop: Int = 0//滑动的最小距离
    private var containerTop: Int = 0
    private var nestTop: Int = 0


    private var startY: Int = 0


    private var musicInfo: MusicInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_detail)
        initViews()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_audio_detail
    }


    override fun offerActivityTitle(): String {
        return getString(R.string.audio)
    }


    fun init() {

        mPresenter = LoveAudioPresenter(this,this)
        val intent = intent
        if (intent != null) {
            typeId = intent.getStringExtra("type_id")

        }


        top_container.post {
            containerTop = top_container.top
            nestTop = nestedScrollView.top

            Log.e("TAG", "init: $containerTop---$nestTop")
        }


        MobclickAgent.onEvent(this, "audio_frequency_id", "音频播放")

        getData()
        initAdapter()
        initListener()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = rlBottom.layoutParams as ConstraintLayout.LayoutParams
        var bottomHeight = 0
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottomHeight = StatusBarUtil.getNavigationBarHeight(this)
        }
        layoutParams.bottomMargin = bottomHeight
        rlBottom.layoutParams = layoutParams


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {

        nestedScrollView.setOnTouchListener { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> startY = event.rawY.toInt()

                MotionEvent.ACTION_UP -> {
                    val deltaY = (event.rawY - startY).toInt()
                    //                    startY = (int) event.getY();
                    if (Math.abs(deltaY) >= mScaledTouchSlop && isToTop) {
                        forwardTop()
                    } else if (Math.abs(deltaY) >= mScaledTouchSlop && !isToTop) {
                        forwardTopBottom()
                    }
                }
            }//                    Log.e("TAG", "initListener: " + deltaY + "--startY--" + startY + "--endY--" + event.getRawY());
            false
        }
        nestedScrollView.setOnScrollListener { l, t, oldl, oldt ->
            //            Log.e("TAG", "initListener: " + t + "--oldt--" + oldt);
            isToTop = t > oldt

        }

        ll_get_wechat.setOnClickListener(this)
        ll_collect.setOnClickListener(this)

    }

    private fun forwardTopBottom() {
        top_container.visibility = View.VISIBLE
        //        nestedScrollView.smoothScrollTo(0, nestTop);

    }

    private fun forwardTop() {
        top_container.visibility = View.GONE
        //        nestedScrollView.smoothScrollTo(0, containerTop);

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_get_wechat -> showToWxServiceDialog()
            R.id.ll_collect -> collectAudio(musicInfo)
        }
    }

    /**
     * 初始设置示例
     */
    override fun initViews() {
        invadeStatusBar()
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        init()
        mScaledTouchSlop = ViewConfiguration.get(this).scaledTouchSlop


        //设置播放器样式，不设置默认首页样式，这里以黑色为例

        //调用此方法目的在于当播放列表为空，会回调至持有播放控制器的所有UI组件，设置Type就是标识UI组件的身份，用来判断是是否处理 回调方法事件autoStartNewPlayTasks()，
        //参数可自定义，需要和回调的autoStartNewPlayTasks（type）对应,
        //        mMusicPlayerController.setUIComponentType(Constants.UI_TYPE_DETAILS);

        //设置闹钟最大定时时间
        music_player_controller.setAlarmSeekBarProgressMax(1000)
        //设置闹钟初始的定时时间
        music_player_controller.setAlarmSeekBarProgress(60)
        //是否点赞,默认false
        //        mMusicPlayerController.setVisivable(false);
        //        mMusicPlayerController.changeSeekbarTextColor(ContextCompat.getColor(this, R.color.user_name_color));

        //注册事件回调
        music_player_controller.setOnClickEventListener(object : MusicPlayerController.OnClickEventListener {
            //收藏事件触发了
            override fun onEventCollect(musicInfo: MusicInfo) {
                //                mPresenter.collectSpa(musicInfo);
                collectAudio(musicInfo)

            }

            //随机播放触发了
            override fun onEventRandomPlay() {
                //其他界面使用播放控制器示例

//                randomSpaInfo(typeId)
            }

            //返回事件
            override fun onBack() {
                this@LoveAudioDetailActivity.onBackPressed()
            }

            override fun onPlayState(info: MusicInfo?) {
                var id = ""
                if (info != null) {
                    id = info.id
                    if (PlayerStatus.PLAYER_STATUS_PLAYING == info.plauStatus) {
                        setPlayCont(id)

                    }
                    val isCollect = info.is_favorite == 1
                    music_player_controller.setCollectIcon(if (isCollect) R.drawable.ic_player_collect_true else R.drawable.ic_player_collect, isCollect, id)
                    setWechatStatus(isCollect)
                }
                musicInfo = info
                setSleepDetailInfo(info)
            }
        })
        //注册到被观察者中
        MusicPlayerManager.getInstance().addObservable(music_player_controller)
        //        //注册播放变化监听
        MusicPlayerManager.getInstance().addPlayerStateListener(this)
        MusicPlayerManager.getInstance().onResumeChecked()//先让播放器刷新起来

    }



    /**
     * 配合播放列表示例
     */
    private fun initAdapter() {

        //注册观察者以刷新列表
    }

    private fun collectAudio(musicInfo: MusicInfo?) {


        (mPresenter as? LoveAudioPresenter)?.collectAudio(musicInfo)


    }

    override fun showAudioCollectSuccess(collect: Boolean) {
        showCollectSuccess(collect)
        setWechatStatus(collect)
    }


    private fun setWechatStatus(isFavorate: Boolean) {
        val count = tv_collect_count.text.toString().trim { it <= ' ' }
        if (isFavorate) {
            iv_collect.setImageResource(R.mipmap.search_knack_collected_icon)
            var countInt = Integer.parseInt(count)
            tv_collect_count.text = (++countInt).toString()
        } else {
            iv_collect.setImageResource(R.mipmap.search_knack_collect_icon)
            var countInt = Integer.parseInt(count)
            tv_collect_count.text = (--countInt).toString()
        }
    }

    private fun setPlayCont(musicId: String) {

        (mPresenter as? LoveAudioPresenter)?.audioPlay(musicId)

    }

    /**
     * 在这里响应当播放器列表为空 是否播放新的歌曲事件
     *
     * @param viewTupe UI组件身份ID
     * @param position
     */
    override fun autoStartNewPlayTasks(viewTupe: Int, position: Int) {

        if (Constants.UI_TYPE_DETAILS == viewTupe) {

            MusicPlayerManager.getInstance().playMusic(musicInfo)//这个position默认是0，由控制器传出
        }
    }


    fun getData() {

        (mPresenter as? LoveAudioPresenter)?.getAudioDetailInfo(typeId)

    }


    override fun showAudioDetailInfo(musicInfo: MusicInfo?) {
        musicInfo?.let {
            showCollectSuccess(musicInfo.is_favorite == 1)
            MusicPlayerManager.getInstance().playMusic(musicInfo)
            setSleepDetailInfo(musicInfo)
            //        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
        }
    }



    fun showCollectSuccess(isCollect: Boolean) {
        music_player_controller.setCollectIcon(if (isCollect) R.drawable.ic_player_collect_true else R.drawable.ic_player_collect, isCollect)
    }


    private fun initWebView(musicInfo: MusicInfo?) {

        audio_webView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        audio_webView.isClickable = true
        val settings = audio_webView.settings
        settings.useWideViewPort = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.defaultTextEncodingName = "gb2312"
        settings.saveFormData = true
        settings.domStorageEnabled = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setAppCacheEnabled(true)
        settings.loadWithOverviewMode = true
        settings.databaseEnabled = true
        settings.javaScriptEnabled = true
        settings.loadsImagesAutomatically = true


        audio_webView.addJavascriptInterface(MyJavaScript(), "APP")

        audio_webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //                Log.e(TAG, "onPageFinished: ");

                //                document.body.getBoundingClientRect().height
                audio_webView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())")

            }
        }
        audio_webView.loadDataWithBaseURL(null, musicInfo?.content, "text/html", "utf-8", null)


    }

    private fun setSleepDetailInfo(musicInfo: MusicInfo?) {
        initWebView(musicInfo)
    }

    override fun onDestroy() {
        super.onDestroy()
        //        Log.e("TAG", "onDestroy: ");
        //必须注销所有已注册的监听
        //        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
        if (null != music_player_controller) {
            MusicPlayerManager.getInstance().deleteObserver(music_player_controller)
            music_player_controller.onDestroy()
        }
        destroyWebView()
    }

    private fun destroyWebView() {

        if (audio_webView != null) {
            audio_webView.clearHistory()
            audio_webView.clearCache(true)
            //            mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
            (audio_webView.parent as ViewGroup).removeAllViews()
            audio_webView.destroy()
        }

    }


    inner class MyJavaScript {
        @JavascriptInterface
        fun resize(height: Float) {
            Log.e(TAG, "resize: $height")
            runOnUiThread {


                val layoutParams = FrameLayout.LayoutParams(resources.displayMetrics.widthPixels - SizeUtils.dp2px(this@LoveAudioDetailActivity, 12f), (height * resources.displayMetrics.density).toInt())
                layoutParams.leftMargin = SizeUtils.dp2px(this@LoveAudioDetailActivity, 8f)
                //                layoutParams.rightMargin = SizeUtils.dp2px(LoveAudioDetailActivity.this, 12f);
                layoutParams.topMargin = SizeUtils.dp2px(this@LoveAudioDetailActivity, 5f)
                audio_webView.layoutParams = layoutParams
            }
        }

    }

    companion object {

        private val TAG = "LoveAudioDetailActivity"
    }

}