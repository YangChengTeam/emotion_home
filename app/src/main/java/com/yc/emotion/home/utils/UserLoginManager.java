package com.yc.emotion.home.utils;

import android.app.Activity;
import android.util.Log;

import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.emotion.home.base.YcApplication;
import com.yc.emotion.home.base.listener.ThirdLoginListener;
import com.yc.emotion.home.model.bean.UserAccreditInfo;
import com.yc.emotion.home.base.ui.widget.LoadDialog;

import java.util.Map;

/**
 * Created by wanglin  on 2018/11/19 13:39.
 */
public class UserLoginManager {


    private static UserLoginManager instance;
    private Activity mActivity;
    private ThirdLoginListener mLoginListener;

    private LoadDialog loadingView;


    public static UserLoginManager get() {
        synchronized (UserLoginManager.class) {
            if (instance == null) {
                synchronized (UserLoginManager.class) {
                    instance = new UserLoginManager();
                }
            }
        }
        return instance;
    }


    public UserLoginManager setCallBack(Activity activity, ThirdLoginListener loginListener) {
        this.mActivity = activity;
        this.mLoginListener = loginListener;
        loadingView = new LoadDialog(activity);
        return this;

    }


    /**
     * 授权并登陆
     *
     * @param shareMedia
     */
    public void oauthAndLogin(final SHARE_MEDIA shareMedia) {
        boolean isAuth = UMShareAPI.get(mActivity).isAuthorize(mActivity, shareMedia);
        if (isAuth) {
            UMShareAPI.get(mActivity).getPlatformInfo(mActivity, shareMedia, umAuthListener);
        } else {
            UMShareAPI.get(mActivity).doOauthVerify(mActivity, shareMedia, umAuthListener);
        }

    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showProgressDialog("正在登录中，请稍候...");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int action, Map<String, String> data) {

            try {
                switch (action) {
                    case UMAuthListener.ACTION_AUTHORIZE://授权
                        oauthAndLogin(share_media);
                        break;
                    case UMAuthListener.ACTION_GET_PROFILE://获取信息


                        if (null != data && data.size() > 0) {
                            Log.e("TAG", "onComplete: " + data.toString());
                            UserAccreditInfo userDataInfo = new UserAccreditInfo();
                            userDataInfo.setNickname(data.get("name"));
                            userDataInfo.setCity(data.get("city"));
                            userDataInfo.setIconUrl(data.get("iconurl"));
                            userDataInfo.setGender(data.get("gender"));
                            userDataInfo.setProvince(data.get("province"));
                            userDataInfo.setOpenid(data.get("openid"));
                            userDataInfo.setAccessToken(data.get("accessToken"));
                            userDataInfo.setUid(data.get("uid"));
                            mLoginListener.onLoginResult(userDataInfo);
                            closeProgressDialog();
                        } else {
                            ToastUtil.toast2(mActivity, "登录失败，请重试!");
                            closeProgressDialog();
                        }
                        break;
                }

            } catch (Exception e) {

                LogUtil.msg("complete:-->" + e.getMessage());
                closeProgressDialog();
                ToastUtil.toast2(mActivity, "登录失败，请重试!");
                deleteOauth(share_media);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int action, Throwable throwable) {
            closeProgressDialog();
            LogUtil.msg("login error->>" + throwable.getMessage());
            ToastUtil.toast2(mActivity, "登录失败,请重试！");
            deleteOauth(share_media);

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int action) {
            ToastUtil.toast2(mActivity, "登录取消");
            closeProgressDialog();
        }
    };

    /**
     * 取消授权
     *
     * @param shareMedia
     */
    public void deleteOauth(SHARE_MEDIA shareMedia) {
        if (null == mActivity) {
            return;
        }
        UMShareAPI.get(mActivity).deleteOauth(mActivity, shareMedia, null);
    }

    /**
     * 显示进度框
     *
     * @param message
     */
    private void showProgressDialog(String message) {
        if (mActivity != null && !mActivity.isFinishing()) {
            if (null == loadingView) {
                loadingView = new LoadDialog(mActivity);
            }
            loadingView.setMessage(message);
            loadingView.show();
        }
    }

    /**
     * 关闭进度框
     */
    public void closeProgressDialog() {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                if (null != loadingView && loadingView.isShowing()) {
                    loadingView.dismiss();
                }
                loadingView = null;
            }
        } catch (Exception e) {
            LogUtil.msg("close dialog error->>" + e.getMessage());
        }
    }


}
