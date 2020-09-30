package com.yc.emotion.home.mine.domain.model

import android.content.Context
import android.text.TextUtils
import android.view.TextureView

import com.yc.emotion.home.base.constant.URLConfig
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.UserInterInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import io.reactivex.Observable
import yc.com.rthttplibrary.bean.ResultInfo

/**
 * Created by suns  on 2019/11/15 15:04.
 */
class MineModel(override var context: Context?) : IModel(context) {

    /**
     * 获取用户资料
     *
     * @param userId
     * @return
     */
    fun userInfo(userId: String): Observable<ResultInfo<UserInfo>> {

        return request.userInfo(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        if (!TextUtils.isEmpty(nickName))
            params["nick_name"] = nickName
        params["face"] = face
        params["password"] = password
        if (!TextUtils.isEmpty(birthday))
            params["birthday"] = birthday
        params["sex"] = sex.toString() + ""
        if (!TextUtils.isEmpty(profession))
            params["profession"] = profession
        if (!TextUtils.isEmpty(age))
            params["age"] = age
        if (!TextUtils.isEmpty(signature))
            params["signature"] = signature
        if (!TextUtils.isEmpty(interested_id))
            params["interested_id"] = interested_id


        return request.updateUserInfo(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 用户感兴趣问题
     *
     * @return
     */
    fun getUserInterseInfo(): Observable<ResultInfo<List<UserInterInfo>>> {

        return request.getUserInterseInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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

    fun addSuggestion(userId: String, content: String?, qq: String?, wechat: String?, url: String): Observable<ResultInfo<String>> {
        val params = HashMap<String, String?>()
        params["user_id"] = userId
        params["content"] = content
        params["qq"] = qq
        if (!TextUtils.isEmpty(wechat)) {
            params["wechat"] = wechat
        }

        return request.addSuggestion(params, URLConfig.getBaseUrl() + url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}