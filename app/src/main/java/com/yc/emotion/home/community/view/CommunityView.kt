package com.yc.emotion.home.community.view


import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.CommunityDetailInfo
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.CommunityTagInfo
import com.yc.emotion.home.model.bean.TopTopicInfo
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/14 17:22.
 */
interface CommunityView : IView, IDialog, StateDefaultImpl {
    fun showCommunityTagInfos(list: List<CommunityTagInfo>) {}
    fun shoCommunityNewestInfos(datas: List<CommunityInfo>?) {}
    fun showLikeTopicSuccess(communityInfo: CommunityInfo, position: Int) {}
    fun showCommunityHotList(list: List<CommunityInfo>) {}
    fun showCommunityMyList(list: List<CommunityInfo>) {}
    fun showDeleteTopicSuccess(communityInfo: CommunityInfo?, position: Int) {}
    fun showCommunityDetailInfo(detailInfo: CommunityDetailInfo?) {}
    fun showCommunityDetailSuccess(communityInfo: CommunityInfo, position: Int) {}
    fun createCommunityResult(t: ResultInfo<String>, content: String?) {}
    fun showCommunityNoticeInfo(data: TopTopicInfo?) {}
    fun publishCommunitySuccess(message: String) {}
    fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?)
}