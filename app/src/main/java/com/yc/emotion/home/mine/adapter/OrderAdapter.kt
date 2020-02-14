package com.yc.emotion.home.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.OrderInfo
import com.yc.emotion.home.utils.DateUtils

/**
 *
 * Created by suns  on 2019/10/24 11:08.
 */
class OrderAdapter(mDatas: List<OrderInfo>?) : BaseQuickAdapter<OrderInfo, BaseViewHolder>(R.layout.layout_order_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: OrderInfo?) {

        helper?.let {
            item?.let {

                helper.setText(R.id.tv_order_sn, "订单号：${item.orderSn}")
                        .setText(R.id.tv_order_date, DateUtils.formatTimeToStr(item.add_time, "yyyy/MM/dd"))
                        .setText(R.id.tv_order_title, item.title)
                        .setText(R.id.tv_order_price, "¥${item.money}")
                        .setText(R.id.tv_order_state, item.getStatusStr())
            }
        }
    }
}