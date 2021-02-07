package com.yc.emotion.home.community.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.community.domain.model.CommunityModel
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver

import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 *
 * Created by suns  on 2019/11/14 17:20.
 */
class CommunityPresenter(context: Context?, view: CommunityView) : BasePresenter<CommunityModel, CommunityView>(context, view) {

    init {
        mModel = CommunityModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun getCommunityTagInfos() {
        mModel?.getCommunityTagInfos()?.getData(mView, { it, _ ->
            it?.let {
                mView.showCommunityTagInfos(it.list)
            }
        }, { _, _ -> }, false)


    }

    fun getCommunityNewsCache() {
        CommonInfoHelper.getO(mContext, "emotion_community_newst_data", object : TypeReference<List<CommunityInfo>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<CommunityInfo>> {

            override fun onParse(o: List<CommunityInfo>?) {
                if (o != null && o.isNotEmpty()) {
                    mView.shoCommunityNewestCacheInfos(o)
                }
            }


        })

    }


    fun getCommunityNewstInfos(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.getCommunityNewstInfos("$userId", page, pageSize)?.getData(mView, { it, _ ->
            it?.let {
                if (page == 1) {
                    CommonInfoHelper.setO<List<CommunityInfo>>(mContext, it.list, "emotion_community_newst_data")
                }
                mView.shoCommunityNewestInfos(it.list)
            }
        }, { _, _ -> mView.onError() }, page == 1)


    }

    fun likeTopic(communityInfo: CommunityInfo, position: Int) {
        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()
            mModel?.likeTopic("$userId", communityInfo.topicId)?.getData(mView, { it, _ ->
                it?.let {
                    mView.showLikeTopicSuccess(communityInfo, position)
                }
            }, { _, _ -> })

        }

    }


    fun getCommunityTagListInfo(catId: String, page: Int, pageSize: Int) {

        val userId = UserInfoHelper.instance.getUid()

        mModel?.getCommunityTagListInfo("$userId", catId, page, pageSize)?.getData(mView, { it, _ ->
            it?.let {
                mView.shoCommunityNewestInfos(it.list)
            }
        }, { _, _ -> })

    }

    fun getCommunityHotCache() {
        CommonInfoHelper.getO(mContext, "emotion_community_hot_info", object : TypeReference<List<CommunityInfo>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<CommunityInfo>> {


            override fun onParse(o: List<CommunityInfo>?) {
                if (o != null && o.isNotEmpty()) {
                    mView.showCommunityHotList(o)
                }
            }
        })
    }


    fun getCommunityHotList(page: Int, pageSize: Int) {

        val userId = UserInfoHelper.instance.getUid()

        mModel?.getCommunityHotList("$userId", page, pageSize)?.getData(mView, { it, _ ->
            it?.let {
                mView.showCommunityHotList(it.list)
                if (page == 1) {
                    CommonInfoHelper.setO<List<CommunityInfo>>(mContext, it.list, "emotion_community_hot_info")
                }
            }
        }, { _, _ -> mView.onError() }, page == 1)

    }


    fun getCommunityMyCache() {
        val userId = UserInfoHelper.instance.getUid()

        CommonInfoHelper.getO(mContext, "${userId}_emotion_community_my_info", object : TypeReference<List<CommunityInfo>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<CommunityInfo>> {


            override fun onParse(o: List<CommunityInfo>?) {
                if (o != null && o.isNotEmpty()) {
                    mView.showCommunityMyList(o)
                }
            }
        })
    }

    fun getMyCommunityInfos(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()


        mModel?.getMyCommunityInfos("$userId", page, pageSize)?.getData(mView, { it, _ ->
            if (it?.list != null
                    && it.list.size > 0) {
                val list = it.list
                mView.showCommunityMyList(list)
                if (page == 1)
                    CommonInfoHelper.setO<List<CommunityInfo>>(mContext, list, "${userId}_emotion_community_my_info")

            } else {
                if (page == 1)
                    mView.onNoData()
            }
        }, { _, _ -> }, page == 1)


    }

    fun deleteTopic(communityInfo: CommunityInfo?, position: Int) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.deleteTopic(communityInfo?.topicId, "$userId")?.getData(mView, { it, _ ->
            it?.let {
                mView.showDeleteTopicSuccess(communityInfo, position)
            }
        }, { _, _ -> })

    }


    fun getCommunityDetailInfo(topicId: String?) {

        val userId = UserInfoHelper.instance.getUid()

        mModel?.getCommunityDetailInfo("$userId", topicId)?.getData(mView, { it, _ ->
            it?.let {
                mView.showCommunityDetailInfo(it)
            }
        }, { _, _ -> })
    }


    fun commentLike(communityInfo: CommunityInfo, position: Int) {
        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()

            mModel?.commentLike("$userId", communityInfo.comment_id)?.getData(mView, { it, _ ->
                it?.let {
                    mView.showCommunityDetailSuccess(communityInfo, position)
                }
            }, { _, _ -> })
        }
    }


    fun createComment(topicId: String?, content: String?) {
        if (!UserInfoHelper.instance.goToLogin(mContext)) {

            val userId = UserInfoHelper.instance.getUid()

            mModel?.createComment("$userId", topicId, content)?.getData(mView, { _, _ ->
                val resultInfo = ResultInfo<String>()
                resultInfo.code = HttpConfig.STATUS_OK
                mView.createCommunityResult(resultInfo, content)
            }, { code, _ ->
                val resultInfo = ResultInfo<String>()
                resultInfo.code = code
                mView.createCommunityResult(resultInfo, content)
            })

        }
    }

    fun getTopTopicInfos() {
        mModel?.getTopTopicInfos()?.getData(mView, { it, _ ->
            it?.let {
                mView.showCommunityNoticeInfo(it)
            }
        }, { _, _ -> })


    }


    fun publishCommunityInfo(cat_id: String?, content: String) {

        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()
            mModel?.publishCommunityInfo("$userId", cat_id, content)?.getData(mView, { it, msg ->
                msg?.let {
                    mView.publishCommunitySuccess(msg)
                }
            }, { _, _ -> })
        }
    }
}