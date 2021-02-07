package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.IndexSearchInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/19 14:53.
 */
interface EmotionSearchView : IView,StateDefaultImpl, IDialog {
    fun showEmotionSearchResult(data: IndexSearchInfo?)
}