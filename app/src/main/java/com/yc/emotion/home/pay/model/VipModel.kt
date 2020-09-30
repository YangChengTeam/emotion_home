package com.yc.emotion.home.pay.model

import android.content.Context
import com.alibaba.fastjson.JSONArray
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import com.yc.emotion.home.utils.UserInfoHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo
import java.util.*

/**
 *
 * Created by suns  on 2019/11/13 16:26.
 */
class VipModel(override var context: Context?) : IModel(context) {
    fun initOrders(userId: String, pay_way_name: String, money: String, title: String, goodId: String): Observable<ResultInfo<OrdersInitBean>> {
        val params = HashMap<String, String>()


        params["user_id"] = userId
        params["pay_way_name"] = pay_way_name
        params["money"] = money


        params["title"] = title //订单标题，会员购买，商品购买等


        val jsonListArray = JSONArray()

        val goodParams = hashMapOf<String, String>()


        goodParams["goods_id"] = goodId

        goodParams["num"] = "1"

        jsonListArray.add(goodParams)

        params["goods_list"] = jsonListArray.toJSONString()

        return request.initOrders(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取商品列表
     * @param id
     * @return
     */
    fun getGoodListInfo(): Observable<ResultInfo<List<GoodsInfo>>> {


        return request.getGoodListInfo("${UserInfoHelper.instance.getUid()}").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}