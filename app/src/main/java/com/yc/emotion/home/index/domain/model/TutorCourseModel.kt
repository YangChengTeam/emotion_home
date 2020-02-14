package com.yc.emotion.home.index.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.CourseInfoWrapper
import com.yc.emotion.home.model.bean.TutorCommentInfoWrapper
import com.yc.emotion.home.model.bean.TutorCourseDetailInfo
import rx.Observable
import java.util.ArrayList
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/12 15:54.
 */
class TutorCourseModel(override var context: Context?) : IModel {


    /**
     * 获取课程详情
     *
     * @param chapter_id
     * @param user_id
     * @return
     */
    fun getCourseInfo(chapter_id: String?, user_id: String): Observable<ResultInfo<TutorCourseDetailInfo>> {
        val params = HashMap<String, String?>()

        params["chapter_id"] = chapter_id
        params["user_id"] = user_id

        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_DETAIL_URL, object : TypeReference<ResultInfo<TutorCourseDetailInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<TutorCourseDetailInfo>>
    }


    /**
     * 获取导师评价列表
     *
     * @param tutor_id
     * @param page
     * @param pageSize
     * @return
     */
    fun getTutorCommentInfos(tutor_id: String?, page: Int, pageSize: Int): Observable<ResultInfo<TutorCommentInfoWrapper>> {
        val params = HashMap<String, String?>()
        params["tutor_id"] = tutor_id
        params["page"] = page.toString() + ""
        params["page_size"] = pageSize.toString() + ""

        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_COMMENT_LIST_URL, object : TypeReference<ResultInfo<TutorCommentInfoWrapper>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<TutorCommentInfoWrapper>>

    }

    /**
     * 收藏或取消收藏课程
     *
     * @param chapter_id
     * @param userId
     * @return
     */
    fun collectCourse(chapter_id: String?, userId: String): Observable<ResultInfo<List<CourseInfo>>> {
        val params = HashMap<String, String?>()
        params["chapter_id"] = chapter_id
        params["user_id"] = userId

        return HttpCoreEngin.get(context).rxpost(URLConfig.COLLECT_COURSE_URL, object : TypeReference<ResultInfo<List<CourseInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<CourseInfo>>>
    }

    /**
     * 课程分类
     *
     * @return
     */
    fun getCourseCategory(): Observable<ResultInfo<ArrayList<CourseInfo>>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_CATEGORY_URL, object : TypeReference<ResultInfo<ArrayList<CourseInfo>>>() {

        }.type, null, true, true, true) as Observable<ResultInfo<ArrayList<CourseInfo>>>
    }


    /**
     * 获取课程列表
     *
     * @param cat_id
     * @return
     */
    fun getCourseList(cat_id: String?): Observable<ResultInfo<List<CourseInfo>>> {
        val params = HashMap<String, String?>()
        params["cat_id"] = cat_id

        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_LIST_URL, object : TypeReference<ResultInfo<List<CourseInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<CourseInfo>>>
    }

    /**
     * 获取导师课程列表
     *
     * @param tutor_id
     * @param page
     * @param pageSize
     * @return
     */
    fun getTutorCourseInfos(tutor_id: String?, page: Int, pageSize: Int): Observable<ResultInfo<CourseInfoWrapper>> {

        val params = HashMap<String, String?>()
        params["tutor_id"] = tutor_id
        params["page"] = page.toString() + ""
        params["page_size"] = pageSize.toString() + ""
        return HttpCoreEngin.get(context).rxpost(URLConfig.TUTOR_COURSE_DETAIL_URL, object : TypeReference<ResultInfo<CourseInfoWrapper>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<CourseInfoWrapper>>
    }
}