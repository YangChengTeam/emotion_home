package com.yc.emotion.home.index.domain.model

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.CourseInfoWrapper
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo
import com.yc.emotion.home.model.bean.QuestionInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/12 14:42.
 */
class EmotionTestModel(override var context: Context?) : IModel {

    /**
     * 获取情感测试详情
     *
     * @param user_id
     * @param test_id
     * @return
     */
    fun getTestDetailInfo(user_id: String, test_id: String?): Observable<ResultInfo<EmotionTestTopicInfo>> {

        val params = HashMap<String, String?>()
        params["user_id"] = user_id
        params["test_id"] = test_id

        return HttpCoreEngin.get(context).rxpost(URLConfig.TEST_DETAIL_URL, object : TypeReference<ResultInfo<EmotionTestTopicInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<EmotionTestTopicInfo>>

    }


    /**
     * 提交测试答案
     *
     * @param userId
     * @param test_id
     * @param questionInfos
     * @return
     */
    fun submitAnswer(userId: String, test_id: String?, questionInfos: List<QuestionInfo>?, aid: String?, option_id: String?): Observable<ResultInfo<EmotionTestInfo>> {
        val params = HashMap<String, String?>()
        params["test_id"] = test_id
        if (questionInfos != null)
            params["answer"] = JSON.toJSONString(questionInfos)
        params["user_id"] = userId
        if (!TextUtils.isEmpty(aid)) {
            params["aid"] = aid
        }
        if (!TextUtils.isEmpty(option_id)) {
            params["option_id"] = option_id
        }

        return HttpCoreEngin.get(context).rxpost(URLConfig.TEST_SUBMIT_ANSWER_URL, object : TypeReference<ResultInfo<EmotionTestInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<EmotionTestInfo>>

    }

    /**
     * 测试分类
     *
     * @return
     */
    fun getTestCategoryInfos(): Observable<ResultInfo<CourseInfoWrapper>> {
        val params = HashMap<String, String>()
        return HttpCoreEngin.get(context).rxpost(URLConfig.TEST_CATEGORY_URL, object : TypeReference<ResultInfo<CourseInfoWrapper>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<CourseInfoWrapper>>
    }


    /**
     * 情感测试列表
     *
     * @return
     */
    fun getEmotionTestInfos(catId: String?, page: Int, pageSize: Int): Observable<ResultInfo<List<EmotionTestInfo>>> {
        val params = HashMap<String, String?>()

        params["page"] = page.toString() + ""

        params["page_size"] = pageSize.toString() + ""
        params["cat_id"] = catId

        return HttpCoreEngin.get(context).rxpost(URLConfig.TEST_LIST_URL, object : TypeReference<ResultInfo<List<EmotionTestInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<EmotionTestInfo>>>
    }


    /**
     * 获取测试报告记录
     *
     * @param userId
     * @param page
     * @param page_size
     * @return
     */
    fun getTestRecords(userId: String, page: Int, page_size: Int): Observable<ResultInfo<List<EmotionTestInfo>>> {
        val params = HashMap<String, String>()
        params["user_id"] = userId
        params["page"] = page.toString() + ""
        params["page_size"] = page_size.toString() + ""
        return HttpCoreEngin.get(context).rxpost(URLConfig.TEST_RECORDS_URL, object : TypeReference<ResultInfo<List<EmotionTestInfo>>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<List<EmotionTestInfo>>>
    }


    /**
     * 获取测试记录详情
     *
     * @param record_id
     * @return
     */

    fun getTestRecordDetail(record_id: String?): Observable<ResultInfo<EmotionTestInfo>> {
        val params = HashMap<String, String?>()
        params["record_id"] = record_id
        return HttpCoreEngin.get(context).rxpost(URLConfig.TEST_RECORD_DETAIL_URL, object : TypeReference<ResultInfo<EmotionTestInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<EmotionTestInfo>>
    }

}