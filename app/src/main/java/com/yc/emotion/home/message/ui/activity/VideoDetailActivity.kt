package com.yc.emotion.home.message.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.danikula.videocache.HttpProxyCacheServer
import com.umeng.analytics.MobclickAgent
import com.video.player.lib.base.IMediaPlayer
import com.video.player.lib.constants.VideoConstants
import com.video.player.lib.manager.VideoPlayerManager
import com.video.player.lib.manager.VideoWindowManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.EmApplication
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment
import com.yc.emotion.home.message.ui.view.MyVideoController
import com.yc.emotion.home.model.bean.VideoItem
import com.yc.emotion.home.utils.UIUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.layout_short_video_play.*
import yc.com.rthttplibrary.util.ScreenUtil


/**
 * Created by suns  on 2020/8/4 15:03.
 */
class VideoDetailActivity : BaseActivity() {


    private var videoItem: VideoItem? = null

    companion object {
        fun startActivity(context: Context?, videoItem: VideoItem) {
            val intent = Intent(context, VideoDetailActivity::class.java)
            intent.putExtra("videoItem", videoItem)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        invadeStatusBar()
        initViews()

    }

    override fun getLayoutId(): Int {
        return R.layout.layout_short_video_play
    }

    override fun initViews() {
        intent?.let {
            videoItem = it.getParcelableExtra("videoItem")
        }

        short_video_from.text = UIUtils.getAppName(this)
        tv_app_name.text = UIUtils.getAppName(this)
        val layoutParams = layout_short_video_play_header.layoutParams
        layoutParams.height = ScreenUtil.getHeight(this) - ScreenUtil.dip2px(this, 20f)
        layout_short_video_play_header.layoutParams = layoutParams
        initViewData()
        initListener()
        play()

    }

    private fun initListener() {

        short_video_end_person_retry_play.clickWithTrigger {
            VideoPlayerManager.getInstance().isMobileWorkEnable = true

            IMediaPlayer.getInstance().reStartVideoPlayer(0)
//            PIMediaPlayer.getInstance().reStartVideoPlayer(0)

        }
        iv_back.clickWithTrigger {
            finish()
        }

        iv_wx.clickWithTrigger {
            showWxDialog()
        }
        short_video_person_get_wx_short.clickWithTrigger {
            showWxDialog()
        }
        short_video_end_person_get_wx.clickWithTrigger {
            showWxDialog()
        }
    }

    private fun initViewData() {
        short_video_person_short_tv.text = videoItem?.title
        tv_video_title.text = videoItem?.title
        tv_play_count.text = "${videoItem?.playCount}"
        Glide.with(this).load(videoItem?.tutor_face).error(R.mipmap.tutor_head).apply(RequestOptions().circleCrop())
                .into(iv_tutor_face)
        short_video_end_hint_tv.text = videoItem?.title

        Glide.with(this).load(videoItem?.tutor_face).apply(RequestOptions().circleCrop()).into(short_video_end_person_iv)
        short_video_end_person_tv.text = videoItem?.tutor_name
        tv_create_date.text = videoItem?.created_at
        tv_tutor_profession.text = videoItem?.tutor_profession
    }

    private fun play() {
        videoItem?.let {
            val proxyUrl = it.videoUrl
//            proxyUrl="http://qg-bshu.zhuoyi52.com/videos/mda-kapy4qefphqd6ijn.mp4"

//            proxyUrl = "http://qg-bshu.zhuoyi52.com/videos/8f7fdde3024e8d5c0e8362f04f3c2a1b.mp4"
            Glide.with(this).load(it.picCover).apply(RequestOptions().error(R.mipmap.efficient_course_example_pic)).into(short_video_view.coverController.mVideoCover)
            short_video_view.setVideoDisplayType(VideoConstants.VIDEO_DISPLAY_TYPE_ZOOM)
            val myVideoController = MyVideoController(this)
            myVideoController.setOnStateListener(object : MyVideoController.OnStateListener {
                override fun onPlayComplete() {
                    short_video_end_person_short_root.visibility = View.VISIBLE
                }

                override fun onProgressChanged() {}

                override fun onPause() {}

                override fun onPrepare() {

                    short_video_end_person_short_root.visibility = View.GONE
                }

                override fun onPlay() {}

            })
            short_video_view.setVideoController(myVideoController, false)
            short_video_view.startPlayVideo(proxyUrl, it.title)
        }

    }

    private fun showWxDialog() {
        val addWxFragment = AddWxFragment()
        addWxFragment.setWX(videoItem?.tutor_weixin)
        addWxFragment.setListener(object : AddWxFragment.OnToWxListener {
            override fun onToWx() {
                val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip = ClipData.newPlainText("text", videoItem?.tutor_weixin)
                myClipboard.setPrimaryClip(myClip)
                openWeiXin()
            }

        })
        addWxFragment.show(supportFragmentManager, "")
        MobclickAgent.onEvent(this, "video_wx_click", "小视频详情页添加微信")
    }

    override fun onDestroy() {
        super.onDestroy()
        VideoPlayerManager.getInstance().onDestroy()
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy()
    }
}