package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.model.bean.TutorCommentInfo
import com.yc.emotion.home.model.bean.TutorCourseDetailInfo
import java.util.ArrayList

/**
 *
 * Created by suns  on 2019/11/12 15:55.
 */
interface TutorCourseView : IView, IDialog, StateDefaultImpl {
    fun showCourseDetailInfo(data: TutorCourseDetailInfo?) {}
    fun showTutorCommentInfos(comment_list: List<TutorCommentInfo>?) {}
    fun setCollectState(collect: Int) {}
    fun showCourseCategory(data: ArrayList<CourseInfo>?) {}
    fun showCourseListInfo(data: List<CourseInfo>?) {}
    fun showTutorCourseInfos(lessons: List<CourseInfo>?) {}
    fun showOrderInfo(data: OrdersInitBean?, payWayName: String)
}