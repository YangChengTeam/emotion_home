package com.yc.emotion.home.base.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ScrollView

/**
 * Created by mayn on 2019/5/10.
 */
class NewsScrollView : ScrollView {
    private var scaledTouchSlop: Int
    private var y = 0
    private var x = 0

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : super(context, attrs) {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                y = ev.y.toInt()
                x = ev.x.toInt()
            }
            MotionEvent.ACTION_UP -> {
                val curY = ev.y.toInt()
                val curX = ev.x.toInt()
                if (Math.abs(curY - y) > scaledTouchSlop) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (listener != null) {
            listener!!.onScrollChange(l, t, oldl, oldt)
        }
    }

    interface onScrollChangeListener {
        fun onScrollChange(l: Int, t: Int, oldl: Int, oldt: Int)
    }

    var listener: onScrollChangeListener? = null
        private set

    fun setOnScrollChangeListener(listener: onScrollChangeListener?) {
        this.listener = listener
    }
}