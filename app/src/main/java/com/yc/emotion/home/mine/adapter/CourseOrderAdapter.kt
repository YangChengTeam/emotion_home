package com.yc.emotion.home.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.OrderInfo

/**
 *
 * Created by suns  on 2019/10/17 13:35.
 */
class CourseOrderAdapter(mDatas: List<OrderInfo>?) : CommonMoreAdapter<OrderInfo, BaseViewHolder>(R.layout.layout_course_order_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: OrderInfo?) {

        helper?.let {
            item?.let {
                helper.addOnClickListener(R.id.tv_course_order_comment)
                        .addOnClickListener(R.id.tv_course_apply_refund)
                        .addOnClickListener(R.id.iv_course_order_more)
            }
        }
    }
}