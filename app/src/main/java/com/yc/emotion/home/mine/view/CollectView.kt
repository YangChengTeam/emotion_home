package com.yc.emotion.home.mine.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean

/**
 *
 * Created by suns  on 2019/11/15 16:08.
 */
interface CollectView : IView, IDialog, StateDefaultImpl {
    fun showCollectCourseList(data: List<CourseInfo>?) {}
    fun showCollectVerbalList(t: List<LoveHealDetDetailsBean>) {}
    fun showCollectArticleList(mExampListsBeans: List<ArticleDetailInfo>?) {}
    fun showDeleteSuccess() {}
    fun showCollectSuccess() {}
}