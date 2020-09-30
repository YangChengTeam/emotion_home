package com.yc.emotion.home.index.presenter

import android.content.Context
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.TutorCourseModel
import com.yc.emotion.home.index.view.TutorCourseView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.observers.DisposableObserver
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig
import yc.com.rthttplibrary.util.LogUtil
import java.util.*

/**
 *
 * Created by suns  on 2019/11/12 15:54.
 */
class TutorCoursePresenter(context: Context?, view: TutorCourseView) : BasePresenter<TutorCourseModel, TutorCourseView>(context, view) {

    init {
        mModel = TutorCourseModel(context)
    }

    override fun loadData(isForceUI: Boolean, isLoading: Boolean) {
    }

    override fun getCache() {
    }


    /**
     * 获取课程信息
     */
    fun getCourseInfo(chapter_id: String?) {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        mModel?.getCourseInfo(chapter_id, "$userId")?.subscribe(object : DisposableObserver<ResultInfo<TutorCourseDetailInfo>>() {
            override fun onNext(t: ResultInfo<TutorCourseDetailInfo>) {

                mView.hideLoadingDialog()
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCourseDetailInfo(t.data)
                    }
                }
            }

            override fun onComplete() {
                mView.hideLoadingDialog()

            }

            override fun onError(e: Throwable) {
                mView.hideLoadingDialog()

            }

        })

    }

    /**
     * 获取课程导师评论
     */
    fun getTutorCommentInfos(tutor_id: String?, page: Int, pageSize: Int) {
        mModel?.getTutorCommentInfos(tutor_id, page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<TutorCommentInfoWrapper>>() {
            override fun onNext(t: ResultInfo<TutorCommentInfoWrapper>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorCommentInfos(t.data.comment_list)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }
        })

    }


    /**
     * 收藏/取消收藏 课程
     */
    fun collectCourse(chapter_id: String?, isCollect: Int) {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        mModel?.collectCourse(chapter_id, "$userId")?.subscribe(object : DisposableObserver<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        var isCollect = isCollect
                        if (isCollect == 0) isCollect = 1
                        else if (isCollect == 1) isCollect = 0
                        mView.setCollectState(isCollect)
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


    fun getCourseCategory() {
        mModel?.getCourseCategory()?.subscribe(object : DisposableObserver<ResultInfo<ArrayList<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<ArrayList<CourseInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCourseCategory(t.data)
                    }
                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

            }

        })

    }

    fun getCourseList(cat_id: String?) {
        mView.showLoadingDialog()
        mModel?.getCourseList(cat_id)?.subscribe(object : DisposableObserver<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            mView.showCourseListInfo(t.data)
                        } else {
                            mView.onNoData()
                        }

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


    fun getTutorCourseInfos(tutor_id: String?, page: Int, pageSize: Int) {
        if (page == 1)
            mView.showLoadingDialog()
        mModel?.getTutorCourseInfos(tutor_id, page, pageSize)?.subscribe(object : DisposableObserver<ResultInfo<CourseInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CourseInfoWrapper>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data.lessons
                        data?.let { datas ->
                            if (datas.isNotEmpty()) {
                                mView.showTutorCourseInfos(datas)
                            } else {
                                if (page == 1) mView.onNoData()
                            }
                        }
                    } else {
                        if (page == 1) mView.onNoData()
                        else {
                        }
                    }
                }
            }

            override fun onComplete() {
                if (page == 1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable) {

            }

        })

    }

    fun initOrders(pay_way_name: String, money: String, title: String, goodId: String) {
        val userId = UserInfoHelper.instance.getUid()

        mView.showLoadingDialog()

        mModel?.initOrders("$userId", pay_way_name, money, title, goodId)?.subscribe(object : DisposableObserver<ResultInfo<OrdersInitBean>>() {
            override fun onComplete() {
                mView.hideLoadingDialog()
            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(t: ResultInfo<OrdersInitBean>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showOrderInfo(t.data, pay_way_name)
                    }
                }
            }

        })

    }
}