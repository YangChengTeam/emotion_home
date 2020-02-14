package com.yc.emotion.home.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.emotion.home.base.YcApplication;
import com.yc.emotion.home.model.bean.ShareInfo;
import com.yc.emotion.home.base.domain.engine.LoveEngine;
import com.yc.emotion.home.model.util.SPUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by wanglin  on 2019/7/9 18:18.
 */
public class ShareInfoHelper {


    private static final String TAG = "ShareInfoHelper";
    private static ShareInfo mShareInfo;

    public static void setShareInfo(ShareInfo ShareInfo) {
        ShareInfoHelper.mShareInfo = ShareInfo;
        try {

            String str = JSON.toJSONString(ShareInfo);
            SPUtils.put(YcApplication.getInstance().getApplicationContext(), SPUtils.SHARE_INFO, str);

        } catch (Exception e) {
            Log.e(TAG, "setShareInfo: 保存json失败" + e.getMessage());
        }
    }

    public static ShareInfo getShareInfo() {
        if (mShareInfo != null) {
            return mShareInfo;
        }
        try {
            String str = (String) SPUtils.get(YcApplication.getInstance().getApplicationContext(), SPUtils.SHARE_INFO, "");

            mShareInfo = JSON.parseObject(str, ShareInfo.class);
        } catch (Exception e) {
            Log.e(TAG, "getShareInfo: 解析json失败" + e.getMessage());
        }

        return mShareInfo;
    }

    public static void getNetShareInfo(Context context) {
        LoveEngine loveEngin = new LoveEngine(context);

        loveEngin.getShareInfo(context).subscribe(new Subscriber<ResultInfo<List<ShareInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<ShareInfo>> shareInfoResultInfo) {
                if (shareInfoResultInfo != null && shareInfoResultInfo.code == HttpConfig.STATUS_OK && shareInfoResultInfo.data != null) {
                    List<ShareInfo> shareInfos = shareInfoResultInfo.data;
                    if (shareInfos.size() > 0){
                        ShareInfo shareInfo = shareInfos.get(0);
                        setShareInfo(shareInfo);
                    }




                }
            }
        });
    }
}
