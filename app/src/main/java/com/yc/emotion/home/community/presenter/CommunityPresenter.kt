package com.yc.emotion.home.community.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.community.domain.model.CommunityModel
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.model.bean.event.CommunityPublishSuccess
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.SnackBarUtils
import com.yc.emotion.home.utils.UserInfoHelper
import org.greenrobot.eventbus.EventBus
import rx.Subscriber

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
        val subscription = mModel?.getCommunityTagInfos()?.subscribe(object : Subscriber<ResultInfo<CommunityTagInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CommunityTagInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCommunityTagInfos(t.data.list)
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
            }

        })

        subScriptions?.add(subscription)
    }

    fun getCommunityNewsCache() {
        CommonInfoHelper.getO(mContext, "emotion_community_newst_data", object : TypeReference<List<CommunityInfo>>() {

        }.type, CommonInfoHelper.onParseListener<List<CommunityInfo>> { datas ->
            if (datas != null && datas.isNotEmpty()) {
                mView.shoCommunityNewestInfos(datas)
            }
        })

    }


    fun getCommunityNewstInfos(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1) {
            mView.showLoadingDialog()
        }

        val subscription = mModel?.getCommunityNewstInfos("$userId", page, pageSize)?.subscribe(object : Subscriber<ResultInfo<CommunityInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CommunityInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        if (page == 1) {
                            CommonInfoHelper.setO<List<CommunityInfo>>(mContext, t.data.list, "emotion_community_newst_data")
                        }
                        mView.shoCommunityNewestInfos(t.data.list)
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1) {
                    mView.hideLoadingDialog()
                }
                mView.onComplete()

            }

            override fun onError(e: Throwable?) {
                if (page == 1) {
                    mView.hideLoadingDialog()
                }
                mView.onError()
            }

        })
        subScriptions?.add(subscription)
    }

    fun likeTopic(communityInfo: CommunityInfo, position: Int) {
        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()
            mView.showLoadingDialog()
            val subscription = mModel?.likeTopic("$userId", communityInfo.topicId)?.subscribe(object : Subscriber<ResultInfo<String>>() {
                override fun onNext(t: ResultInfo<String>?) {
                    t?.let {
                        if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                            mView.showLikeTopicSuccess(communityInfo, position)
                        }
                    }
                }

                override fun onCompleted() {
                    mView.hideLoadingDialog()
                }

                override fun onError(e: Throwable?) {

                }

            })
            subScriptions?.add(subscription)
        }

    }


    fun getCommunityTagListInfo(catId: String, page: Int, pageSize: Int) {

        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        val subscription = mModel?.getCommunityTagListInfo("$userId", catId, page, pageSize)?.subscribe(object : Subscriber<ResultInfo<CommunityInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CommunityInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.shoCommunityNewestInfos(t.data.list)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })

        subScriptions?.add(subscription)
    }

    fun getCommunityHotCache() {
        CommonInfoHelper.getO(mContext, "emotion_community_hot_info", object : TypeReference<List<CommunityInfo>>() {

        }.type, CommonInfoHelper.onParseListener<List<CommunityInfo>> { datas ->
            if (datas != null && datas.isNotEmpty()) {
                mView.showCommunityHotList(datas)
            }
        })
    }


    fun getCommunityHotList(page: Int, pageSize: Int) {

        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()
        val subscription = mModel?.getCommunityHotList("$userId", page, pageSize)?.subscribe(object : Subscriber<ResultInfo<CommunityInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CommunityInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCommunityHotList(t.data.list)
                        if (page == 1) {
                            CommonInfoHelper.setO<List<CommunityInfo>>(mContext, t.data.list, "emotion_community_hot_info")
                        }
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1)
                    mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {
                mView.onError()
            }

        })
        subScriptions?.add(subscription)
    }


    fun getCommunityMyCache() {
        val userId = UserInfoHelper.instance.getUid()

        CommonInfoHelper.getO(mContext, "${userId}_emotion_community_my_info", object : TypeReference<List<CommunityInfo>>() {

        }.type, CommonInfoHelper.onParseListener<List<CommunityInfo>> { datas ->
            if (datas != null && datas.isNotEmpty()) {
                mView.showCommunityMyList(datas)
            }
        })
    }

    fun getMyCommunityInfos(page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()

        val subscription = mModel?.getMyCommunityInfos("$userId", page, pageSize)?.subscribe(object : Subscriber<ResultInfo<CommunityInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CommunityInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.list != null
                                && t.data.list.size > 0) {
                            val list = t.data.list
                            mView.showCommunityMyList(list)
                            if (page == 1)
                                CommonInfoHelper.setO<List<CommunityInfo>>(mContext, list, "${userId}_emotion_community_my_info")

                        } else {
                            if (page == 1)
                                mView.onNoData()
                        }
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }

    fun deleteTopic(communityInfo: CommunityInfo?, position: Int) {
        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        val subscription = mModel?.deleteTopic(communityInfo?.topicId, "$userId")?.subscribe(object : Subscriber<ResultInfo<String>>() {
            override fun onNext(t: ResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showDeleteTopicSuccess(communityInfo, position)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun getCommunityDetailInfo(topicId: String?) {

        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        val subscription = mModel?.getCommunityDetailInfo("$userId", topicId)?.subscribe(object : Subscriber<ResultInfo<CommunityDetailInfo>>() {
            override fun onNext(t: ResultInfo<CommunityDetailInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val detailInfo = t.data

                        mView.showCommunityDetailInfo(detailInfo)
//                        initData(detailInfo)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun commentLike(communityInfo: CommunityInfo, position: Int) {
        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()
            mView.showLoadingDialog()
            val subscription = mModel?.commentLike("$userId", communityInfo.comment_id)?.subscribe(object : Subscriber<ResultInfo<String>>() {
                override fun onNext(t: ResultInfo<String>?) {
                    t?.let {
                        if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                            mView.showCommunityDetailSuccess(communityInfo, position)
                        }
                    }
                }

                override fun onCompleted() {
                    mView.hideLoadingDialog()
                }

                override fun onError(e: Throwable?) {

                }

            })
            subScriptions?.add(subscription)
        }
    }


    fun createComment(topicId: String?, content: String?) {
        if (!UserInfoHelper.instance.goToLogin(mContext)) {


            val user_id = UserInfoHelper.instance.getUid()
            mView.showLoadingDialog()
            val subscription = mModel?.createComment("$user_id", topicId, content)?.subscribe(object : Subscriber<ResultInfo<String>>() {
                override fun onNext(t: ResultInfo<String>?) {
                    t?.let {
                        mView.createCommunityResult(t, content)
                    }
                }

                override fun onCompleted() {
                    mView.hideLoadingDialog()
                }

                override fun onError(e: Throwable?) {

                }

            })
            subScriptions?.add(subscription)
        }
    }


    fun getTopTopicInfos() {
        mView.showLoadingDialog()
        val subscription = mModel?.getTopTopicInfos()?.subscribe(object : Subscriber<ResultInfo<TopTopicInfo>>() {
            override fun onNext(t: ResultInfo<TopTopicInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCommunityNoticeInfo(t.data)
                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }


    fun publishCommunityInfo(cat_id: String?, content: String) {

        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()
            mView.showLoadingDialog()
            val subscription = mModel?.publishCommunityInfo("$userId", cat_id, content)?.subscribe(object : Subscriber<ResultInfo<String>>() {
                override fun onNext(t: ResultInfo<String>?) {
                    t?.let {
                        if (t.code == HttpConfig.STATUS_OK) {

                            mView.publishCommunitySuccess(t.message)

                        }
                    }
                }

                override fun onCompleted() {
                    mView.hideLoadingDialog()
                }

                override fun onError(e: Throwable?) {

                }

            })
            subScriptions?.add(subscription)
        }
    }
}