package com.yc.emotion.home.base.ui.fragment.common

import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.utils.clickWithTrigger

/**
 * Created by suns  on 2019/9/6 16:41.
 */
class AddWxFragment : BaseDialogFragment() {

    private var mWx: String? = ""
    private var tvWx: TextView? = null


    private fun initView() {

        tvWx = rootView?.findViewById(R.id.tv_wx) as TextView
        val tvCopyWx = rootView?.findViewById(R.id.tv_copy_wx) as TextView
        val ivClose = rootView?.findViewById(R.id.iv_close) as ImageView
        if (!TextUtils.isEmpty(mWx)) tvWx?.text = mWx
        tvCopyWx.clickWithTrigger { v: View? ->
            listener?.onToWx()
        }
        ivClose.clickWithTrigger { v: View? -> dismiss() }
    }

    override val width: Float
        protected get() = 0.75f

    override val animationId: Int
        get() = R.style.share_anim

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override val gravity: Int
        get() = Gravity.CENTER

    override fun getLayoutId(): Int {
        return R.layout.frament_add_wx
    }

    override fun initViews() {
        initView()
    }

    fun setWX(wx: String?) {

        mWx = wx
    }

    private var listener: OnToWxListener? = null
    fun setListener(listener: OnToWxListener?) {
        this.listener = listener
    }


    interface OnToWxListener {
        fun onToWx()
    }
}