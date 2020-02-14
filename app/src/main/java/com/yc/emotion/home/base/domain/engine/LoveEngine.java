package com.yc.emotion.home.base.domain.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.yc.emotion.home.model.bean.AResultInfo;
import com.yc.emotion.home.model.bean.LoveHealingBean;
import com.yc.emotion.home.model.bean.MenuadvInfoBean;
import com.yc.emotion.home.model.bean.ShareInfo;
import com.yc.emotion.home.model.bean.WetChatInfo;
import com.yc.emotion.home.constant.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by mayn on 2019/5/9.
 */

public class LoveEngine extends BaseEngine {

    public LoveEngine(Context context) {
        super(context);
    }



    public Observable<AResultInfo<List<LoveHealingBean>>> recommendLovewords(String userId, String page, String page_size, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page);
        params.put("page_size", page_size);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealingBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealingBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.debugBaseUrl.concat(url), new TypeReference<AResultInfo<List<LoveHealingBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
//    http://love.bshu.com/v1/lovewords/recommend


    public Observable<AResultInfo<MenuadvInfoBean>> menuadvInfo(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<MenuadvInfoBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<MenuadvInfoBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrlV1(url), new TypeReference<AResultInfo<MenuadvInfoBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }




    public Observable<AResultInfo<String>> collectLovewords(String userId, String lovewordsId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("lovewords_id", lovewordsId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrlV1(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }



    public Observable<ResultInfo<List<ShareInfo>>> getShareInfo(Context context) {
        return HttpCoreEngin.get(context).rxpost(URLConfig.SHARE_INFO_URL, new TypeReference<ResultInfo<List<ShareInfo>>>() {
        }.getType(), null, true, true, true);
    }



    public Observable<ResultInfo<List<MusicInfo>>> randomSpaInfo(String type_id) {
        Map<String, String> params = new HashMap<>();

        params.put("user_id", "");
        params.put("type_id", type_id);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SPA_RANDOM_URL, new TypeReference<ResultInfo<List<MusicInfo>>>() {
        }.getType(), params, true, true, true);

    }







    /**
     * 分享得会员
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<String>> shareReward(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SHARE_REWARD_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 获取微信信息
     *
     * @param tutor_id
     * @param article_id
     * @param example_id
     * @return
     */
    public Observable<ResultInfo<WetChatInfo>> getWechatInfo(String tutor_id, String article_id, String example_id) {
        Map<String, String> params = new HashMap<>();

        if (!TextUtils.isEmpty(tutor_id))
            params.put("tutor_id", tutor_id);
        if (!TextUtils.isEmpty(article_id)) {
            params.put("article_id", article_id);
        }
        if (!TextUtils.isEmpty(example_id)) {
            params.put("example_id", example_id);
        }

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.WECHAT_INFO_URL, new TypeReference<ResultInfo<WetChatInfo>>() {
        }.getType(), params, true, true, true);

    }


}
