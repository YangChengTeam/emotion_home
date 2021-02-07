package com.yc.emotion.home.index.presenter

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.EmotionTestModel
import com.yc.emotion.home.index.view.EmotionTestView
import com.yc.emotion.home.model.bean.CourseInfoWrapper
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo
import com.yc.emotion.home.model.bean.QuestionInfo
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.config.HttpConfig


/**
 *
 * Created by suns  on 2019/11/12 14:42.
 */
class EmotionTestPresenter(context: Context?, view: EmotionTestView) : BasePresenter<EmotionTestModel, EmotionTestView>(context, view) {

    init {
        mModel = EmotionTestModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {

    }

    override fun getCache() {
    }


    fun getTestDetailInfo(test_id: String?) {
        val userId = UserInfoHelper.instance.getUid()


        mModel?.getTestDetailInfo("$userId", test_id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showEmotionTestInfo(it)
            }
        }, { _, _ -> })

    }


    fun submitAnswer(test_id: String?, questionInfos: List<QuestionInfo>?, aid: String?, option_id: String?) {
        val userId = UserInfoHelper.instance.getUid()
        mModel?.submitAnswer("$userId", test_id, questionInfos, aid, option_id)?.getData(mView, { it, _ ->
            it?.let {

                mView.showEmotionTestResult(it)
            }
        }, { _, _ -> })

    }

    fun getTestCategoryInfos() {
        mModel?.getTestCategoryInfos()?.getData(mView, { it, _ ->
            it?.let {
                mView.showEmotionCategorys(it.list)
            }
        }, { _, _ -> }, false)
    }

    fun getEmotionTestInfos(catId: String?, page: Int, pageSize: Int) {

        mModel?.getEmotionTestInfos(catId, page, pageSize)?.getData(mView, { it, _ ->
            if (it != null) {
                mView.showEmotionTestListInfo(it)
            } else {
                if (page == 1) mView.onNoData()
            }
        }, { _, _ -> mView.onError() }, page == 1)
    }


    fun getTestRecords(page: Int, page_size: Int) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.getTestRecords("$userId", page, page_size)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showTestRecords(it)
                if (page == 1) {
                    CommonInfoHelper.setO(mContext, it, "${userId}test_reports")
                }
            } else {
                if (page == 1) mView.onNoData()
            }
        }, { _, _ -> mView.onComplete() }, page == 1)
    }

    fun getTestRecordsCache() {
        val userId = UserInfoHelper.instance.getUid()

        CommonInfoHelper.getO(mContext, "${userId}test_reports", object : TypeReference<List<EmotionTestInfo>>() {}.type,
                object : CommonInfoHelper.OnParseListener<List<EmotionTestInfo>> {
                    override fun onParse(o: List<EmotionTestInfo>?) {
                        if (o != null && o.isNotEmpty()) {
                            mView.showTestRecords(o)
                        }
                    }
                })

    }

    fun getTestRecordDetail(record_id: String?) {

        mModel?.getTestRecordDetail(record_id)?.getData(mView, { it, _ ->
            it?.let {
                mView.showTestRecordDetail(it)
            }
        }, { _, _ -> })

    }


}