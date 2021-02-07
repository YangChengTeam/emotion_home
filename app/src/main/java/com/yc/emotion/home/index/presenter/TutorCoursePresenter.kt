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

        mModel?.getCourseInfo(chapter_id, "$userId")?.getData(mView, { it, _ ->
            it?.let {
                mView.showCourseDetailInfo(it)
            }
        }, { _, _ -> })


    }

    /**
     * 获取课程导师评论
     */
    fun getTutorCommentInfos(tutor_id: String?, page: Int, pageSize: Int) {
        mModel?.getTutorCommentInfos(tutor_id, page, pageSize)?.getData(mView, { it, _ ->
            it?.let {
                mView.showTutorCommentInfos(it.comment_list)
            }
        }, { _, _ -> }, false)
    }


    /**
     * 收藏/取消收藏 课程
     */
    fun collectCourse(chapter_id: String?, isCollect: Int) {
        val userId = UserInfoHelper.instance.getUid()
        mModel?.collectCourse(chapter_id, "$userId")?.getData(mView, { it, _ ->
            it?.let {
                var isCollect = isCollect
                if (isCollect == 0) isCollect = 1
                else if (isCollect == 1) isCollect = 0
                mView.setCollectState(isCollect)
            }
        }, { _, _ -> })


    }


    fun getCourseCategory() {
        mModel?.getCourseCategory()?.getData(mView, { it, _ ->
            it?.let {
                mView.showCourseCategory(it)
            }
        }, { _, _ -> }, false)

    }

    fun getCourseList(cat_id: String?) {

        mModel?.getCourseList(cat_id)?.getData(mView, { it, _ ->
            if (it != null && it.isNotEmpty()) {
                mView.showCourseListInfo(it)
            } else {
                mView.onNoData()
            }
        }, { _, _ -> })
    }


    fun getTutorCourseInfos(tutor_id: String?, page: Int, pageSize: Int) {

        mModel?.getTutorCourseInfos(tutor_id, page, pageSize)?.getData(mView, { it, _ ->
            if (it != null) {
                val data = it.lessons
                data?.let { datas ->
                    if (datas.isNotEmpty()) {
                        mView.showTutorCourseInfos(datas)
                    } else {
                        if (page == 1) mView.onNoData()
                    }
                }
            } else {
                if (page == 1) mView.onNoData()
            }
        }, { _, _ -> mView.onComplete() }, page == 1)

    }

    fun initOrders(pay_way_name: String, money: String, title: String, goodId: String) {
        val userId = UserInfoHelper.instance.getUid()

        mModel?.initOrders("$userId", pay_way_name, money, title, goodId)?.getData(mView,{it,_->
            it?.let {
                mView.showOrderInfo(it, pay_way_name)
            }
        },{_,_->})


    }
}