package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.*

/**
 *
 * Created by suns  on 2019/11/12 16:50.
 */
interface TutorView : IView, IDialog, StateDefaultImpl {
    fun showTutorDetailInfo(data: TutorDetailInfo?) {}
    fun showTutorServiceInfos(data: List<TutorServiceInfo>?) {}
    fun showTutorServiceDetailInfo(data: TutorServiceDetailInfo?) {}
    fun showTutorServiceOrder(payWayName: String, data: OrdersInitBean) {}
    fun showTutorApitude(data: TutorInfoWrapper?) {}
    fun showTutorCategory(data: List<CourseInfo>?) {}
    fun showTutorListInfos(data: List<TutorInfo>?) {}

}