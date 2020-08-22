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
class IndexActivityFragment : BaseDialogFragment() {
    private val window: Window? = null
    private override var rootView: View? = null
    private var mWx = ""
    private var tvWx: TextView? = null
    fun getView(resId: Int): View {
        return rootView!!.findViewById(resId)
    }

    protected fun initView() {
        Log.e("->", "initView: ")
        val roundCornerImg = getView(R.id.roundCornerImg) as RoundCornerImg
        tvWx = getView(R.id.tv_wx) as TextView
        roundCornerImg.setCorner(20)
        rootView!!.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onToWx()
            }
        }
        val ivClose = getView(R.id.iv_close) as ImageView
        ivClose.setOnClickListener { v: View? -> dismiss() }
    }

    override val width: Float
        protected get() = 0.7f

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    fun setWX(wx: String) {
        mWx = wx
    }

    override fun onResume() {
        super.onResume()
        Log.e("->", "setWX: $mWx")
        tvWx!!.text = Html.fromHtml("添加客服微信：<font color='#ddae52'>$mWx</font>")
    }

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