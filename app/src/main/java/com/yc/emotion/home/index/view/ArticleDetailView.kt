package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.LoveByStagesDetailsBean
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/12 11:09.
 */
interface ArticleDetailView : IView, StateDefaultImpl, IDialog {
    fun showArticleDetailInfo(data: LoveByStagesDetailsBean?)
    fun collectArticle(i: Int, collected: Boolean)
    fun digArticle(digArticle: Boolean)
}