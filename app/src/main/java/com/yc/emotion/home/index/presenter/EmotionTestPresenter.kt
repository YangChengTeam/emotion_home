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

        mView.showLoadingDialog()
        mModel?.getTestDetailInfo("$userId", test_id)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<EmotionTestTopicInfo>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<EmotionTestTopicInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val emotionTestTopicInfo = t.data
                        mView.showEmotionTestInfo(emotionTestTopicInfo)

                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }
        })

    }


    fun submitAnswer(test_id: String?, questionInfos: List<QuestionInfo>?, aid: String?, option_id: String?) {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        mModel?.submitAnswer("$userId", test_id, questionInfos, aid, option_id)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<EmotionTestInfo>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<EmotionTestInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val emotionTestInfo = t.data
                        mView.showEmotionTestResult(emotionTestInfo)

                    }
                }

            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })
    }

    fun getTestCategoryInfos() {
        mModel?.getTestCategoryInfos()?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<CourseInfoWrapper>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<CourseInfoWrapper>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showEmotionCategorys(t.data.list)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })
    }

    fun getEmotionTestInfos(catId: String?, page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        mModel?.getEmotionTestInfos(catId, page, pageSize)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<List<EmotionTestInfo>>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<List<EmotionTestInfo>>) {
                mView.hideLoadingDialog()
                t.let {

                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showEmotionTestListInfo(t.data)
                    } else {
                        if (page == 1) mView.onNoData()
                    }
                }
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()
                mView.onError()
            }
        })
    }


    fun getTestRecords(page: Int, page_size: Int) {
        val userId = UserInfoHelper.instance.getUid()
        if (page == 1) {
            mView.showLoadingDialog()
        }

        mModel?.getTestRecords("$userId", page, page_size)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<List<EmotionTestInfo>>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<List<EmotionTestInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            mView.showTestRecords(t.data)
                            if (page == 1) {
                                CommonInfoHelper.setO(mContext, t.data, "${userId}test_reports")
                            }
                        } else {
                            if (page == 1) mView.onNoData()
                        }
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

        })
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
        mView.showLoadingDialog()
        mModel?.getTestRecordDetail(record_id)?.subscribe(object : DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<EmotionTestInfo>>() {
            override fun onNext(t: yc.com.rthttplibrary.bean.ResultInfo<EmotionTestInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTestRecordDetail(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

        })
    }


}