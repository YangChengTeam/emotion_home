package com.yc.emotion.home.index.presenter

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.IndexModel
import com.yc.emotion.home.index.view.IndexView
import com.yc.emotion.home.mine.domain.bean.*
import com.yc.emotion.home.model.bean.IndexInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.Preference
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig
import java.util.zip.Inflater


/**
 *
 * Created by suns  on 2019/11/7 16:35.
 */
class IndexPresenter(context: Context?, view: IndexView) : BasePresenter<IndexModel, IndexView>(context, view) {

    private var invatationcode by Preference(ConstantKey.INVITATION_CODE, "")

    init {
        mModel = IndexModel(context)
    }


    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return
//        getIndexLiveList()
//        getOnlineLiveList()
        getLiveVideoInfoList()
        getRewardInfo()
    }


    /**
     * 获取缓存数据
     * ps:为什么缓存要和网络数据分开???
     *    刷新时只需要取网络数据，不需要取缓存，如果放在一起就会导致刷新时还要取一次缓存 所以分开定义
     */
    override fun getCache() {

        CommonInfoHelper.getO(mContext, "index_emotion_choiceness_info", object : TypeReference<IndexInfo>() {

        }.type, object : CommonInfoHelper.OnParseListener<IndexInfo> {


            override fun onParse(o: IndexInfo?) {
                o?.let {
                    mView.showIndexCaches(o)
                }
            }
        })

        getIndexData()
    }

    /**
     * 获取网络数据
     */
    fun getIndexData() {

        mModel?.getIndexInfo()?.getData(mView, { indexInfo, _ ->
            indexInfo?.let {
                mView.showIndexInfo(indexInfo)

                CommonInfoHelper.setO(mContext, indexInfo, "index_emotion_choiceness_info")
            }

        }, { _, _ -> }, false)

    }


    fun getOnlineLiveList() {
        CommonInfoHelper.getO(mContext, "index_live_info_list",
                object : TypeReference<List<LiveInfo>>() {}.type, object : CommonInfoHelper.OnParseListener<List<LiveInfo>> {
            override fun onParse(o: List<LiveInfo>?) {
                o?.let {
                    if (it.isNotEmpty()) {
                        mView.showIndexLiveInfos(o)
                    }
                }
            }
        })

        mModel?.getOnlineLiveList()?.getData(mView, { it, _ ->
            it?.let {
                if (it.list != null && it.list.size > 0) {
                    val list = ArrayList<LiveInfo>()
                    list.addAll(it.list)
                    list.addAll(it.recording)
                    mView.showIndexLiveInfos(list)
                    CommonInfoHelper.setO(mContext, list, "index_live_info_list")
                }
            }
        }, { _, _ -> }, false)


    }


    fun getLiveVideoInfoList() {
        CommonInfoHelper.getO(mContext, "index_live_video_list",
                object : TypeReference<List<LiveVideoInfo>>() {}.type, object : CommonInfoHelper.OnParseListener<List<LiveVideoInfo>> {
            override fun onParse(o: List<LiveVideoInfo>?) {
                o?.let {
                    if (it.isNotEmpty()) {
                        mView.showIndexLiveVideos(o)
                    }
                }
            }
        })

        mModel?.getLiveVideoInfoList()?.getData(mView, { it, _ ->
            it?.let {
                mView.showIndexLiveVideos(it.list)
                CommonInfoHelper.setO(mContext, it.list, "index_live_video_list")
            }
        }, { _, _ -> }, false)


    }

    fun statisticsLive(id: String?) {
        mModel?.statisticsLive(id)?.getData(mView, { _, _ -> }, { _, _ -> }, false)

    }


    fun getRewardInfo() {
        mModel?.getRewardInfo()?.getData(mView, { it, _ ->
            it?.let {
                if (!TextUtils.isEmpty(it.code))
                    invatationcode = it.code
            }
        }, { _, _ -> }, false)
    }
}