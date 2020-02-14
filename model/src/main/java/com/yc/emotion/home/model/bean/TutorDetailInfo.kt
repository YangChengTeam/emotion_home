package com.yc.emotion.home.model.bean

/**
 * Created by suns  on 2019/10/25 17:43.
 */
class TutorDetailInfo {


    /**
     * tutor : {"tutor_id":5,"bg_img":"http://qg.bshu.com/ueditor/php/up/file/20191015/1571133276919190.jpg","name":"胜羽","face":"http://qg.bshu.com/ueditor/php/up/file/20191015/1571133280227644.jpg","profession":"情感咨询师","master":"","applicable":"","willget":"","weixin":"","phone":"","qq":""}
     * certs : [{"id":3,"tutor_id":5,"cert_name":"一级情感护理师","organ":"深圳市景安精神关爱基金会","cert_no":"","cert_img":"http://qg.bshu.com/ueditor/php/up/file/20191015/1571133550518891.jpg","name":"洪文鹏","face":"http://qg.bshu.com/ueditor/php/up/file/20191015/1571133530885579.jpg","sort":0,"create_time":0,"update_time":0}]
     * company : {"id":2,"tutor_id":5,"company_name":"武汉执子之手信息咨询有限责任公司","corporation":"洪文鹏","business_license":"91420106MA4KYJ51X1","image":"http://qg.bshu.com/ueditor/php/up/file/20191015/1571133726964435.jpg"}
     */

    var tutor: TutorInfo? = null
    var company: TutorInfo? = null
    var certs: List<TutorInfo>? = null


}
