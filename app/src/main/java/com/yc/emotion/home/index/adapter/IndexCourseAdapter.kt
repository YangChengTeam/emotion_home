package com.yc.emotion.home.index.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.model.bean.CourseInfo
import yc.com.rthttplibrary.util.ScreenUtil

/**
 *
 * Created by suns  on 2019/9/27 17:19.
 */
class IndexCourseAdapter(courseInfos: ArrayList<CourseInfo>?) : CommonMoreAdapter<CourseInfo, BaseViewHolder>(R.layout.index_course_item, courseInfos) {
    override fun convert(helper: BaseViewHolder, item: CourseInfo?) {

            item?.let {
                val roundCornerImg = helper.getView<RoundCornerImg>(R.id.course_roundImg)
                val layoutParams = roundCornerImg.layoutParams
                layoutParams.height = ScreenUtil.dip2px(mContext, 120f)
                roundCornerImg.layoutParams = layoutParams

                Glide.with(mContext).load(item.img)
                        .apply(RequestOptions().error(R.mipmap.home_course_one).diskCacheStrategy(DiskCacheStrategy.DATA))
                        .thumbnail(0.1f).into(roundCornerImg)


                helper.setText(R.id.tv_course_count, "${item.count}人已学习")
                        .setText(R.id.tv_course_title, item.title)
                        .setText(R.id.tv_course_price, item.price)
                        .setText(R.id.tv_course_tutor, item.name)


            }
        }

}