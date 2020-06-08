package com.yc.emotion.home.index.adapter

import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.DateUtils
import java.math.BigDecimal

/**
 *
 * Created by suns  on 2019/10/9 14:02.
 */
class EfficientCourseAdapter(mDatas: List<CourseInfo>?) : CommonMoreAdapter<CourseInfo, BaseViewHolder>(R.layout.item_course_efficient, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: CourseInfo?) {
        helper?.let {
            item?.let {
                val bd = BigDecimal(item.price).toInt()
                val roundCornerImg = helper.getView<RoundCornerImg>(R.id.efficient_roundCornerImg)
                roundCornerImg.setCorner(20)
                Glide.with(mContext).load(item.img).apply(RequestOptions().error(R.mipmap.efficient_course_example_pic)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)).thumbnail(0.1f).into(roundCornerImg)

                helper.setText(R.id.tv_efficient_course_title, item.title)
                        .setText(R.id.tv_efficient_price, "$bd/年")
                        .setText(R.id.tv_efficient_teacher_name, item.name)
                        .setText(R.id.tv_efficient_teach_count, "${item.count}人已学习")

                try {
                    val duration = item.duration

                    helper.setText(R.id.tv_efficient_date, "时长：${DateUtils.formatTimeToStr(duration, "mm:ss")}")


                } catch (e: Exception) {
                    Log.e("TAG", "时长不正确：${e.message}")
                }

            }
        }
    }
}