package com.yc.emotion.home.mine.view

import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.base.view.StateDefaultImpl
import com.yc.emotion.home.model.bean.OrderInfo
import yc.com.rthttplibrary.view.IDialog

/**
 *
 * Created by suns  on 2019/11/18 15:42.
 */
interface OrderView : IView, IDialog,StateDefaultImpl {
    fun showOrderList(data: List<OrderInfo>?)
}