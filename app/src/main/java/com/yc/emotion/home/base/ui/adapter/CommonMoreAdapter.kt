package com.yc.emotion.home.base.ui.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView

/**
 * Created by suns  on 2020/5/18 18:14.
 */
abstract class CommonMoreAdapter<T, K : BaseViewHolder?> : BaseQuickImproAdapter<T, K> {
    constructor(data: List<T>?) : super(data) {
        init()
    }

    constructor(layoutResId: Int, data: List<T>?) : super(layoutResId, data) {
        init()
    }

    constructor(layoutResId: Int) : super(layoutResId) {
        init()
    }

    private fun init() {
        setLoadMoreView(CustomLoadMoreView())
    }
}