package com.yc.emotion.home.index.presenter

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.IndexVerbalModel
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.IndexHotInfoWrapper
import com.yc.emotion.home.model.bean.LoveHealDateBean
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.SearchDialogueBean
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

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
        mModel?.getIndexDropInfos(keyword)?.subscribe(object : DisposableObserver<ResultInfo<IndexHotInfoWrapper>>() {
            override fun onNext(t: ResultInfo<IndexHotInfoWrapper>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showDropKeyWords(t.data.list)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

        })

    }

    fun searchVerbalTalk(keyword: String?, page: Int, pageSize: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1)
            mView.showLoadingDialog()
        mModel?.searchVerbalTalk("$userId", keyword, page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<SearchDialogueBean>>() {
            override fun onNext(t: ResultInfo<SearchDialogueBean>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val searchDialogueBean = t.data
                        mView.showSearchResult(searchDialogueBean, keyword)
                    }
                }
            }

            override fun onComplete() {
                if (page == 1)
                    mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
                mView.onError()
            }

        })

    }


    fun searchCount(keyword: String?) {
        val userId = UserInfoHelper.instance.getUid()
        mModel?.searchCount("$userId", keyword)?.subscribe(object : DisposableObserver<ResultInfo<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(stringResultInfo: ResultInfo<String>) {

            }
        })

    }


    private fun loveCategory(sence: String) {

        mView.showLoadingDialog()
        mModel?.loveCategory(sence)?.subscribe(object : DisposableObserver<ResultInfo<List<LoveHealDateBean>>>() {
            override fun onNext(t: ResultInfo<List<LoveHealDateBean>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        createNewData(t.data, sence)

                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }

        })

    }

    fun getCacheData(sence: String) {

        var key = ""
        if (sence == "")
            key = "main1_Dialogue_category"
        else if (sence == "2") {
            key = "main1_Dialogue_sence"
        }

        CommonInfoHelper.getO(mContext, key, object : TypeReference<List<LoveHealDateBean>>() {

        }.type, object : CommonInfoHelper.OnParseListener<List<LoveHealDateBean>> {

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
        mModel?.loveListCategory("$userId", category_id, page, page_size)?.subscribe(object : DisposableObserver<ResultInfo<List<LoveHealDetBean>>>() {
            override fun onNext(t: ResultInfo<List<LoveHealDetBean>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        createNewData(t.data)
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) {
                    mView.hideLoadingDialog()
                }
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }
        })

    }

    private fun createNewData(data: List<LoveHealDetBean>?) {
        data?.let {
            data.forEach {
                var details = it.details
                if (details == null || details.isEmpty()) {
                    details = it.detail
                }
                details?.let {
                    var i = 0
                    while (i < details.size) {
                        val loveHealDetDetailsBean = details[i]
                        if (TextUtils.isEmpty(loveHealDetDetailsBean.content)) {
                            details.removeAt(i)
                            i--
                        }
                        i++
                    }

                }


            }
        }
        mView.showVerbalDetailInfos(data)
    }
}