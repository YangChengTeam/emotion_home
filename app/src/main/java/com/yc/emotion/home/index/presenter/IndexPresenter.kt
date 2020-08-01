package com.yc.emotion.home.index.presenter

import android.content.Context
import android.util.Log
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.bean.SexInfo
import com.yc.emotion.home.index.domain.model.IndexModel
import com.yc.emotion.home.index.view.IndexView
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.mine.domain.bean.LiveInfoWrapper
import com.yc.emotion.home.model.bean.IndexInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/7 16:35.
 */
class IndexPresenter(context: Context?, view: IndexView) : BasePresenter<IndexModel, IndexView>(context, view) {


    init {
        mModel = IndexModel(context)
    }


    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
        if (!isForceUI) return
//        getIndexLiveList()
        getOnlineLiveList()
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

        val subscription = mModel?.getIndexInfo()?.subscribe(object : Subscriber<ResultInfo<IndexInfo>>() {
            override fun onNext(t: ResultInfo<IndexInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val indexInfo = t.data

                        mView.showIndexInfo(indexInfo)

                        CommonInfoHelper.setO(mContext, indexInfo, "index_emotion_choiceness_info")
                    }
                }
            }

            override fun onCompleted() {

                mView.onComplete()

            }

            override fun onError(e: Throwable?) {
            }
        })
        subScriptions?.add(subscription)
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

        val subscription = mModel?.getOnlineLiveList()?.subscribe(object : Subscriber<ResultInfo<LiveInfoWrapper>>() {
            override fun onNext(t: ResultInfo<LiveInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.list != null && t.data.list.size > 0) {
                        val list = ArrayList<LiveInfo>()
                        list.addAll(t.data.list)
                        list.addAll(t.data.recording)
                        mView.showIndexLiveInfos(list)
                        CommonInfoHelper.setO(mContext, list, "index_live_info_list")
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