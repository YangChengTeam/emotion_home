package com.yc.emotion.home.index.ui.fragment

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_live_intro.*

/**
 *
 * Created by suns  on 2020/6/8 16:52.
 */
class LiveIntroFragment : BaseDialogFragment() {
    override val width: Float
        get() = 0.7f
    override val animationId: Int
        get() = R.style.share_anim
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_live_intro
    }

    override fun initViews() {
        val tvTitle = rootView?.findViewById<TextView>(R.id.tv_live_content)
        val tvLiveTime = rootView?.findViewById<TextView>(R.id.tv_live_time)
        val arg = arguments
        arg?.let {
            if (it.containsKey("livetitle")) {
                val title = it.getString("livetitle")
                val startTime = it.getString("start_time")
                val endTime = it.getString("end_time")

                tvTitle?.text = title
                if (startTime != null && endTime != null) {
                    tvLiveTime?.text = String.format(getString(R.string.start_end_time, startTime, endTime))
                } else {
                    ll_live_time?.visibility = View.GONE
                }
            }
        }

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
    }
}