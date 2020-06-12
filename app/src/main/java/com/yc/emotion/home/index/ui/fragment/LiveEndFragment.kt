package com.yc.emotion.home.index.ui.fragment

import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment

/**
 *
 * Created by suns  on 2020/6/8 17:59.
 */
class LiveEndFragment : BaseDialogFragment() {
    override val width: Float
        get() = 0.6f
    override val animationId: Int
        get() = 0
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_live_end
    }

    override fun initViews() {
        rootView?.findViewById<TextView>(R.id.tv_end)?.setOnClickListener {
            dismiss()
            activity?.finish()
        }
    }
}