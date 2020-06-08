package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment

/**
 * Created by suns  on 2019/8/31 10:58.
 */
class ExitPublishFragment : BaseDialogFragment() {
    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }


    protected fun initView() {
        val bundle = arguments
        var tint: String? = null
        if (bundle != null) {
            tint = bundle.getString("tint")
        }
        val tvCancel = getView(R.id.tv_cancel)
        val tvConfirm = getView(R.id.tv_confirm)
        val tvExitTint = getView(R.id.tv_confirm_publish) as TextView
        if (!TextUtils.isEmpty(tint)) tvExitTint.text = tint
        tvCancel?.setOnClickListener { v: View? -> dismiss() }
        tvConfirm?.setOnClickListener { v: View? ->

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
        fun newInstance(tint: String?): ExitPublishFragment {
            val exitPublishFragment = ExitPublishFragment()
            val bundle = Bundle()
            bundle.putString("tint", tint)
            exitPublishFragment.arguments = bundle
            return exitPublishFragment
        }
    }
}