package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.utils.clickWithTrigger

/**
 * Created by suns  on 2019/8/31 10:58.
 */
class ExitPublishFragment private constructor() : BaseDialogFragment() {


    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }


    private fun initView() {
        val bundle = arguments
        var tint: String? = null
        var isCancel = false
        if (bundle != null) {
            tint = bundle.getString("tint")
            isCancel = bundle.getBoolean("isCancel")
        }
        val tvCancel = getView(R.id.tv_cancel)
        val tvConfirm = getView(R.id.tv_confirm)
        val tvExitTint = getView(R.id.tv_confirm_publish) as TextView
        val dividerView = getView(R.id.view_divider)

        if (!TextUtils.isEmpty(tint)) tvExitTint.text = tint
        tvCancel?.visibility = if (isCancel) View.GONE else View.VISIBLE
        dividerView?.visibility = if (isCancel) View.GONE else View.VISIBLE
        tvCancel?.clickWithTrigger { v: View? -> dismiss() }
        tvConfirm?.clickWithTrigger { v: View? ->

            confirmListener?.onConfirm()

            dismiss()
        }
    }

    override val width: Float
        protected get() = 0.8f

    override val animationId: Int
        get() = R.style.share_anim

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun getLayoutId(): Int {
        return R.layout.frament_exit_publish
    }


    override fun initViews() {
        initView()
    }


    private var confirmListener: OnConfirmListener? = null
    fun setOnConfirmListener(confirmListener: OnConfirmListener?) {
        this.confirmListener = confirmListener
    }


    interface OnConfirmListener {
        fun onConfirm()
    }

    companion object {
        fun newInstance(tint: String?, isCancel: Boolean = false): ExitPublishFragment {
            val exitPublishFragment = ExitPublishFragment()
            val bundle = Bundle()
            bundle.putString("tint", tint)
            bundle.putBoolean("isCancel", isCancel)
            exitPublishFragment.arguments = bundle
            return exitPublishFragment
        }
    }
}