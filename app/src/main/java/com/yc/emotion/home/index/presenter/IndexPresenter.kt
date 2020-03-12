package com.yc.emotion.home.index.presenter

import android.content.Context
import android.util.Log
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R.id.swipeRefreshLayout
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.index.domain.bean.SexInfo
import com.yc.emotion.home.index.domain.model.IndexModel
import com.yc.emotion.home.index.view.IndexView
import com.yc.emotion.home.model.bean.IndexInfo
import com.yc.emotion.home.model.bean.LiveInfoWrapper
import com.yc.emotion.home.utils.CommonInfoHelper
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

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
        getIndexLiveList()
    }


    /**
     * 获取缓存数据
     * ps:为什么缓存要和网络数据分开???
     *    刷新时只需要取网络数据，不需要取缓存，如果放在一起就会导致刷新时还要取一次缓存 所以分开定义
     */
    override fun getCache() {

        Log.e("TAG", "getCache")
        CommonInfoHelper.getO(mContext, "index_emotion_choiceness_info", object : TypeReference<IndexInfo>() {

        }.type, CommonInfoHelper.onParseListener<IndexInfo> { detailInfos ->
            detailInfos?.let {
                mView.showIndexCaches(detailInfos)
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


    fun getSexData(sex: Int) {
        val subscription = mModel?.getSexInfoBySex(sex)?.subscribe(object : Subscriber<List<SexInfo>>() {
            override fun onNext(t: List<SexInfo>?) {
                t?.let {
                    mView.showIcon(t)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }

    fun getIndexLiveList() {
        val subscription = mModel?.getLiveListInfo()?.subscribe(object : Subscriber<ResultInfo<LiveInfoWrapper>>() {
            override fun onNext(t: ResultInfo<LiveInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.data != null) {
                        val liveInfos = t.data.data
                        mView.showIndexLiveInfos(liveInfos)
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