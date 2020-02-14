package com.yc.emotion.home.pay.view

import com.yc.emotion.home.base.view.IDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean

/**
 *
 * Created by suns  on 2019/11/13 16:27.
 */
interface VipView:IView,IDialog{
    fun showGoodInfoList(data: List<GoodsInfo>?)
    fun showOrderInfo(data: OrdersInitBean?, pay_way_name: String)

}