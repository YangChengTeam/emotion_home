package com.yc.emotion.home.index.ui.fragment

import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment

/**
 * Created by suns  on 2020/6/3 18:26.
 */
class CloseLiveFragment : BaseDialogFragment() {


    override val width: Float
        protected get() = 0.7f

    override val animationId: Int
        get() = 0

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_close_live
    }

    override fun initViews() {
        rootView?.findViewById<TextView>(R.id.tv_minimize)?.setOnClickListener {
            onCloseListener?.onMinimize()
            dismiss()
        }

        rootView?.findViewById<TextView>(R.id.tv_close)?.setOnClickListener {
            onCloseListener?.onClose()
            dismiss()
        }
    }

    private var onCloseListener: OnCloseListener? = null

    fun setOnCloseListener(listener: OnCloseListener) {
        this.onCloseListener = listener
    }

    interface OnCloseListener {
        fun onMinimize()
        fun onClose()
    }
}