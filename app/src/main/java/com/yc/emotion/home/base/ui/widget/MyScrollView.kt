package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

/**
 * Created by suns  on 2019/7/29 16:22.
 */
class MyScrollView : NestedScrollView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
            onScrollListener?.onScroll(l, t, oldl, oldt)
    }

    private var onScrollListener: OnScrollListener? = null
    fun setOnScrollListener(onScrollListener: OnScrollListener?) {
        this.onScrollListener = onScrollListener
    }

    interface OnScrollListener {
        fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int)
    }
}