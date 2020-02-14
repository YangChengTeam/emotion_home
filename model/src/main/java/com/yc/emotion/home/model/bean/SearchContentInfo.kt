package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *
 * Created by suns  on 2019/10/15 11:18.
 */
class SearchContentInfo : MultiItemEntity {


    @JSONField(name = "article")
    var articleDetailInfo: ArticleDetailInfo? = null//精选文章

    @JSONField(name = "lesson_chapter")
    var courseInfo: CourseInfo? = null//相关课程

    @JSONField(name = "psych_test")
    var emotionTestInfo: EmotionTestInfo? = null//情感测试

    var tutorServiceInfo: TutorServiceInfo? = null//推荐服务

    var isShow = false//是否显示标题
    var isExtra = false

    var type = ITEM_TYPE_ARTICLE

    override fun getItemType(): Int {
        return type
    }

    companion object {
        val ITEM_TYPE_ARTICLE = 1
        val ITEM_TYPE_COURSE = 2
        val ITEM_TYPE_EMOTION_TEST = 3
        val ITEM_TYPE_TUTOR_SERVICE = 4
        val ITEM_TYPE_DIVIDER = 5
    }

}