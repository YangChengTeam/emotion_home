package com.yc.emotion.home.base.domain.engine

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin

import com.yc.emotion.home.constant.URLConfig
import com.yc.emotion.home.model.bean.*
import rx.Observable
import java.util.*

/**
 * Created by mayn on 2019/5/9.
 */
class LoveEngine(context: Context?) : BaseEngine(context) {
    fun recommendLovewords(userId: String?, page: String?, page_size: String?, url: String): Observable<AResultInfo<List<LoveHealingBean>>> {
        val params: MutableMap<String?, String?> = HashMap()
        params["user_id"] = userId
        params["page"] = page
        params["page_size"] = page_size
        requestParams(params)
        val httpCoreEngin = HttpCoreEngin.get(mContext)
        return httpCoreEngin.rxpost(URLConfig.debugBaseUrl + url, object : TypeReference<AResultInfo<List<LoveHealingBean?>?>?>() {}.type,
                params,
                true,
                true, true) as Observable<AResultInfo<List<LoveHealingBean>>>
    }

    //    http://love.bshu.com/v1/lovewords/recommend
    fun menuadvInfo(url: String?): Observable<AResultInfo<MenuadvInfoBean>> {
        val params: Map<String?, String?> = HashMap()
        //        params.put("password", password);
        requestParams(params)
        val httpCoreEngin = HttpCoreEngin.get(mContext)
        return httpCoreEngin.rxpost(URLConfig.getUrlV1(url), object : TypeReference<AResultInfo<MenuadvInfoBean?>?>() {}.type,
                params,
                true,
                true, true) as Observable<AResultInfo<MenuadvInfoBean>>
    }


    fun getShareInfo(context: Context?): Observable<ResultInfo<List<ShareInfo>>> {
        return HttpCoreEngin.get(context).rxpost(URLConfig.SHARE_INFO_URL,
                object : TypeReference<ResultInfo<List<ShareInfo?>?>?>() {}.type,
                null, true, true, true) as Observable<ResultInfo<List<ShareInfo>>>
    }


    /**
     * 分享得会员
     *
     * @param userId
     * @return
     */
    fun shareReward(userId: String?): Observable<ResultInfo<String>> {
        val params: MutableMap<String?, String?> = HashMap()
        params["user_id"] = userId
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SHARE_REWARD_URL,
                object : TypeReference<ResultInfo<String?>?>() {}.type, params,
                true, true, true) as Observable<ResultInfo<String>>
    }

    /**
     * 获取微信信息
     *
     * @param tutor_id
     * @param article_id
     * @param example_id
     * @return
     */
    fun getWechatInfo(tutor_id: String?, article_id: String?, example_id: String?, position: String?): Observable<ResultInfo<WetChatInfo>> {
        val params: MutableMap<String?, String?> = HashMap()
        if (!TextUtils.isEmpty(tutor_id)) params["tutor_id"] = tutor_id
        if (!TextUtils.isEmpty(article_id)) {
            params["article_id"] = article_id
        }
        if (!TextUtils.isEmpty(example_id)) {
            params["example_id"] = example_id
        }
        if (!TextUtils.isEmpty(position)) params["position"] = position
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.WECHAT_INFO_URL,
                object : TypeReference<ResultInfo<WetChatInfo?>?>() {}.type, params,
                true, true, true) as Observable<ResultInfo<WetChatInfo>>
    }
}