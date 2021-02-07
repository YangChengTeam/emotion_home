package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.OrderInfo
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

/**
 *
 * Created by suns  on 2019/11/18 15:42.
 */
class OrderModel(override var context: Context?) : IModel(context) {


    /**
     * 获取订单列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    fun getOrderList(userId: String, page: Int, pageSize: Int): Flowable<ResultInfo<List<OrderInfo>>> {


        return request.getOrderList(userId, page, pageSize)

    }
}