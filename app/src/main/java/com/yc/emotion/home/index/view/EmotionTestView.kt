package com.yc.emotion.home.index.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/12 14:43.
 */
interface EmotionTestView : IView, IDialog,StateDefaultImpl {
    fun showEmotionTestInfo(emotionTestTopicInfo: EmotionTestTopicInfo?) {}
    fun showEmotionTestResult(emotionTestInfo: EmotionTestInfo?) {}
    fun showEmotionCategorys(list: ArrayList<CourseInfo>?) {}
    fun showEmotionTestListInfo(data: List<EmotionTestInfo>?) {}
    fun showTestRecords(data: List<EmotionTestInfo>?){}
    fun showTestRecordDetail(data: EmotionTestInfo?){}
}