package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 * Created by suns  on 2019/10/22 15:42.
 */
class IndexInfo {


    var tutors: List<TutorInfo>? = null//导师
    var lesson_chapter: List<CourseInfo>? = null//课程信息
    var psych_test: List<EmotionTestInfo>? = null
    var article: List<ArticleDetailInfo>? = null

    var banners: List<BannerInfo>? = null
    var message: DailySentence? = null


}

class DailySentence {
    var add_time: Long = 0
    var id: Int = 0
    var img: String? = null
    @JSONField(name = "message_img_id")
    var messageImgId: Int = 0
    var new_time: Long = 0
    var read: Int = 0
    var text: String? = null

    override fun toString(): String {
        return "DailySentence(add_time=$add_time, id=$id, img=$img, messageImgId=$messageImgId, new_time=$new_time, read=$read, text=$text)"
    }


}
