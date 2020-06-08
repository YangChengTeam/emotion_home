package com.yc.emotion.home.model.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 * Created by suns  on 2019/11/6 16:24.
 */
class UserInfo {


    /**
     * id : 50082
     * name :
     * mobile :
     * nick_name : 小埋
     * face : http://thirdwx.qlogo.cn/mmopen/vi_32/yCITyWC1icMglvQPX7sdibia3Lbyvp21Ekxw0TdsrkcWI84EjHmlnJ0J7lXCns1ciboJ6fmM5VVanBH6lvpUNpFMoQ/132
     * sex : 0
     * birthday :
     * tutor_id : 0
     * age : null
     * signature :
     * profession :
     * vip_type : 1
     * vip_start_time : 1572857100
     * vip_end_time : 1699087500
     * vip_tips : 1
     * is_vip : 1
     * vip_txt : 永久会员
     * inters : []
     * user_id : 50082
     */
    @JSONField(name = "user_id")
    var id: Int = 0
    var name: String? = null
    var mobile: String? = null
    var nick_name: String? = null
    var face: String? = null
    var sex: Int = 0//1 男 2 女
    var birthday: String? = null
    var tutor_id: Int = 0
    var age: String? = null
    var signature: String? = null
    var profession: String? = null
    var vip_type: Int = 0
    var vip_start_time: Int = 0
    var vip_end_time: Int = 0

    /* "vip_tips": 1 已开通
        "vip_tips":  2     已过期
        "vip_tips": 0 未开通*/
    var vip_tips: Int = 0
    var is_vip: Int = 0
    var vip_txt: String? = null

    var inters: List<UserInfo>? = null

    var interested: String? = null

    var pwd: String? = null


    override fun toString(): String {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", mobile='" + mobile + '\''.toString() +
                ", nick_name='" + nick_name + '\''.toString() +
                ", face='" + face + '\''.toString() +
                ", sex=" + sex +
                ", birthday='" + birthday + '\''.toString() +
                ", tutor_id=" + tutor_id +
                ", age='" + age + '\''.toString() +
                ", signature='" + signature + '\''.toString() +
                ", profession='" + profession + '\''.toString() +
                ", vip_type=" + vip_type +
                ", vip_start_time=" + vip_start_time +
                ", vip_end_time=" + vip_end_time +
                ", vip_tips=" + vip_tips +
                ", is_vip=" + is_vip +
                ", vip_txt='" + vip_txt + '\''.toString() +
                ", inters=" + inters +
                '}'.toString()
    }
}
