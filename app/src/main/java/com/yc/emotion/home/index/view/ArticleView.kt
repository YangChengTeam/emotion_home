package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.AticleTagInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/8 11:23.
 */
interface ArticleView : IView, StateDefaultImpl, IDialog {
    fun showArticleCategory(data: List<AticleTagInfo>?) {}
    fun showArticleInfoList(data: List<ArticleDetailInfo>?) {

    }

}