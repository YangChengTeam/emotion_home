package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.JSONArray
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo
import java.util.*

/**
 *
 * Created by suns  on 2019/11/12 16:48.
 */
class TutorModel(override var context: Context?) : IModel(context) {


    /**
     * 获取导师详情
     *
     * @param tutor_id
     * @return
     */
    fun getTutorDetailInfo(tutor_id: String?): Flowable<ResultInfo<TutorDetailInfo>> {

        return request.getTutorDetailInfo(tutor_id)

    }


    /**
     * 获取导师服务列表
     *
     * @param tutor_id
     * @param page
     * @param page_sie
     * @return
     */
    fun getTutorServices(tutor_id: String?, page: Int, page_sie: Int): Flowable<ResultInfo<List<TutorServiceInfo>>> {


        return request.getTutorServices(tutor_id, page, page_sie)


    }


    /**
     * 获取导师服务详情
     *
     * @param service_id
     * @return
     */
    fun getTutorServiceDetailInfo(service_id: String?): Flowable<ResultInfo<TutorServiceDetailInfo>> {


        return request.getTutorServiceDetailInfo(service_id)
    }


    /**
     * 导师相关证书
     *
     * @param tutor_id
     * @return
     */
    fun getApitudeInfo(tutor_id: String?): Flowable<ResultInfo<TutorInfoWrapper>> {


        return request.getApitudeInfo(tutor_id)
    }

    /**
     * 获取导师分类
     *
     * @return
     */
    fun getTutorCategory(): Flowable<ResultInfo<List<CourseInfo>>> {


        return request.getTutorCategory()
    }

    /**
     * 获取导师列表
     *
     * @param page
     * @param pageSize
     * @param catid
     * @return
     */
    fun getTutorListInfo(catid: String?, page: Int, pageSize: Int): Flowable<ResultInfo<List<TutorInfo>>> {

        return request.getTutorListInfo(catid, page, pageSize)
    }

    fun initOrders(userId: String, pay_way_name: String, money: String, title: String, goodId: String): Flowable<ResultInfo<OrdersInitBean>> {
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


        return request.initOrders(params)
    }
}