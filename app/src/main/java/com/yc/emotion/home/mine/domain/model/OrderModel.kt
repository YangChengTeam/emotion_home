package com.yc.emotion.home.mine.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.OrderInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/18 15:42.
 */
class OrderModel(override var context: Context?) : IModel {


    /**
     * 获取订单列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    fun getOrderList(userId: String, page: Int, pageSize: Int): Observable<ResultInfo<List<OrderInfo>>> {

        //        user_id=2&page=1&page_size=10

        val params = HashMap<String, String>()

        params["user_id"] = userId

        params["page"] = page.toString() + ""
        params["page_size"] = pageSize.toString() + ""

        return HttpCoreEngin.get(context).rxpost(URLConfig.ORDER_LIST_URL, object : TypeReference<ResultInfo<List<OrderInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<OrderInfo>>>

    }
}