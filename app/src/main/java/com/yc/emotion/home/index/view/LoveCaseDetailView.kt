package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/14 10:52.
 */
interface LoveCaseDetailView : IView, IDialog {
    fun showLoveCaseDetail(data: LoveByStagesDetailsBean?)
    fun showLoveCaseCollectResult(i: Int, collected: Boolean)
    fun showLoveCaseDigResult( dig: Boolean)
}