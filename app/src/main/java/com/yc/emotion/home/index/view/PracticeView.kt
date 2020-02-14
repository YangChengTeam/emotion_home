package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.MainT2Bean

/**
 *
 * Created by suns  on 2019/11/13 17:13.
 */
interface PracticeView : IView, IDialog, StateDefaultImpl {
    fun showPracticeInfoList(exampDataBean: List<MainT2Bean>)

    fun loadMoreComplete()

    fun loadMoreEnd()
}