package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.LessonInfo
import com.yc.emotion.home.utils.UserInfoHelper

/**
 *
 * Created by suns  on 2019/10/10 15:28.
 */
class TutorCoursePpAdapter(courseInfos: List<LessonInfo>?) : BaseQuickAdapter<LessonInfo, BaseViewHolder>(R.layout.layout_tutor_course_pp_item, courseInfos) {

    private var uId: Int = 0

    init {
        uId = UserInfoHelper.instance.getUid() as Int
    }


    override fun convert(helper: BaseViewHolder?, item: LessonInfo?) {

        helper?.let {
            item?.let {

                helper.setText(R.id.tv_course_title, item.lesson_title)
                val pos = helper.adapterPosition

                if (pos == 0 || uId > 0) {
                    helper.setGone(R.id.iv_course_pp_lock, false)
                } else {
                    helper.setGone(R.id.iv_course_pp_lock, true)
                }

            }
        }
    }
}