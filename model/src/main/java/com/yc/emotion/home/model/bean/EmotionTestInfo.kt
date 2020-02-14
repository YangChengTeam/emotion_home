package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by suns  on 2019/10/11 09:29.
 * 情感测试相关信息
 */
class EmotionTestInfo {

    @JSONField(name = "test_id")
    var id: String? = null
    @JSONField(name = "image")
    var img: String? = null
    @JSONField(name = "subject")
    var title: String? = null

    var people: Int = 0//测试人数

    var desp: String? = null

    var need_pay: Int = 0

    var goods_id: String? = null

    var answer: String? = null

    var type: Int = 0//情感测试类型1跳转 2得分

    var record_id: String? = null

    var option_id: String? = null

}