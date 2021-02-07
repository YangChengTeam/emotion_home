package com.yc.emotion.home.community.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference

import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.model.bean.CommunityDetailInfo
import com.yc.emotion.home.model.bean.CommunityInfoWrapper
import com.yc.emotion.home.model.bean.CommunityTagInfoWrapper
import com.yc.emotion.home.model.bean.TopTopicInfo
import io.reactivex.Flowable
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
    fun getCommunityTagInfos(): Flowable<ResultInfo<CommunityTagInfoWrapper>> {


        return request.getCommunityTagInfos()
    }


    /**
     * 获取社区最新发帖
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    fun getCommunityNewstInfos(userId: String, page: Int, pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>> {


        return request.getCommunityNewstInfos(userId, page, pageSize)
    }

    /**
     * 帖子点赞
     *
     * @param user_id
     * @param topic_id
     * @return
     */
    fun likeTopic(user_id: String, topic_id: String): Flowable<ResultInfo<String>> {


        return request.likeTopic(user_id, topic_id)
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
    fun getCommunityTagListInfo(userId: String, catId: String, page: Int, pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>> {


        return request.getCommunityTagListInfo(userId, catId, page, pageSize)
    }


    /**
     * 获取热门帖子
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */

    fun getCommunityHotList(userId: String, page: Int, pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>> {


        return request.getCommunityHotList(userId, page, pageSize)
    }


    /**
     * 我的发帖
     *
     * @param userId
     * @return
     */
    fun getMyCommunityInfos(userId: String, page: Int, pageSize: Int): Flowable<ResultInfo<CommunityInfoWrapper>> {


        return request.getMyCommunityInfos(userId, page, pageSize)

    }

    /**
     * 删除发帖
     *
     * @param comment_id
     * @param userId
     * @return
     * http://qinggan.site/api/topic.topic/delete?debug=qwe123&topic_id=236
     */
    fun deleteTopic(comment_id: String?, userId: String): Flowable<ResultInfo<String>> {


        return request.deleteTopic(comment_id, userId)
    }


    /**
     * 帖子详情
     *
     * @param topicId
     * @return
     */
    fun getCommunityDetailInfo(userId: String, topicId: String?): Flowable<ResultInfo<CommunityDetailInfo>> {


        return request.getCommunityDetailInfo(userId, topicId)
    }

    /**
     * 评论点赞
     *
     * @param userId
     * @return
     */
    fun commentLike(userId: String, commentId: String): Flowable<ResultInfo<String>> {


        return request.commentLike(userId, commentId)

    }


    /**
     * 创建评论
     *
     * @param user_id
     * @param topicId
     * @param content
     * @return
     */
    fun createComment(user_id: String, topicId: String?, content: String?): Flowable<ResultInfo<String>> {

        return request.createComment(user_id, topicId, content)
    }


    /**
     * 公告
     *
     * @return
     */

    fun getTopTopicInfos(): Flowable<ResultInfo<TopTopicInfo>> {

        return request.getTopTopicInfos()
    }


    /**
     * 发帖
     *
     * @param userId
     * @param cat_id
     * @param content
     * @return
     */
    fun publishCommunityInfo(userId: String, cat_id: String?, content: String): Flowable<ResultInfo<String>> {


        return request.publishCommunityInfo(userId, cat_id, content)
    }
}