package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.MainT2Bean

/**
 *
 * Created by suns  on 2019/11/20 13:30.
 */
interface LoveCaseView : IView, IDialog, StateDefaultImpl {
    fun showLoveCaseList(data: List<MainT2Bean>) {}
    fun loadMoreComplete(){}
    fun loadEnd(){}
}