package com.yc.emotion.home.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.OrderInfo

/**
 *
 * Created by suns  on 2019/10/17 10:34.
 */
class ServiceOrderAdapter(mDatas: List<OrderInfo>?) : BaseQuickAdapter<OrderInfo, BaseViewHolder>(R.layout.layout_service_order_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: OrderInfo?) {
        helper?.let {
            item?.let {
                //                Log.e("TAG", item.toString())
//                helper.setText(R.id.tv_service_order_title, item.title)
//                        .setText(R.id.tv_service_order_day, "剩余：${item.service_count}")
//                        .setText(R.id.tv_service_order_buy_date, DateUtils.formatTimeToStr(item.buy_date, "yyyy-MM-dd"))
                helper.addOnClickListener(R.id.tv_service_order_comment)
                        .addOnClickListener(R.id.tv_service_apply_refund)
                        .addOnClickListener(R.id.iv_service_order_more)

            }
        }
    }
}