package com.yc.emotion.home.index.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.model.bean.LessonInfo
import yc.com.rthttplibrary.util.ScreenUtil

/**
 *
 * Created by suns  on 2020/7/29 15:38.
 */
class TutorCourseLessonAdapter(lessons: List<LessonInfo>) : CommonMoreAdapter<LessonInfo, BaseViewHolder>(R.layout.layout_tutor_course_item, lessons) {
    override fun convert(helper: BaseViewHolder, item: LessonInfo?) {

            item?.let {
                helper.setText(R.id.tv_tutor_course, it.lesson_title)
                val view = helper.getView<RoundCornerImg>(R.id.screenshot_image)
                Glide.with(mContext).load(it.lesson_image).apply(RequestOptions().error(R.mipmap.index_tutor_example)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)).into(view)

                val ivCourseLock = helper.getView<ImageView>(R.id.iv_tutor_course_lock)
                val pos = helper.adapterPosition
                val needpay = it.need_pay
                if (pos == 0 || needpay == 0) {
                    ivCourseLock.visibility = View.GONE
                } else {
                    ivCourseLock.visibility = View.VISIBLE
                }
                val rootView = helper.getView<LinearLayout>(R.id.rootView)
                val layoutParams = rootView.layoutParams as LinearLayout.LayoutParams
                layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 8f)
//                layoutParams.width = ScreenUtil.getWidth(mContext) * 9 / 20
                rootView.layoutParams = layoutParams

            }



    }
}