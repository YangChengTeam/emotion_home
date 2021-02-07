package com.yc.emotion.home.mine.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2020/8/26 10:19.
 */
interface RewardView : IView, IDialog, StateDefaultImpl {
    fun showRewardInfo(data: RewardInfo?)
    fun showBindSuccess()
    fun showRewardDetailList(data: List<RewardDetailInfo>?)
    fun showDisposeApplySuccess()
    fun showDisposeDetailInfoList(data: List<DisposeDetailInfo>?)
    fun showRewardMoneyList(data: List<RewardDetailInfo>?)
    fun showRankingList(data: List<RewardDetailInfo>?)
    fun showBindInvitation(data: RewardInfo?)

}