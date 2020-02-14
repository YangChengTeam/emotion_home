package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by suns  on 2019/10/10 18:31.
 * 导师信息
 */
class TutorInfo {

    /**
     * tutor_id : 1
     * bg_img : http://qg.bshu.com/ueditor/php/up/file/20191015/1571133303134113.jpg
     * name : 初恋
     * face : http://qg.bshu.com/ueditor/php/up/file/20191015/1571133298190102.jpg
     * profession : 情感咨询师
     * master :
     * applicable : 1.感情中不懂ta，想要洞悉两性思维得你 2.感情缺乏激情，感受不到对方得爱的你 3.感情中总争吵，想要拥有甜蜜幸福得你 4.心里放不下ta，想挽回不懂怎么做的你
     * willget :
     * weixin :
     * phone :
     * qq :
     */
    @JSONField(name = "tutor_id")
    var tutorId: String = ""
    @JSONField(name = "face")
    var img: String? = null
    var name: String? = null
    var star: Int = 0
    var desc: String = ""

    var bg_img: String? = null

    var profession: String? = null
    var master: String? = null
    var applicable: String? = null
    var willget: String? = null
    var weixin: String? = null
    var phone: String? = null
    var qq: String? = null

    var content: String? = null

    //导师等级
    var level: Int? = 0


    //导师公司信息
    private var id: Int = 0

    var company_name: String? = null
    var corporation: String? = null
    var business_license: String? = null
    var image: String? = null


    //导师证书信息
    var cert_name: String? = null
    var organ: String? = null
    var cert_no: String? = null
    var cert_img: String? = null

    var sort: Int = 0
    var create_time: Int = 0
    var update_time: Int = 0


}