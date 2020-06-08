package com.yc.emotion.home.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.wapchief.likestarlibrary.like.TCHeartLayout;
import com.yc.emotion.home.R;
import com.yc.emotion.home.base.ui.activity.BaseActivity;
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment;
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment;
import com.yc.emotion.home.constant.Constant;
import com.yc.emotion.home.index.adapter.ChatAdapter;
import com.yc.emotion.home.index.domain.bean.ChatItem;
import com.yc.emotion.home.index.domain.bean.Message;
import com.yc.emotion.home.index.domain.bean.MessageType;
import com.yc.emotion.home.index.ui.fragment.CloseLiveFragment;
import com.yc.emotion.home.index.ui.fragment.WxLoginFragment;
import com.yc.emotion.home.mine.domain.bean.LiveInfo;
import com.yc.emotion.home.model.bean.UserInfo;
import com.yc.emotion.home.model.bean.event.EventLoginState;
import com.yc.emotion.home.utils.GenerateTestUserSig;
import com.yc.emotion.home.utils.PreferenceUtil;
import com.yc.emotion.home.utils.SoftKeyBoardUtils;
import com.yc.emotion.home.utils.UserInfoHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import im.zego.zegoexpress.ZegoExpressEngine;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VOICE_CHATROOM;

/**
 * Created by suns  on 2020/5/29 14:28.
 */
public class LiveLookActivity extends BaseActivity {

    private RelativeLayout rlLoading;
    private TCHeartLayout tcHeartLayout;

    private ZegoExpressEngine engine;
    private static final String TAG = "LiveLookActivity";

    private Handler mHandler = new Handler();
    private Random random = new Random();
    private LinearLayout llRaise;
    private TextView tvChat;
    private LinearLayout inputLayout;
    private int[] viewRect = new int[2];
    private Rect rect = new Rect();
    private EditText et_send;

    //    消息列表
    private ArrayList<ChatItem> records = new ArrayList<>();
    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private EditText et_send_message;
    private FrameLayout send_container;
    private TextView tvSendBg;
    private String userID;
    private LinearLayout llWx;
    private LinearLayout llShare;
    private TextView tvLiveState;
    //    直播动画
    private ImageView ivLiveAnim;
    //    在线人数
    private TextView tvOnLineCount;
    //    点赞人数
    private TextView tvPraiseCount;
    private int praiseCount = 200;//点赞人数
    private ImageView ivClose;
    private TextView tvPraiseBottom;
    private String userName;
    private int uid;
    private int onlineCount = 368;//在线人数基数

    private TRTCCloud mTRTCCloud;
    private LiveInfo liveInfo;
    private TextView tvMentorName;
    private ImageView ivMentorAvatar;

    public static void startActivity(Context context, LiveInfo liveInfo) {
        Intent intent = new Intent(context, LiveLookActivity.class);
        intent.putExtra("liveinfo", liveInfo);
        context.startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.live_room_layout;
    }

    @Override
    public void initViews() {
        invadeStatusBar(); //侵入状态栏

        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
        initView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置fitsSystemWindows属性后添加
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.live_room_layout);
        initViews();
    }

    private void initView() {
        liveInfo = getIntent().getParcelableExtra("liveinfo");

        rlLoading = findViewById(R.id.loading_connect_listen);
        tcHeartLayout = findViewById(R.id.heart_layout);

        llRaise = findViewById(R.id.ll_raise);
        tvChat = findViewById(R.id.tv_chat);
        inputLayout = findViewById(R.id.user_input_layout);
        et_send = findViewById(R.id.live_send_message);
        send_container = findViewById(R.id.send_txt_msg_submit_container);
        tvSendBg = findViewById(R.id.send_txt_msg_submit);

        recyclerViewChat = findViewById(R.id.recyclerView_chat);
        llWx = findViewById(R.id.ll_wx);
        llShare = findViewById(R.id.ll_share);
        tvLiveState = findViewById(R.id.live_state);
        ivLiveAnim = findViewById(R.id.iv_live_anim);
        tvOnLineCount = findViewById(R.id.online_count);
        tvPraiseCount = findViewById(R.id.praise_count);
        ivClose = findViewById(R.id.close_live);
        tvPraiseBottom = findViewById(R.id.tv_praise_bottom);
        LinearLayout llWheat = findViewById(R.id.ll_wheat);
        tvMentorName = findViewById(R.id.mentor_name);
        ivMentorAvatar = findViewById(R.id.mentor_avatar);
        llWheat.setVisibility(View.GONE);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        chatAdapter = new ChatAdapter(records);
        recyclerViewChat.setAdapter(chatAdapter);

        uid = UserInfoHelper.Companion.getInstance().getUid();

        // 初始化SDK
//        engine = ZegoExpressEngine.createEngine(
//                SettingDataUtil.getAppId(),
//                SettingDataUtil.getAppKey(),
//                SettingDataUtil.getEnv(),
//                SettingDataUtil.getScenario(),
//                getApplication(),
//                null
//        );
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());

        rlLoading.setVisibility(View.VISIBLE);
//        TextureView preview = findViewById(R.id.preview);

        // 调用sdk 开始预览接口 设置view 启用预览
//        zegoCanvas = new ZegoCanvas(preview);
        starLook();
        initListener();
        tvChat.post(() -> {

            tvChat.getLocationOnScreen(viewRect);
            int tvWidth = tvChat.getWidth();
            int tvHeight = tvChat.getHeight();

            rect.left = viewRect[0];
            rect.top = viewRect[1];
            rect.right = viewRect[0] + tvWidth;
            rect.bottom = viewRect[1] + tvHeight;
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                if (uid == 0) {//未登录
                    WxLoginFragment wxLoginFragment = new WxLoginFragment();
                    wxLoginFragment.show(getSupportFragmentManager(), "");

                } else {
                    inputLayout.setVisibility(View.VISIBLE);
                    SoftKeyBoardUtils.showSoftBoard(et_send);
                }
            } else {
                SoftKeyBoardUtils.hideSoftBoard(et_send);
            }
        }

        return super.dispatchTouchEvent(event);
    }


    private void initListener() {
        llRaise.setOnClickListener(v -> {
            if (uid == 0) {
                WxLoginFragment wxLoginFragment = new WxLoginFragment();
                wxLoginFragment.show(getSupportFragmentManager(), "");
            } else {
                tcHeartLayout.addFavor();
                sendMsg(Message.command_praise, "点赞");
                praiseCount++;
                setPraise();
            }
        });

        SoftKeyBoardUtils.setListener(this, new SoftKeyBoardUtils.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                inputLayout.setVisibility(View.GONE);
            }
        });
        send_container.setOnClickListener(v -> {
            String content = et_send.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(LiveLookActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMsg(Message.command_normal, content);
        });

        et_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();

                if (TextUtils.isEmpty(result)) {
                    tvSendBg.setBackgroundColor(ContextCompat.getColor(LiveLookActivity.this, R.color.color_f8f8f8));
                    tvSendBg.setTextColor(ContextCompat.getColor(LiveLookActivity.this, R.color.color_bdbcbc));
                } else {
                    tvSendBg.setBackgroundColor(ContextCompat.getColor(LiveLookActivity.this, R.color.main_theme_color));

                    tvSendBg.setTextColor(ContextCompat.getColor(LiveLookActivity.this, R.color.white));
                }
            }
        });
        chatAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatItem chatItem = chatAdapter.getItem(position);
            if (null != chatItem) {
                if (chatItem.getItemType() == ChatItem.TYPE_NOTIFICATION) {
                    AddWxFragment addWxFragment = new AddWxFragment();
                    addWxFragment.show(getSupportFragmentManager(), "");
                }
            }
        });
        llWx.setOnClickListener(v -> {
            if (uid == 0) {
                WxLoginFragment wxLoginFragment = new WxLoginFragment();
                wxLoginFragment.show(getSupportFragmentManager(), "");
            } else {
                AddWxFragment addWxFragment = new AddWxFragment();
                addWxFragment.show(getSupportFragmentManager(), "");
            }
        });
        llShare.setOnClickListener(v -> {
            if (uid == 0) {
                WxLoginFragment wxLoginFragment = new WxLoginFragment();
                wxLoginFragment.show(getSupportFragmentManager(), "");
            } else {
                ShareAppFragment shareAppFragment = new ShareAppFragment();
                shareAppFragment.show(getSupportFragmentManager(), "");
            }
        });
        ivClose.setOnClickListener(v -> {
            showCloseFragment();
        });
    }

    private void starLook() {

//        engine.startPreview(zegoCanvas);


        loginRoom();

        mTRTCCloud.setListener(new TRTCCloudListener() {
            @Override
            public void onError(int errCode, String errMsg, Bundle bundle) {
                super.onError(errCode, errMsg, bundle);
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    exitRoom();
                }
            }

            @Override
            public void onEnterRoom(long result) {
                super.onEnterRoom(result);
                Log.e(TAG, "onEnterRoom: ");


                if (result > 0) {
                    rlLoading.setVisibility(View.GONE);

                    sendMsg(Message.command_come_room, userName);

                    Toast.makeText(LiveLookActivity.this, "进房成功，总计耗时[(" + result + ")]ms", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveLookActivity.this, "进房失败，错误码[(" + result + ")]", Toast.LENGTH_SHORT).show();
                }
                tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
                //
            }

            //reason	离开房间原因，0：主动调用 exitRoom 退房；1：被服务器踢出当前房间；2：当前房间整个被解散。
            @Override
            public void onExitRoom(int reason) {
                super.onExitRoom(reason);
                Log.e(TAG, "onExitRoom: ");
            }


            //导师进入房间会回调
            @Override
            public void onRemoteUserEnterRoom(String s) {
                super.onRemoteUserEnterRoom(s);
                Log.e(TAG, "onRemoteUserEnterRoom: " + s);
                List<ChatItem> chatItems = new ArrayList<>();

                ChatItem chatItem = new ChatItem(s, ChatItem.TYPE_COME_CHAT);

                chatItems.add(chatItem);

                chatAdapter.addData(chatItems);

//                sendMsg(Message.command_get_wx, s);


            }

            @Override
            public void onRemoteUserLeaveRoom(String s, int i) {
                super.onRemoteUserLeaveRoom(s, i);
                Log.e(TAG, "onRemoteUserLeaveRoom: ");


            }

            @Override
            public void onFirstAudioFrame(String s) {
                super.onFirstAudioFrame(s);

                Log.e(TAG, "onFirstAudioFrame: " + s);
                setLiveState(true);
                setPraise();
            }

            @Override
            public void onRecvCustomCmdMsg(String userId, int cmdId, int seq, byte[] message) {
//                Log.e(TAG, "onRecvCustomCmdMsg: " + cmdId);
                // 接收到 userId 发送的消息
                List<ChatItem> chatItems = new ArrayList<>();
                ChatItem chatItem = null;
                switch (cmdId) {  // 发送方和接收方协商好的cmdId
                    case Message.command_normal:
                        // 普通消息

                        chatItem = new ChatItem(userId, new String(message, StandardCharsets.UTF_8), ChatItem.TYPE_OTHER);
                        break;
                    case Message.command_tutor:
                        // 处理cmdId = 1消息
                        chatItem = new ChatItem(userId, new String(message, StandardCharsets.UTF_8), ChatItem.TYPE_NOTIFICATION);
                        break;
                    case Message.command_get_wx:
                        // 处理cmdId = 2消息

                        break;
                    case Message.command_praise:
                        //点赞
                        //点赞消息
                        tcHeartLayout.addFavor();
                        praiseCount++;
                        setPraise();
                        break;
                    case Message.command_come_room:
                        onlineCount++;
                        tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));


                        chatItem = new ChatItem(userName, ChatItem.TYPE_COME_CHAT);

                        chatItems.add(chatItem);

                        break;

                    default:
                        break;

                }
                if (chatItem != null)
                    chatItems.add(chatItem);

                chatAdapter.addData(chatItems);
            }
        });


        /** 举例：监听房间内用户进出房间的通知 */
//        boolean isExitLive = PreferenceUtil.getInstance().getBooleanValue(Constant.is_exit_live, true);
//        if (isExitLive) {
//            engine.setEventHandler(new IZegoEventHandler() {
//                @Override
//                public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
//                    super.onRoomUserUpdate(roomID, updateType, userList);
//                    Log.e(TAG, roomID);
//
//
//                    List<ChatItem> chatItems = new ArrayList<>();
//                    for (int i = 0; i < userList.size(); i++) {
//                        ZegoUser user = userList.get(i);
//                        ChatItem chatItem = new ChatItem(user.userName, ChatItem.TYPE_COME_CHAT);
////                    Log.e(TAG, "onRoomUserUpdate: " + user.userName);
////                    chatItem.setUsername(user.userName);
////                    chatItem.setType(ChatItem.TYPE_COME_CHAT);
//                        chatItems.add(chatItem);
//                    }
//
//                    chatAdapter.addData(chatItems);
//                }
//
//                @Override
//                public void onRoomOnlineUserCountUpdate(String roomID, int count) {
//                    super.onRoomOnlineUserCountUpdate(roomID, count);
//                    Log.e(TAG, "onRoomOnlineUserCountUpdate: " + count);
//                    if (count > onlineCount) {
//                        onlineCount = count;
//                    }
//                    tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
//
//                }
//
//                //state 房间状态 1.CONNECTING 2 CONNECTED 已连接 0 DISCONNECTED 断开链接
//                @Override
//                public void onRoomStateUpdate(String roomID, ZegoRoomState state, int errorCode, JSONObject extendedData) {
//                    super.onRoomStateUpdate(roomID, state, errorCode, extendedData);
//                    int value = state.value();
//                    if (value == 2) {
//                        rlLoading.setVisibility(View.GONE);
//
//                        List<ChatItem> chatItems = new ArrayList<>();
//                        ChatItem chatItem = new ChatItem(userName, ChatItem.TYPE_COME_CHAT);
//
//                        chatItems.add(chatItem);
//                        chatAdapter.addData(chatItems);
//                    }
//                    Log.e(TAG, "onRoomStateUpdate: " + state.name() + " : " + value);
//
//                }
//
//
//                @Override
//                public void onPlayerStateUpdate(String streamID, ZegoPlayerState state, int errorCode, JSONObject extendedData) {
//                    super.onPlayerStateUpdate(streamID, state, errorCode, extendedData);
//                    int value = state.value();
//                    Log.e(TAG, "onPlayerStateUpdate: " + value + "  code: " + errorCode);
//                    if (value == 2) {//拉流成功
//                        setLiveState(true);
//                        setPraise();
//                    }
//                }
//
//                /**
//                 * 接收房间广播消息通知
//                 *
//                 * @param roomID 房间 ID
//                 * @param messageList 收到的消息列表
//                 */
//                @Override
//                public void onIMRecvBroadcastMessage(String roomID, ArrayList<ZegoBroadcastMessageInfo> messageList) {
//                    Log.i(TAG, "onIMRecvBroadcastMessage: roomID = " + roomID + ",  messageList= " + messageList);
//                    List<ChatItem> chatItems = new ArrayList<>();
//                    for (int i = 0; i < messageList.size(); i++) {
//                        ZegoBroadcastMessageInfo info = messageList.get(i);
//                        String content = info.message;
//
//                        Message message = JSON.parseObject(content, Message.class);
//                        ChatItem chatItem = null;
//                        switch (message.getType()) {
//                            case TUTOR:
//                                chatItem = new ChatItem(info.fromUser.userName, message.getContent(), ChatItem.TYPE_NOTIFICATION);
//                                break;
//                            case NORMAL:
//                                chatItem = new ChatItem(info.fromUser.userName, message.getContent(), ChatItem.TYPE_OTHER);
//                                break;
//                            case PRAISE:
//                                //点赞消息
//                                tcHeartLayout.addFavor();
//                                praiseCount++;
//                                setPraise();
//                                break;
//                        }
//                        if (chatItem != null)
//                            chatItems.add(chatItem);
//                    }
//                    chatAdapter.addData(chatItems);
//                }
//
//                /**
//                 * 接收自定义信令通知
//                 *
//                 * @param roomID 房间 ID
//                 * @param fromUser 信令的发送人
//                 * @param command 信令内容
//                 */
//                public void onIMRecvCustomCommand(String roomID, ZegoUser fromUser, String command) {
//                    Log.i(TAG, "onIMRecvCustomCommand: roomID = " + roomID + "fromUser :" + fromUser + ", command= " + command);
//
//                    /** 在ListView中显示消息 */
//                    /** Show message in the Listview */
//                    ChatItem chatItem = new ChatItem(fromUser.userName, command);
//                    records.add(chatItem);
//
//                    chatAdapter.addData(records);
//                }
//
//                /**
//                 * 接收房间弹幕消息通知
//                 *
//                 * @param roomID 房间 ID
//                 * @param messageList 收到的消息列表
//                 */
//                @Override
//                public void onIMRecvBarrageMessage(String roomID, ArrayList<ZegoBarrageMessageInfo> messageList) {
//                    Log.i(TAG, "onIMRecvBarrageMessage: roomID = " + roomID + ",  messageList= " + messageList);
//                    for (int i = 0; i < messageList.size(); i++) {
//                        ZegoBarrageMessageInfo info = messageList.get(i);
//
//                        String content = info.message;
//
//                        Message message = JSON.parseObject(content, Message.class);
//                        Log.e(TAG, "onIMRecvBroadcastMessage: " + message.getType());
//
//                        if (message.getType() == MessageType.PRAISE) {
//                            tcHeartLayout.addFavor();
//                        }
//                    }
//                }
//
//
//            });
//            streamID = "201985627";
//            // 开始拉流
//            engine.startPlayingStream(streamID);
//            Log.e(TAG, "starLook: ");
//        }

    }


    private void exitRoom() {
        mTRTCCloud.stopLocalAudio();
        mTRTCCloud.exitRoom();
        //销毁 trtc 实例
        if (mTRTCCloud != null) {
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }


    private void loginRoom() {
        String randomSuffix = (new Date().getTime() % (new Date().getTime() / 1000)) + "";
        if (uid == 0) {
            userID = "userid-" + randomSuffix;
            userName = "username-" + randomSuffix;
        } else {
            userID = uid + "";
            UserInfo userInfo = UserInfoHelper.Companion.getInstance().getUserInfo();
            Log.e(TAG, "loginRoom: " + userInfo);
            if (null != userInfo && !TextUtils.isEmpty(userInfo.getNick_name())) {
                userName = userInfo.getNick_name();
            } else {
                userName = "username-" + randomSuffix;
            }
        }


        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.userId = userID;
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);

        trtcParams.roomId = 123456;
        trtcParams.role = TRTCCloudDef.TRTCRoleAudience;

        //进入房间
        mTRTCCloud.enterRoom(trtcParams, TRTC_APP_SCENE_VOICE_CHATROOM);
        // 开始推流
//        mTRTCCloud.startLocalAudio();

//        ZegoUser user = new ZegoUser(userID, userName);
//        roomId = "ChatRoom-1";
//
//        ZegoRoomConfig config = new ZegoRoomConfig();
//        /** 使能用户登录/登出房间通知 */
//        /** Enable notification when user login or logout */
//        config.isUserStatusNotify = true;
//
//
//        engine.loginRoom(roomId, user, config);
    }

    private void setPraise() {
        tvPraiseCount.setText(String.format(getString(R.string.praise_count), praiseCount));
        tvPraiseBottom.setText(String.valueOf(praiseCount));
        if (liveInfo != null) {
            tvMentorName.setText(liveInfo.getNickname());
            Glide.with(this).load(liveInfo.getLive_cover()).circleCrop().into(ivMentorAvatar);
        }
    }

    private void setLiveState(boolean isLive) {
        tvLiveState.setVisibility(isLive ? View.GONE : View.VISIBLE);
        ivLiveAnim.setVisibility(isLive ? View.VISIBLE : View.GONE);
        tvOnLineCount.setVisibility(isLive ? View.VISIBLE : View.GONE);
        tvPraiseCount.setVisibility(isLive ? View.VISIBLE : View.GONE);
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivLiveAnim.getBackground();
        if (isLive) {
            animationDrawable.start();
        } else {
            animationDrawable.stop();
        }


    }


    private String createMessage(MessageType type, String content) {
        Message message = new Message(type, content);
        return JSON.toJSONString(message);
    }

    public void sendMsg(final int cmdId, final String msg) {

        if (!msg.equals("")) {
            /**
             * cmdID	消息 ID，取值范围为1 - 10
             * data	待发送的消息，最大支持1KB（1000字节）的数据大小
             * reliable	是否可靠发送，可靠发送的代价是会引入一定的延时，因为接收端要暂存一段时间的数据来等待重传
             * ordered	是否要求有序，即是否要求接收端接收的数据顺序和发送端发送的顺序一致，这会带来一定的接收延时，因为在接收端需要暂存并排序这些消息。
             */
            boolean isSendSuccess = mTRTCCloud.sendCustomCmdMsg(cmdId, msg.getBytes(), false, false);
            if (isSendSuccess) {
                Log.i(TAG, "send message success");

                if (cmdId == Message.command_normal) {
                    records.add(new ChatItem(userName, msg, ChatItem.TYPE_ME));
                }
                chatAdapter.setNewData(records);
            } else {
                Log.i(TAG, "send  message fail:" + msg);
            }


//            engine.sendBroadcastMessage(roomId, createMessage(type, msg), new IZegoIMSendBroadcastMessageCallback() {
//                /** 发送广播消息结果回调处理 */
//                /** Send broadcast message result callback processing */
//
//                @Override
//                public void onIMSendBroadcastMessageResult(int errorCode, long messageID) {
//                    if (errorCode == 0) {
//                        Log.i(TAG, "send broadcast message success");
//
//                        Toast.makeText(LiveLookActivity.this, getString(R.string.tx_im_send_bc_ok), Toast.LENGTH_SHORT).show();
//                        if (type == MessageType.NORMAL) {
//                            records.add(new ChatItem(userName + "(我)", msg, ChatItem.TYPE_ME));
//                            chatAdapter.setNewData(records);
//                        }
//
//                    } else {
//                        Log.i(TAG, "send broadcast message fail ");
//                        Toast.makeText(LiveLookActivity.this, getString(R.string.tx_im_send_bc_fail) + errorCode, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

    private void showCloseFragment() {
        CloseLiveFragment closeLiveFragment = new CloseLiveFragment();
        closeLiveFragment.show(getSupportFragmentManager(), "");
        closeLiveFragment.setOnCloseListener(new CloseLiveFragment.OnCloseListener() {
            @Override
            public void onMinimize() {
                PreferenceUtil.getInstance().setBooleanValue(Constant.is_exit_live, false);
//                moveTaskToBack(true);
//                engine.setEventHandler(null);
                finish();

            }

            @Override
            public void onClose() {
//                PreferenceUtil.getInstance().setBooleanValue(Constant.is_exit_live, true);
//                if (streamID != null)
//                    engine.stopPlayingStream(streamID);
//                engine.logoutRoom(roomId);
//                engine.setEventHandler(null);
                exitRoom();
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoginState event) {
        if (event.state == EventLoginState.STATE_LOGINED) {
            uid = UserInfoHelper.Companion.getInstance().getUid();
            loginRoom();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onBackPressed() {

        showCloseFragment();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }

}
