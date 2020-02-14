package com.yc.emotion.home.model.bean

/**
 *
 * Created by wanglin  on 2018/11/29 18:27.
 */
class UserAccreditInfo {

    var nickname: String? = null

    var face: String? = null

    var city: String? = null

    var iconUrl: String? = null

    var gender: String? = null

    var province: String? = null

    var openid: String? = null

    var accessToken: String? = null

    var uid: String? = null
    override fun toString(): String {
        return "UserAccreditInfo(nickname=$nickname, face=$face, city=$city, iconUrl=$iconUrl, gender=$gender, province=$province, openid=$openid, accessToken=$accessToken, uid=$uid)"
    }


}