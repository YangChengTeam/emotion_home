package com.yc.emotion.home.index.presenter

import android.content.Context
import android.view.View
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R.id.swipeRefreshLayout
import com.yc.emotion.home.R.id.top_empty_view
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.index.domain.model.TutorCourseModel
import com.yc.emotion.home.index.view.TutorCourseView
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.CourseInfoWrapper
import com.yc.emotion.home.model.bean.TutorCommentInfoWrapper
import com.yc.emotion.home.model.bean.TutorCourseDetailInfo
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_collect_view.*
import rx.Subscriber
import java.util.ArrayList

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
        val subscription = mModel?.getCourseInfo(chapter_id, "$userId")?.subscribe(object : Subscriber<ResultInfo<TutorCourseDetailInfo>>() {
            override fun onNext(t: ResultInfo<TutorCourseDetailInfo>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCourseDetailInfo(t.data)
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

    /**
     * 获取课程导师评论
     */
    fun getTutorCommentInfos(tutor_id: String?, page: Int, pageSize: Int) {
        val subscription = mModel?.getTutorCommentInfos(tutor_id, page, pageSize)?.subscribe(object : Subscriber<ResultInfo<TutorCommentInfoWrapper>>() {
            override fun onNext(t: ResultInfo<TutorCommentInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showTutorCommentInfos(t.data.comment_list)
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


    /**
     * 收藏/取消收藏 课程
     */
    fun collectCourse(chapter_id: String?, isCollect: Int) {
        val userId = UserInfoHelper.instance.getUid()
        mView.showLoadingDialog()
        val subscription = mModel?.collectCourse(chapter_id, "$userId")?.subscribe(object : Subscriber<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        var isCollect = isCollect
                        if (isCollect == 0) isCollect = 1
                        else if (isCollect == 1) isCollect = 0
                        mView.setCollectState(isCollect)
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


    fun getCourseCategory() {
        val subscription = mModel?.getCourseCategory()?.subscribe(object : Subscriber<ResultInfo<ArrayList<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<ArrayList<CourseInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showCourseCategory(t.data)
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

    fun getCourseList(cat_id: String?) {
        mView.showLoadingDialog()
        val subscription = mModel?.getCourseList(cat_id)?.subscribe(object : Subscriber<ResultInfo<List<CourseInfo>>>() {
            override fun onNext(t: ResultInfo<List<CourseInfo>>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK) {
                        if (t.data != null && t.data.isNotEmpty()) {
                            mView.showCourseListInfo(t.data)
                        } else {
                            mView.onNoData()
                        }

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


    fun getTutorCourseInfos(tutor_id: String?, page: Int, pageSize: Int) {
        if (page==1)
            mView.showLoadingDialog()
        val subscription = mModel?.getTutorCourseInfos(tutor_id, page, pageSize)?.subscribe(object : Subscriber<ResultInfo<CourseInfoWrapper>>() {
            override fun onNext(t: ResultInfo<CourseInfoWrapper>?) {
                t?.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.lessons != null && t.data.lessons!!.isNotEmpty()) {
                        mView.showTutorCourseInfos(t.data.lessons)
                    } else {
                        if (page == 1) mView.onNoData()
                    }
                }
            }

            override fun onCompleted() {
                if (page==1) mView.hideLoadingDialog()
                mView.onComplete()
            }

            override fun onError(e: Throwable?) {

            }

        })
        subScriptions?.add(subscription)

    }
}