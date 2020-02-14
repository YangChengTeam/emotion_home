package com.yc.emotion.home.message.domain.model

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.MessageInfo
import rx.Observable
import java.util.HashMap

/**
 *
 * Created by suns  on 2019/11/15 14:03.
 */
class MessageModel(override var context: Context?) : IModel {


    /**
     * 获取消息信息
     *
     * @return
     */
    fun getMessageInfoList(): Observable<ResultInfo<List<MessageInfo>>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.MESSAGE_LIST_URL, object : TypeReference<ResultInfo<List<MessageInfo>>>() {

        }.type, null, true, true, true) as Observable<ResultInfo<List<MessageInfo>>>
    }


    /**
     * 通知详情
     *
     * @param id
     * @return
     */
    fun getNotificationDetail(id: String): Observable<ResultInfo<MessageInfo>> {
        val params = HashMap<String, String>()
        params["id"] = id
        return HttpCoreEngin.get(context).rxpost(URLConfig.NOTIFICATION_DETAIL_URL, object : TypeReference<ResultInfo<MessageInfo>>() {

        }.type, params, true, true, true) as Observable<ResultInfo<MessageInfo>>
    }
}