package com.yc.emotion.home.base.domain.engine

import android.content.Context
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.OrdersInitBean
import rx.Observable
import java.util.*

/**
 * Created by mayn on 2019/5/14.
 */

open class OrderEngine(context: Context?) : BaseEngine(context) {


    fun initOrders(userId: String, pay_way_name: String, money: String, title: String, goodId: String): Observable<AResultInfo<OrdersInitBean>> {
        val params = HashMap<String, String>()


        params["user_id"] = userId
        params["pay_way_name"] = pay_way_name
        params["money"] = money


        params["title"] = title //订单标题，会员购买，商品购买等



       val jsonListArray= JSONArray()

        val goodParams = hashMapOf<String, String>()


        goodParams["goods_id"] = goodId

        goodParams["num"] = "1"

        jsonListArray.add(goodParams)

        params["goods_list"] = jsonListArray.toJSONString()
        val httpCoreEngin = HttpCoreEngin.get(mContext)
        return httpCoreEngin.rxpost(URLConfig.ORDER_URL, object : TypeReference<AResultInfo<OrdersInitBean>>() {
        }.type, params,
                true,
                true, true) as Observable<AResultInfo<OrdersInitBean>>
    }


    /**
     * 获取商品列表
     * @param id
     * @return
     */
    fun getGoodListInfo(): Observable<AResultInfo<List<GoodsInfo>>> {
        val params = HashMap<String, String>()

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.GOOD_LIST_URL, object : TypeReference<AResultInfo<List<GoodsInfo>>>() {

        }.type, params, true, true, true) as Observable<AResultInfo<List<GoodsInfo>>>
    }

}
