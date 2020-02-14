package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.confession.ConfessionDataBean

/**
 *
 * Created by suns  on 2019/11/20 14:48.
 */
interface ExpressView : IView, IDialog, StateDefaultImpl {
    fun loadEnd() {}
    fun loadMoreComplete() {}
    fun showConfessionInfos(mConfessionDataBeans: List<ConfessionDataBean>) {}
    fun showNormalDataSuccess(data: String?) {}
}