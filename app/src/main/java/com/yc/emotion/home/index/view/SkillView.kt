package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.*

/**
 *
 * Created by suns  on 2019/11/14 11:50.
 */
interface SkillView : IView, IDialog, StateDefaultImpl {
    fun showSkillInfos(mDatas: List<MainT3Bean>) {}
    fun showCategoryArticleInfos(data: List<CategoryArticleBean>?) {}
    fun showSkillListInfo(lists: List<ArticleDetailInfo>) {}
    fun showSkillArticleList(data: List<LoveByStagesBean>?) {}
    fun showSkillArticleDetailInfo(data: LoveByStagesDetailsBean?) {}
    fun showSkillArticleCollectResult(num:Int,collectArticle: Boolean) {}
    fun showSkillArticleDigResult(digArticle: Boolean) {}
}