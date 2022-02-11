package com.yc.emotion.home.index.ui.fragment

import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.base.ui.widget.RoundCornerImg

/**
 * Created by suns  on 2019/9/26 17:48.
 */
open class IndexActivityFragment : BaseDialogFragment() {


    private var tvWx: TextView? = null
    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }

    private fun initView() {

        val roundCornerImg = getView(R.id.roundCornerImg) as RoundCornerImg
        tvWx = getView(R.id.tv_wx) as TextView
        roundCornerImg.setCorner(20)

        val arg = arguments
        arg?.let {
            val mWx = it.getString("wx")
            tvWx?.text = Html.fromHtml("添加客服微信：<font color='#ddae52'>$mWx</font>")
        }

        rootView?.setOnClickListener { v: View? ->

            listener?.onToWx()

        }
        val ivClose = getView(R.id.iv_close) as ImageView
        ivClose.setOnClickListener { v: View? -> dismiss() }
    }

    override val width: Float
        get() = 0.7f

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT


    private var listener: onToWxListener? = null
    fun setListener(listener: onToWxListener?) {
        this.listener = listener
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_index_activity
    }

    override fun initViews() {
        initView()
    }

    override val animationId: Int
        get() = R.style.share_anim

    interface onToWxListener {
        fun onToWx()
    }
}