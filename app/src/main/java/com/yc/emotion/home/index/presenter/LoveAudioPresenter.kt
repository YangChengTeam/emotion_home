package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.music.player.lib.bean.MusicInfo
import com.music.player.lib.bean.MusicInfoWrapper
import com.tencent.connect.UserInfo
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.LoveAudioModel
import com.yc.emotion.home.index.view.LoveAudioView
import com.yc.emotion.home.model.bean.AudioDataInfo
import com.yc.emotion.home.model.bean.AudioDataWrapperInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/20 11:03.
 */
class LoveAudioPresenter(context: Context?, view: LoveAudioView) : BasePresenter<LoveAudioModel, LoveAudioView>(context, view) {

    init {
        mModel = LoveAudioModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {

    }


    fun getAudioListCache() {
        CommonInfoHelper.getO(mContext, "audio_main_data", object : TypeReference<List<MusicInfo>>() {

        }.type, CommonInfoHelper.onParseListener<List<MusicInfo>> { datas ->
            if (datas != null) {
                mView.showAudioListInfo(datas, true)
            }
        })
    }

    fun getLoveItemList(typeId: String?, page: Int, limit: Int, orderInt: Int) {
        val userId = UserInfoHelper.instance.getUid()

        if (page == 1) {
            mView.showLoadingDialog()
        }
        val subscription = mModel?.getLoveItemList("$userId", typeId, page, limit, orderInt)?.subscribe(object : Subscriber<ResultInfo<MusicInfoWrapper>>() {
            override fun onNext(t: ResultInfo<MusicInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showAudioListInfo(t.data.list, false)
                        if (page == 1) CommonInfoHelper.setO<List<MusicInfo>>(mContext, t.data.list, "audio_main_data")
                    }
                }
            }

            override fun onCompleted() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {
                if (page == 1) mView.hideLoadingDialog()
                mView.onError()
            }

        })
        subScriptions?.add(subscription)
    }


    fun getAudioDetailInfo(id: String?) {
        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()
        val subscription = mModel?.getMusicDetailInfo("$userId", id)?.subscribe(object : Subscriber<ResultInfo<MusicInfoWrapper>>() {
            override fun onNext(t: ResultInfo<MusicInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showAudioDetailInfo(t.data.info)
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

    fun collectAudio(musicInfo: MusicInfo?) {

        if (!UserInfoHelper.instance.goToLogin(mContext)) {
            val userId = UserInfoHelper.instance.getUid()
            mView.showLoadingDialog()

            val subscription = mModel?.collectAudio("$userId", musicInfo?.id)?.subscribe(object : Subscriber<ResultInfo<String>>() {
                override fun onNext(t: ResultInfo<String>?) {
                    t?.let {
                        var isCollect = musicInfo?.is_favorite == 1
                        if (t.code == HttpConfig.STATUS_OK) {

                            isCollect = !isCollect
                            musicInfo?.is_favorite = if (isCollect) 1 else 0
                            mView.showAudioCollectSuccess(isCollect)

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

    fun audioPlay(spa_id: String) {
        val subscription = mModel?.audioPlay(spa_id)?.subscribe(object : Subscriber<ResultInfo<String>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: ResultInfo<String>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {

                    }
                }
            }

        })
        subScriptions?.add(subscription)
    }


    fun getAudioCategoryCache() {
        CommonInfoHelper.getO(mContext, "audio_filter_data", object : TypeReference<List<AudioDataInfo>>() {

        }.type, CommonInfoHelper.onParseListener<List<AudioDataInfo>> { filterInfos ->
            if (filterInfos != null) {
                mView.showAudioCategoryList(filterInfos)
            }
        })
        getAudioDataInfo()

    }

    fun getAudioDataInfo() {
        val subscription = mModel?.getAudioDataInfo()?.subscribe(object : Subscriber<ResultInfo<AudioDataWrapperInfo>>() {
            override fun onNext(t: ResultInfo<AudioDataWrapperInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showAudioCategoryList(t.data.list)

//                    mWorker.setCache("audio_filter_data", audioDataWrapperInfoResultInfo.data.getList());
                        CommonInfoHelper.setO<List<AudioDataInfo>>(mContext, t.data.getList(), "audio_filter_data")
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


}