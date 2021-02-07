package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2020/4/29 10:56.
 */
interface MonagraphView : IView, IDialog,StateDefaultImpl {
    fun showMonagraphInfos(data: List<ArticleDetailInfo>?)
}