package com.yc.emotion.home.index.ui.fragment

import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.utils.clickWithTrigger

/**
 * Created by suns  on 2021/12/02 14:42 .
 */
class ToWxFragment : BaseDialogFragment() {
    override val width: Float
        get() = 0.8f
    override val animationId: Int
        get() = 0
    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.fragment_to_wx
    }

    override fun initViews() {
        rootView?.findViewById<TextView>(R.id.tv_cancel)?.clickWithTrigger { dismiss() }
        rootView?.findViewById<TextView>(R.id.tv_confirm)?.clickWithTrigger {
            (requireActivity() as BaseActivity).openWeiXin()
            dismiss()
        }
    }
}