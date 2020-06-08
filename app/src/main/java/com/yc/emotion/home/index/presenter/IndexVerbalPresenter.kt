package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.IndexVerbalModel
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/13 13:48.
 */
class IndexVerbalPresenter(context: Context?, view: IndexVerbalView) : BasePresenter<IndexVerbalModel, IndexVerbalView>(context, view) {

    init {
        mModel = IndexVerbalModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {
    }

    fun getIndexDropInfos(keyword: String?) {
        val subscription = mModel?.getIndexDropInfos(keyword)?.subscribe(object : Subscriber<ResultInfo<IndexHotInfoWrapper>>() {
            override fun onNext(t: ResultInfo<IndexHotInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showDropKeyWords(t.data.list)
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

    fun searchVerbalTalk(keyword: String?, page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()
        val subscription = mModel?.searchVerbalTalk("$userId", keyword, page, pageSize)?.subscribe(object : Subscriber<AResultInfo<SearchDialogueBean>>() {
            override fun onNext(t: AResultInfo<SearchDialogueBean>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val searchDialogueBean = t.data
                        mView.showSearchResult(searchDialogueBean, keyword)
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


    fun searchCount(keyword: String?) {
        val userId = UserInfoHelper.instance.getUid()
        val subscription = mModel?.searchCount("$userId", keyword)?.subscribe(object : Subscriber<ResultInfo<String>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(stringResultInfo: ResultInfo<String>) {

            }
        })

        subScriptions?.add(subscription)
    }


    private fun loveCategory(sence: String) {

        mView.showLoadingDialog()
        val subscription = mModel?.loveCategory(sence)?.subscribe(object : Subscriber<AResultInfo<List<LoveHealDateBean>>>() {
            override fun onNext(t: AResultInfo<List<LoveHealDateBean>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        createNewData(t.data, sence)

                    }
                }
            }

            override fun onCompleted() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)
    }

    fun getCacheData(sence: String) {

        var key = ""
        if (sence == "")
            key = "main1_Dialogue_category"
        else if (sence == "2") {
            key = "main1_Dialogue_sence"
        }

        CommonInfoHelper.getO(mContext, key, object : TypeReference<List<LoveHealDateBean>>() {

        }.type, object :CommonInfoHelper.OnParseListener<List<LoveHealDateBean>> {

            override fun onParse(o: List<LoveHealDateBean>?) {
                if (null != o && o.isNotEmpty()) {
                    mView.showVerbalSenceInfo(o)
                }
            }
        })
        loveCategory(sence)
    }


    private fun createNewData(loveHealDateBeans: List<LoveHealDateBean>?, sence: String) {
        if (loveHealDateBeans == null || loveHealDateBeans.isEmpty()) {
            return
        }
        val mDatas = arrayListOf<LoveHealDateBean>()

        loveHealDateBeans.forEach {
            it.type = LoveHealDateBean.ITEM_TITLE
            mDatas.add(it)
            it.children?.forEach { item ->
                item.type = LoveHealDateBean.ITEM_CONTENT
                mDatas.add(item)
            }
        }

        mView.showVerbalSenceInfo(mDatas)

        if (sence == "")
            CommonInfoHelper.setO(mContext, mDatas, "main1_Dialogue_category")
        else if (sence == "2") {
            CommonInfoHelper.setO(mContext, mDatas, "main1_Dialogue_sence")
        }

    }


    fun loveListCategory(category_id: String?, page: Int, page_size: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()
        val subscription = mModel?.loveListCategory("$userId", category_id, page, page_size)?.subscribe(object : Subscriber<AResultInfo<List<LoveHealDetBean>>>() {
            override fun onNext(t: AResultInfo<List<LoveHealDetBean>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showVerbalDetailInfos(t.data)
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

            }
        })

        subScriptions?.add(subscription)
    }
}