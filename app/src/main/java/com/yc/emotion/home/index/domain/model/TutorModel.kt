package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.engine.OrderEngine
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.*
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/12 16:48.
 */
class TutorModel(override var context: Context?) : IModel, OrderEngine(context) {


    /**
     * 获取导师详情
     *
     * @param tutor_id
     * @return
     */
    fun getTutorDetailInfo(tutor_id: String?): Observable<ResultInfo<TutorDetailInfo>> {
        val params = HashMap<String, String?>()

        params["tutor_id"] = tutor_id

        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_DETAIL_URL, object : TypeReference<ResultInfo<TutorDetailInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<TutorDetailInfo>>

    }


    /**
     * 获取导师服务列表
     *
     * @param tutor_id
     * @param page
     * @param page_sie
     * @return
     */
    fun getTutorServices(tutor_id: String?, page: Int, page_sie: Int): Observable<ResultInfo<List<TutorServiceInfo>>> {
        val params = HashMap<String, String?>()

        params["tutor_id"] = tutor_id
        params["page"] = page.toString() + ""
        params["page_sie"] = page_sie.toString() + ""

        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_SERVICE_LIST_URL, object : TypeReference<ResultInfo<List<TutorServiceInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<TutorServiceInfo>>>


    }


    /**
     * 获取导师服务详情
     *
     * @param service_id
     * @return
     */
    fun getTutorServiceDetailInfo(service_id: String?): Observable<ResultInfo<TutorServiceDetailInfo>> {
        val params = HashMap<String, String?>()
        params["service_id"] = service_id
        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_SERVICE_DEATAIL_URL, object : TypeReference<ResultInfo<TutorServiceDetailInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<TutorServiceDetailInfo>>
    }


    /**
     * 导师相关证书
     *
     * @param tutor_id
     * @return
     */
    fun getApitudeInfo(tutor_id: String?): Observable<ResultInfo<TutorInfoWrapper>> {
        val params = HashMap<String, String?>()
        params["tutor_id"] = tutor_id

        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_CERTS_URL, object : TypeReference<ResultInfo<TutorInfoWrapper>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<TutorInfoWrapper>>
    }

    /**
     * 获取导师分类
     *
     * @return
     */
    fun getTutorCategory(): Observable<ResultInfo<List<CourseInfo>>> {

        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_CATGORY_URL, object : TypeReference<ResultInfo<List<CourseInfo>>>() {

        }.type, null, true, true, true) as Observable<ResultInfo<List<CourseInfo>>>

    }

    /**
     * 获取导师列表
     *
     * @param page
     * @param pageSize
     * @param catid
     * @return
     */
    fun getTutorListInfo(catid: String?, page: Int, pageSize: Int): Observable<ResultInfo<List<TutorInfo>>> {
        val params = HashMap<String, String?>()

        params["page"] = page.toString() + ""
        params["page_size"] = pageSize.toString() + ""
        params["cat_id"] = catid

        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_LIST_URL, object : TypeReference<ResultInfo<List<TutorInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<TutorInfo>>>

    }
}