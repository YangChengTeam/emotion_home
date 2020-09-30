package com.yc.emotion.home.community.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.model.bean.CommunityDetailInfo
import com.yc.emotion.home.model.bean.CommunityInfoWrapper
import com.yc.emotion.home.model.bean.CommunityTagInfoWrapper
import com.yc.emotion.home.model.bean.TopTopicInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import yc.com.rthttplibrary.bean.ResultInfo

import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/14 17:21.
 */
class CommunityModel(override var context: Context?) : IModel(context) {


    /**
     * 获取热门标签
     *
     * @return
     */
    fun getCommunityTagInfos(): Observable<ResultInfo<CommunityTagInfoWrapper>> {
//        return HttpCoreEngin.get(context).rxpost(URLConfig.COMMUNITY_TAG_URL, object : TypeReference<ResultInfo<CommunityTagInfoWrapper>>() {
//
//        }.type, null, true, true, true) as Observable<ResultInfo<CommunityTagInfoWrapper>>

        return request.getCommunityTagInfos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取社区最新发帖
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    fun getCommunityNewstInfos(userId: String, page: Int, pageSize: Int): Observable<ResultInfo<CommunityInfoWrapper>> {
//        val params = HashMap<String, String>()
//        params["user_id"] = userId
//        params["page"] = page.toString()
//        params["page_size"] = pageSize.toString()
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.COMMUNITY_NEWEST_LIST_URL, object : TypeReference<ResultInfo<CommunityInfoWrapper>>() {
//
//        }.type, params, true, true, true) as Observable<ResultInfo<CommunityInfoWrapper>>

        return request.getCommunityNewstInfos(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 帖子点赞
     *
     * @param user_id
     * @param topic_id
     * @return
     */
    fun likeTopic(user_id: String, topic_id: String): Observable<ResultInfo<String>> {
//        val params = HashMap<String, String>()
//        params["user_id"] = user_id
//        params["topic_id"] = topic_id
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.TOPIC_LIKE_URL, object : TypeReference<ResultInfo<String>>() {
//
//        }.type,
//                params, true, true, true) as Observable<ResultInfo<String>>

        return request.likeTopic(user_id, topic_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取相应标签帖子列表
     *
     * @param userId
     * @param catId
     * @param page
     * @param pageSize
     * @return
     */
    fun getCommunityTagListInfo(userId: String, catId: String, page: Int, pageSize: Int): Observable<ResultInfo<CommunityInfoWrapper>> {

//        val params = HashMap<String, String>()
//
//        params["user_id"] = userId
//        params["cat_id"] = catId
//        params["page"] = page.toString()
//        params["page_size"] = pageSize.toString()
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.COMMUNITY_TAG_LIST_URL, object : TypeReference<ResultInfo<CommunityInfoWrapper>>() {
//
//        }.type,
//                params, true, true, true) as Observable<ResultInfo<CommunityInfoWrapper>>

        return request.getCommunityTagListInfo(userId, catId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 获取热门帖子
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */

    fun getCommunityHotList(userId: String, page: Int, pageSize: Int): Observable<ResultInfo<CommunityInfoWrapper>> {
//        val params = HashMap<String, String>()
//        params["user_id"] = userId
//
//        params["page"] = page.toString()
//        params["page_size"] = pageSize.toString()
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.COMMUNITY_HOT_LIST_URL, object : TypeReference<ResultInfo<CommunityInfoWrapper>>() {
//
//        }.type, params, true, true, true) as Observable<ResultInfo<CommunityInfoWrapper>>

        return request.getCommunityHotList(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 我的发帖
     *
     * @param userId
     * @return
     */
    fun getMyCommunityInfos(userId: String, page: Int, pageSize: Int): Observable<ResultInfo<CommunityInfoWrapper>> {
//
//        val params = HashMap<String, String>()
//
//        params["user_id"] = userId
//        params["page"] = page.toString()
//        params["page_size"] = pageSize.toString()
//        return HttpCoreEngin.get(context).rxpost(URLConfig.COMMUNITY_MY_URL, object : TypeReference<ResultInfo<CommunityInfoWrapper>>() {
//
//        }.type, params, true, true, true) as Observable<ResultInfo<CommunityInfoWrapper>>

        return request.getMyCommunityInfos(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * 删除发帖
     *
     * @param comment_id
     * @param userId
     * @return
     * http://qinggan.site/api/topic.topic/delete?debug=qwe123&topic_id=236
     */
    fun deleteTopic(comment_id: String?, userId: String): Observable<ResultInfo<String>> {
//        val params = HashMap<String, String?>()
//
//        params["topic_id"] = comment_id
//        params["user_id"] = userId
//        return HttpCoreEngin.get(context).rxpost(URLConfig.DELETE_TOPIC_URL, object : TypeReference<ResultInfo<String>>() {
//
//        }.type, params, true,
//                true, true) as Observable<ResultInfo<String>>

        return request.deleteTopic(comment_id, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 帖子详情
     *
     * @param topicId
     * @return
     */
    fun getCommunityDetailInfo(userId: String, topicId: String?): Observable<ResultInfo<CommunityDetailInfo>> {
//        val params = HashMap<String, String?>()
//        params["user_id"] = userId
//
//        params["topic_id"] = topicId
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.TOPIC_DETAIL_URL, object : TypeReference<ResultInfo<CommunityDetailInfo>>() {
//
//        }.type, params, true, true, true) as Observable<ResultInfo<CommunityDetailInfo>>

        return request.getCommunityDetailInfo(userId, topicId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 评论点赞
     *
     * @param userId
     * @return
     */
    fun commentLike(userId: String, commentId: String): Observable<ResultInfo<String>> {

//        val params = HashMap<String, String>()
//        params["user_id"] = userId
//        params["comment_id"] = commentId
//        return HttpCoreEngin.get(context).rxpost(URLConfig.COMMENT_LIKE_URL, object : TypeReference<ResultInfo<String>>() {
//
//        }.type, params, true, true, true) as Observable<ResultInfo<String>>

        return request.commentLike(userId, commentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }


    /**
     * 创建评论
     *
     * @param user_id
     * @param topicId
     * @param content
     * @return
     */
    fun createComment(user_id: String, topicId: String?, content: String?): Observable<ResultInfo<String>> {
//        val params = HashMap<String, String?>()
//        params["user_id"] = user_id
//        params["topic_id"] = topicId
//        params["content"] = content
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.CREATE_COMMENT_URL, object : TypeReference<ResultInfo<String>>() {
//
//        }.type,
//                params, true, true, true) as Observable<ResultInfo<String>>

        return request.createComment(user_id, topicId, content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 公告
     *
     * @return
     */

    fun getTopTopicInfos(): Observable<ResultInfo<TopTopicInfo>> {

//        return HttpCoreEngin.get(context).rxpost(URLConfig.TOP_TOPIC_URL, object : TypeReference<ResultInfo<TopTopicInfo>>() {
//
//        }.type, null, true, true, true) as Observable<ResultInfo<TopTopicInfo>>

        return request.getTopTopicInfos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 发帖
     *
     * @param userId
     * @param cat_id
     * @param content
     * @return
     */
    fun publishCommunityInfo(userId: String, cat_id: String?, content: String): Observable<ResultInfo<String>> {
//        val params = HashMap<String, String?>()
//
//        params["user_id"] = userId
//        params["cat_id"] = cat_id
//        params["content"] = content
//
//        return HttpCoreEngin.get(context).rxpost(URLConfig.PUBLISH_COMMUNITY_URL, object : TypeReference<ResultInfo<String>>() {
//
//        }.type,
//                params, true, true, true) as Observable<ResultInfo<String>>

        return request.publishCommunityInfo(userId, cat_id, content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}