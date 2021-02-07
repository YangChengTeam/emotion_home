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
 * Created by suns  on 2019/11/12 15:54.
 */
class TutorCourseModel(override var context: Context?) : IModel(context) {


    /**
     * 获取课程详情
     *
     * @param chapter_id
     * @param user_id
     * @return
     */
    fun getCourseInfo(chapter_id: String?, user_id: String): Flowable<ResultInfo<TutorCourseDetailInfo>> {

        return request.getCourseInfo(chapter_id, user_id)
    }


    /**
     * 获取导师评价列表
     *
     * @param tutor_id
     * @param page
     * @param pageSize
     * @return
     */
    fun getTutorCommentInfos(tutor_id: String?, page: Int, pageSize: Int): Flowable<ResultInfo<TutorCommentInfoWrapper>> {


        return request.getTutorCommentInfos(tutor_id, page, pageSize)

    }

    /**
     * 收藏或取消收藏课程
     *
     * @param chapter_id
     * @param userId
     * @return
     */
    fun collectCourse(chapter_id: String?, userId: String): Flowable<ResultInfo<List<CourseInfo>>> {

        return request.collectCourse(chapter_id, userId)
    }

    /**
     * 课程分类
     *
     * @return
     */
    fun getCourseCategory(): Flowable<ResultInfo<ArrayList<CourseInfo>>> {

        return request.getCourseCategory()
    }


    /**
     * 获取课程列表
     *
     * @param cat_id
     * @return
     */
    fun getCourseList(cat_id: String?): Flowable<ResultInfo<List<CourseInfo>>> {

        return request.getCourseList(cat_id)
    }

    /**
     * 获取导师课程列表
     *
     * @param tutor_id
     * @param page
     * @param pageSize
     * @return
     */
    fun getTutorCourseInfos(tutor_id: String?, page: Int, pageSize: Int): Flowable<ResultInfo<CourseInfoWrapper>> {

        return request.getTutorCourseInfos(tutor_id, page, pageSize)
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