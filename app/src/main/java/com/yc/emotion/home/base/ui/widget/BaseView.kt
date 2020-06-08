package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import org.jetbrains.annotations.NotNull

/**
 * Created by wanglin  on 2018/2/27 11:45.
 */
abstract class BaseView : FrameLayout {
    protected var mContext: Context
    protected var mRootView: View

    constructor(context: Context) : super(context) {
        mContext = context
        mRootView = LayoutInflater.from(context).inflate(getLayoutId(), this)
        init()
    }

    protected abstract fun getLayoutId(): Int

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        mRootView = LayoutInflater.from(context).inflate(getLayoutId(), this)
        init()
    }

    fun getView(resId: Int): View {
        return mRootView.findViewById(resId)
    }

    fun init() {}
}