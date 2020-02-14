package com.yc.emotion.home.mine.domain.model

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/15 15:04.
 */
class MineModel(override var context: Context?) : IModel {

    /**
     * 获取用户资料
     *
     * @param userId
     * @return
     */
    fun userInfo(userId: String): Observable<AResultInfo<UserInfo>> {
        val params = HashMap<String, String>()
        params["user_id"] = userId

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.USER_INFO_URL, object : TypeReference<AResultInfo<UserInfo>>() {

        }.type, params, true,
                true, true) as Observable<AResultInfo<UserInfo>>
    }


    /**
     * 更新用户资料
     *
     * @param userId
     * @param nickName
     * @param face
     * @param password
     * @param birthday
     * @param sex        1男2女
     * @param profession 职业
     * @param age
     * @param signature
     * @return
     */
    //nick_name=aaa&face=&password=123456&birthday&sex&user_id=2&profession=1&age=1&signature=1
    fun updateUserInfo(userId: String, nickName: String?, face: String?, password: String, birthday: String, sex: Int, profession: String?, age: String?, signature: String?, interested_id: String?): Observable<ResultInfo<UserInfo>> {
        val params = HashMap<String, String?>()
        params["user_id"] = userId
        params["nick_name"] = nickName
        params["face"] = face
        params["password"] = password
        params["birthday"] = birthday
        params["sex"] = sex.toString() + ""
        params["profession"] = profession
        params["age"] = age
        params["signature"] = signature
        params["interested_id"] = interested_id


        return HttpCoreEngin.get(context).rxpost(URLConfig.UPDATE_USER_INFO_URL, object : TypeReference<ResultInfo<UserInfo>>() {

        }.type,
                params, true, true, true) as Observable<ResultInfo<UserInfo>>
    }


    /**
     * 用户感兴趣问题
     *
     * @return
     */
    fun getUserInterseInfo(): Observable<ResultInfo<List<UserInterInfo>>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.INTER_ALL_URL, object : TypeReference<ResultInfo<List<UserInterInfo>>>() {

        }.type, null, true, true, true) as Observable<ResultInfo<List<UserInterInfo>>>
    }


    /**
     * 意见反馈
     *
     * @param userId
     * @param content
     * @param qq
     * @param wechat
     * @param url
     * @return
     */

    fun addSuggestion(userId: String, content: String?, qq: String?, wechat: String?, url: String): Observable<AResultInfo<String>> {
        val params = HashMap<String, String?>()
        params["user_id"] = userId
        params["content"] = content
        params["qq"] = qq
        if (!TextUtils.isEmpty(wechat)) {
            params["wechat"] = wechat
        }

        val httpCoreEngin = HttpCoreEngin.get(context)
        return httpCoreEngin.rxpost(URLConfig.getBaseUrl() + url, object : TypeReference<AResultInfo<String>>() {

        }.type, params,
                true,
                true, true) as Observable<AResultInfo<String>>
    }
}