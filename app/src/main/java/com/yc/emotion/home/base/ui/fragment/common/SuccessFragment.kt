package com.yc.emotion.home.base.ui.fragment.common

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment

/**
 * Created by suns  on 2019/9/4 17:01.
 */
class SuccessFragment : BaseDialogFragment() {
    private var mTilte: String? = null
    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }

    fun setTint(title: String?) {
        mTilte = title
    }

    private fun initView() {
        val tv = getView(R.id.tv_success) as TextView
        if (!TextUtils.isEmpty(mTilte)) tv.text = mTilte
    }

    override val width: Float
        protected get() = 0.4f

    override val animationId: Int
        get() = R.style.share_anim

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    override val gravity: Int
        get() = Gravity.CENTER

    override fun getLayoutId(): Int {
        return R.layout.frament_success
    }

    override fun initViews() {
        initView()
    }

}