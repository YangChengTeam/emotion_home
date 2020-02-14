package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by suns  on 2019/10/12 17:06.
 * 导师服务信息
 *
 */
class TutorServiceInfo {

    var id: String? = null

    @JSONField(name = "image")
    var img: String? = null

    var name: String? = null

    @JSONField(name = "m_price")
    var price: String? = null


    var buy_count: Int = 0

    var comment_count: Int = 0


    var desp: String? = null

    var good_num: Int = 0

    var buy_num: Int = 0

    var content: String? = null

    @JSONField(name = "service_day")
    var serviceday: Int = 0
}