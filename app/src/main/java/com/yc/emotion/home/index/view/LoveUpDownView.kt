package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.LoveHealingBean

/**
 *
 * Created by suns  on 2020/4/28 16:00.
 */
interface LoveUpDownView : IView, IDialog, StateDefaultImpl {
    fun showRecommendWords(data: List<LoveHealingBean>?)
    fun showCollectSuccess(msg: String?)

}