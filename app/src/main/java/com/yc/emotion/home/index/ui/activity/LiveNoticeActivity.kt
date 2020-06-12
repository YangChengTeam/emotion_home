package com.yc.emotion.home.index.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import kotlinx.android.synthetic.main.activity_live_notice.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Created by suns  on 2020/6/10 17:18.
 */
class LiveNoticeActivity : BaseActivity() {
    companion object {
        fun startActivity(context: Context, liveInfo: LiveInfo) {
            val intent = Intent(context, LiveNoticeActivity::class.java)
            intent.putExtra("liveinfo", liveInfo)
            context.startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        invadeStatusBar() //侵入状态栏

        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_live_notice
    }


    override fun initViews() {
        val liveInfo = intent?.getParcelableExtra<LiveInfo>("liveinfo")
        liveInfo?.let {

            tv_live_time.text = String.format(getString(R.string.start_end_time), convertTime(liveInfo.start_time), convertTime(liveInfo.end_time))
            tv_live_tutor.text = "主讲：${it.nickname}"
            tv_live_title.text = it.live_title
            Glide.with(this).load(it.face).circleCrop().error(R.drawable.default_avatar_72).into(iv_tutor_face)
        }

        rl_back.setOnClickListener { finish() }
    }

    private fun convertTime(time: Long): String? {
        val sd = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sd.format(Date(time * 1000))
    }
}