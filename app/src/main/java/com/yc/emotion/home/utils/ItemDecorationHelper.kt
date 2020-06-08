package com.yc.emotion.home.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.kk.utils.ScreenUtil

/**
 * Created by wanglin  on 2018/10/25 13:45.
 */
class ItemDecorationHelper(context: Context?, left: Int, top: Int, right: Int, bottom: Int) : ItemDecoration() {
    private val mContext: Context?
    private var right = 0
    private var bottom = 0
    private var left = 0
    private var top = 0

    constructor(context: Context?, right: Int, bottom: Int) : this(context, 0, 0, right, bottom) {}
    constructor(context: Context?, bottom: Int) : this(context, 0, bottom) {}

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[ScreenUtil.dip2px(mContext, left.toFloat()), ScreenUtil.dip2px(mContext, top.toFloat()), ScreenUtil.dip2px(mContext, right.toFloat())] = ScreenUtil.dip2px(mContext, bottom.toFloat())
    }

    init {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        mContext = context
    }
}