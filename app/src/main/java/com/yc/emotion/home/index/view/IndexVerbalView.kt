package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.IndexHotInfo
import com.yc.emotion.home.model.bean.LoveHealDateBean
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.SearchDialogueBean

/**
 *
 * Created by suns  on 2019/11/13 13:50.
 */
interface IndexVerbalView : IView, IDialog, StateDefaultImpl {
    fun showDropKeyWords(list: List<IndexHotInfo>) {}
    fun showSearchResult(searchDialogueBean: SearchDialogueBean?, keyword: String?) {}
    fun showVerbalSenceInfo(data: List<LoveHealDateBean>) {}
    fun showVerbalDetailInfos(data: List<LoveHealDetBean>?) {}
}