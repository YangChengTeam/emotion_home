package com.yc.emotion.home.index.domain.model

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.CourseInfoWrapper
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo
import com.yc.emotion.home.model.bean.QuestionInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo
import java.util.*

/**
 *
 * Created by suns  on 2019/11/12 14:42.
 */
class EmotionTestModel(override var context: Context?) : IModel(context) {

    /**
     * 获取情感测试详情
     *
     * @param user_id
     * @param test_id
     * @return
     */
    fun getTestDetailInfo(user_id: String, test_id: String?): Observable<ResultInfo<EmotionTestTopicInfo>> {


        return request.getTestDetailInfo(user_id, test_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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


        return request.submitAnswer(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * 测试分类
     *
     * @return
     */
    fun getTestCategoryInfos(): Observable<ResultInfo<CourseInfoWrapper>> {


        return request.getTestCategoryInfos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 情感测试列表
     *
     * @return
     */
    fun getEmotionTestInfos(catId: String?, page: Int, pageSize: Int): Observable<ResultInfo<List<EmotionTestInfo>>> {


        return request.getEmotionTestInfos(catId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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


        return request.getTestRecords(userId, page, page_size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取测试记录详情
     *
     * @param record_id
     * @return
     */

    fun getTestRecordDetail(record_id: String?): Observable<ResultInfo<EmotionTestInfo>> {


        return request.getTestRecordDetail(record_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}