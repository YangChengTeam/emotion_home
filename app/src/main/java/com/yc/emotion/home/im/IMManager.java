package com.yc.emotion.home.im;

import android.content.Context;
import android.service.voice.VoiceInteractionService;
import android.util.Log;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupListener;
import com.tencent.imsdk.v2.V2TIMGroupManager;
import com.tencent.imsdk.v2.V2TIMGroupManagerImpl;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMValueCallback;

/**
 * Created by suns  on 2020/6/8 12:54.
 */
public class IMManager {
    private static IMManager instance;
    private V2TIMSDKConfig config;

    private static final String TAG = "IMManager";
    private final V2TIMManager v2TIMManager;

    private IMManager() {
        // 1. 从 IM 控制台获取应用 SDKAppID，详情请参考 SDKAppID。
        // 2. 初始化 config 对象
        config = new V2TIMSDKConfig();
        // 3. 指定 log 输出级别，详情请参考 SDKConfig。
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
        v2TIMManager = V2TIMManager.getInstance();

    }

    public static IMManager getInstance() {
        synchronized (IMManager.class) {
            if (instance == null) {
                synchronized (IMManager.class) {
                    instance = new IMManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context, int sdkAppID) {
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        // initSDK 后 SDK 会自动连接网络，网络连接状态可以在 V2TIMSDKListener 回调里面监听。
        v2TIMManager.initSDK(context, sdkAppID, config, new V2TIMSDKListener() {
            // 5. 监听 V2TIMSDKListener 回调
            @Override
            public void onConnecting() {
                // 正在连接到腾讯云服务器
                Log.e(TAG, "onConnecting: ");
            }

            @Override
            public void onConnectSuccess() {
                // 已经成功连接到腾讯云服务器
                Log.e(TAG, "onConnectSuccess: ");
            }

            @Override
            public void onConnectFailed(int code, String error) {
                // 连接腾讯云服务器失败
                Log.e(TAG, "onConnectFailed: ");
            }
        });
    }


    /**
     * 登录房间
     *
     * @param userId
     * @param userSig
     * @param callback
     */
    public void login(String userId, String userSig, V2TIMCallback callback) {
        v2TIMManager.login(userId, userSig, callback);
    }

    /**
     * 登出房间
     *
     * @param callback
     */
    public void logout(V2TIMCallback callback) {
        v2TIMManager.logout(callback);
    }

    /**
     * 主播创建房间
     *
     * @param groupId
     * @param groupName
     * @param callback
     */
    public void createGroupRoom(String groupId, String groupName, V2TIMValueCallback<String> callback) {
        v2TIMManager.createGroup(V2TIMManager.GROUP_TYPE_AVCHATROOM, groupId, groupName, callback);
    }

    /**
     * 群主解算群
     */
    public void dismissGroup(String groupID,
                             final V2TIMCallback callback) {
        v2TIMManager.dismissGroup(groupID, callback);
    }

    /**
     * 用户加入房间
     *
     * @param groupID
     * @param callback
     */
    public void joinGroupRoom(String groupID, V2TIMCallback callback) {
        v2TIMManager.joinGroup(groupID, "", callback);
    }


    /**
     * @param text
     * @param groupID
     * @param priority
     * @param callback
     */
    public void sendTextMessage(String text,
                                String groupID,
                                int priority,
                                V2TIMValueCallback<V2TIMMessage> callback) {
        v2TIMManager.sendGroupTextMessage(text, groupID, priority, callback);
    }


    /**
     * 发送自定义消息
     *
     * @param message
     * @param groupID
     * @param priority
     * @param callback
     */
    public void sendCustomMessage(String message,
                                  String groupID,
                                  int priority,
                                  V2TIMValueCallback<V2TIMMessage> callback) {
        v2TIMManager.sendGroupCustomMessage(message.getBytes(), groupID, priority, callback);

    }

    /**
     * 禁言或解禁群组
     */
    public void muteGroupMember(String groupId, boolean isMuted,
                                V2TIMCallback callback) {
        V2TIMGroupInfo groupInfo = new V2TIMGroupInfo();
        groupInfo.setAllMuted(isMuted);

        groupInfo.setGroupID(groupId);

        V2TIMManager.getGroupManager().setGroupInfo(groupInfo, callback);
    }

    /**
     * 设置接受消息回调
     *
     * @param listener
     */
    public void setReceiveMagListener(V2TIMSimpleMsgListener listener) {
        v2TIMManager.addSimpleMsgListener(listener);
    }


    /**
     * 群组消息回调
     *
     * @param listener
     */
    public void setGroupListener(V2TIMGroupListener listener) {
        v2TIMManager.setGroupListener(listener);
    }

    /**
     * 获取群列表
     *
     * @param groupID
     * @param callback
     */
    public void getGroupList(String groupID,
                             V2TIMValueCallback<V2TIMGroupMemberInfoResult> callback) {
        V2TIMManager.getGroupManager().getGroupMemberList(groupID, V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL, 0, callback);
//        V2TIMManager.getGroupManager().getGroupsInfo();
    }

    /**
     * 获取会话列表
     *
     * @param nextSeq
     * @param count
     * @param callback
     */
    public void getConversationList(long nextSeq,
                                    int count,
                                    V2TIMValueCallback<V2TIMConversationResult> callback) {
        V2TIMManager.getConversationManager().getConversationList(nextSeq, count, callback);
    }


    /**
     * 设置会话监听
     *
     * @param listener
     */
    public void setConversationListener(V2TIMConversationListener listener) {
        V2TIMManager.getConversationManager().setConversationListener(listener);
    }

}
