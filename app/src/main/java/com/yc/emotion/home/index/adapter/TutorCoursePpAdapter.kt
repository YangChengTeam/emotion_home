package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.LessonInfo
import com.yc.emotion.home.utils.UserInfoHelper

/**
 *
 * Created by suns  on 2019/10/10 15:28.
 */
class TutorCoursePpAdapter(courseInfos: List<LessonInfo>?) : CommonMoreAdapter<LessonInfo, BaseViewHolder>(R.layout.layout_tutor_course_pp_item, courseInfos) {

//    private var uId: Int = 0
//
//    init {
//        uId = UserInfoHelper.instance.getUid()
//    }


    override fun convert(helper: BaseViewHolder, item: LessonInfo?) {


        item?.let {

            helper.setText(R.id.tv_course_title, item.lesson_title)
                    .setText(R.id.tv_course_duration, item.duration)
//            val pos = helper.adapterPosition

            if (it.need_pay == 0) {
                helper.setGone(R.id.iv_course_pp_lock, false)
            } else {
                helper.setGone(R.id.iv_course_pp_lock, true)
            }

        }

    }
}