package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by suns  on 2019/10/17 10:37.
 */
class OrderInfo {

    var id: String? = null

    var img: String? = null

    var title: String? = null

    var service_count: Int = 0//服务天数

    var buy_date: Long = 0//购买日期

    @JSONField(name = "order_sn")
    var orderSn: String? = null

    var type: Int = 0

    var money: String? = null

    @JSONField(name = "payway_name")
    var payWay: String? = null

    var add_time: Long = 0

    var status: Int? = 0


    fun getStatusStr(): String {
        return when (status) {
            0 -> "待支付"
            1 -> "支付中"
            2 -> "取消支付"
            3 -> "支付失败"
            4 -> "支付成功"
            5 -> "发货失败"
            6 -> "交易完成"
            7 -> "申请退货"
            8 -> "退货失败"
            9 -> "退货成功"
            else -> ""
        }
    }


}