package com.yc.emotion.home.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
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
import com.yc.emotion.home.index.adapter.ChatAdapter;
import com.yc.emotion.home.index.domain.bean.ChatItem;
import com.yc.emotion.home.index.domain.bean.Message;
import com.yc.emotion.home.mine.domain.bean.LiveInfo;
import com.yc.emotion.home.mine.presenter.LivePresenter;
import com.yc.emotion.home.mine.view.LiveView;
import com.yc.emotion.home.utils.GenerateTestUserSig;
import com.yc.emotion.home.utils.SoftKeyBoardUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import im.zego.zegoexpress.entity.ZegoCanvas;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VOICE_CHATROOM;

/**
 * Created by suns  on 2020/5/27 15:27.
 */
public class LiveRoomActivity extends BaseActivity implements LiveView {

    private static final String TAG = "LiveRoomActivity";
    //    private ZegoExpressEngine engine;
    private ZegoCanvas zegoCanvas;
    private RelativeLayout rlLoading;
    private TCHeartLayout tcHeartLayout;

    private Handler mHandler = new Handler();
    private Random random = new Random();
    private LinearLayout llRaise;
    private TextView tvChat;
    private LinearLayout inputLayout;
    private int[] viewRect = new int[2];
    private Rect rect = new Rect();
    private EditText et_send;
    //    用户列表
    private ArrayList<String> mUserList = new ArrayList<>();
    //    消息列表
    private ArrayList<ChatItem> records = new ArrayList<>();
    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private EditText et_send_message;
    private FrameLayout send_container;
    private TextView tvSendBg;
    private String roomId = "ChatRoom-1";
    private String userID;
    private String userName;

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
    private TextView tvPraiseBottom;
    private int onlineCount = 368;//在线人数基数
    private LiveInfo liveUser;
    private ImageView ivAvatar;
    private TextView tvMentorName;
    private TRTCCloud mTRTCCloud;
    private LinearLayout llwheat;

    public static void startActivity(Context context, LiveInfo liveUser) {
        Intent intent = new Intent(context, LiveRoomActivity.class);
        intent.putExtra("user", liveUser);
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
        requestPermission();
        initView();

    }

    private void requestPermission() {

        String[] permissionNeeded = new String[]{"android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(permissionNeeded, 101);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置fitsSystemWindows属性后添加
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        initViews();
    }

    private void initView() {
        liveUser = getIntent().getParcelableExtra("user");

        LivePresenter livePresenter = new LivePresenter(this, this);

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
        tvPraiseBottom = findViewById(R.id.tv_praise_bottom);
        ivAvatar = findViewById(R.id.mentor_avatar);

        tvMentorName = findViewById(R.id.mentor_name);
        llwheat = findViewById(R.id.ll_wheat);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        chatAdapter = new ChatAdapter(records);
        recyclerViewChat.setAdapter(chatAdapter);
        rlLoading.setVisibility(View.VISIBLE);
        // 初始化SDK
//        engine = ZegoExpressEngine.createEngine(
//                SettingDataUtil.getAppId(),
//                SettingDataUtil.getAppKey(),
//                SettingDataUtil.getEnv(),
//                SettingDataUtil.getScenario(),
//                getApplication(),
//                null
//        );

        // 创建 trtcCloud 实例
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());

        userID = liveUser.getId();
        userName = liveUser.getNickname();
        livePresenter.createRoom(userID);


        initListener();
        tvChat.post(() -> {

            tvChat.getLocationOnScreen(viewRect);
            int tvWidth = tvChat.getWidth();
            int tvHeight = tvChat.getHeight();
//                Log.e(TAG, "run: " + viewRect[0] + "--" + viewRect[1]);
            rect.left = viewRect[0];
            rect.top = viewRect[1];
            rect.right = viewRect[0] + tvWidth;
            rect.bottom = viewRect[1] + tvHeight;
            Log.e(TAG, "run: " + rect.left + "--" + rect.top);
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, "dispatchTouchEvent: ");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            Log.e(TAG, "x=" + x + ";y=" + y);
            Log.e(TAG, "onTouchEvent: " + rect.left + "---" + rect.right + "--" + rect.top + "--" + rect.bottom);
            if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                inputLayout.setVisibility(View.VISIBLE);
                SoftKeyBoardUtils.showSoftBoard(et_send);
            } else {
                SoftKeyBoardUtils.hideSoftBoard(et_send);
            }
        }
        return super.dispatchTouchEvent(event);
    }


    private void setPraise() {
        tvPraiseCount.setText(String.format(getString(R.string.praise_count), praiseCount));
        tvPraiseBottom.setText(String.valueOf(praiseCount));
    }

    private void initListener() {

        llRaise.setOnClickListener(v -> {
            tcHeartLayout.addFavor();
            sendMsg(Message.command_praise, "点赞");
            praiseCount++;
            setPraise();
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
                Toast.makeText(LiveRoomActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMsg(Message.command_normal, content);
        });
        llwheat.setOnClickListener(v -> {
            // 开始推流
            if (mTRTCCloud != null)
                mTRTCCloud.startLocalAudio();
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
                    tvSendBg.setBackgroundColor(ContextCompat.getColor(LiveRoomActivity.this, R.color.color_f8f8f8));
                    tvSendBg.setTextColor(ContextCompat.getColor(LiveRoomActivity.this, R.color.color_bdbcbc));
                } else {
                    tvSendBg.setBackgroundColor(ContextCompat.getColor(LiveRoomActivity.this, R.color.main_theme_color));

                    tvSendBg.setTextColor(ContextCompat.getColor(LiveRoomActivity.this, R.color.white));
                }
            }
        });
    }

    private void startLive() {
//        engine.startPreview(zegoCanvas);

        mTRTCCloud.setListener(new TRTCCloudListener() {
            @Override
            public void onError(int errCode, String errMsg, Bundle bundle) {
                super.onError(errCode, errMsg, bundle);
                Log.e(TAG, "onError: " + errCode + "--" + errMsg);
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    exitRoom();
                }
            }

            @Override
            public void onEnterRoom(long result) {
                super.onEnterRoom(result);
                Log.e(TAG, "onEnterRoom: ");
                rlLoading.setVisibility(View.GONE);
                setLiveUserInfo();
                if (result > 0) {
                    Toast.makeText(LiveRoomActivity.this, "进房成功，总计耗时[(" + result + ")]ms", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveRoomActivity.this, "进房失败，错误码[(" + result + ")]", Toast.LENGTH_SHORT).show();
                }

                tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
            }

            @Override
            public void onExitRoom(int i) {
                super.onExitRoom(i);
                Log.e(TAG, "onExitRoom: ");
            }

            @Override
            public void onRemoteUserEnterRoom(String s) {
                super.onRemoteUserEnterRoom(s);
                Log.e(TAG, "onRemoteUserEnterRoom: ");
                List<ChatItem> chatItems = new ArrayList<>();

                ChatItem chatItem = new ChatItem(s, ChatItem.TYPE_COME_CHAT);

                chatItems.add(chatItem);

                chatAdapter.addData(chatItems);

//                sendMsg(Message.command_get_wx, s);
                sendMsg(Message.command_come_room, s);
            }

            @Override
            public void onRemoteUserLeaveRoom(String s, int i) {
                super.onRemoteUserLeaveRoom(s, i);
                Log.e(TAG, "onRemoteUserLeaveRoom: ");


            }


            @Override
            public void onSendFirstLocalAudioFrame() {
                super.onSendFirstLocalAudioFrame();
                Log.e(TAG, "onSendFirstLocalAudioFrame: ");
                setLiveState(true);
                setPraise();
                llwheat.setVisibility(View.GONE);
            }

            @Override
            public void onRecvCustomCmdMsg(String userId, int cmdId, int seq, byte[] message) {
                Log.e(TAG, "onRecvCustomCmdMsg: ");
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
                        //进入房间
                        onlineCount++;
                        tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
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
//        engine.setEventHandler(new IZegoEventHandler() {
//            @Override
//            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
//                super.onRoomUserUpdate(roomID, updateType, userList);
//                Log.e(TAG, "onRoomUserUpdate: roomID = " + roomID + ", state = " + updateType + ",  userList= " + userList);
//
//
//            }
//
//            @Override
//            public void onRoomOnlineUserCountUpdate(String roomID, int count) {
//                super.onRoomOnlineUserCountUpdate(roomID, count);
//                if (count > onlineCount) {
//                    onlineCount = count;
//                }
//                tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
//            }
//
//            /**
//             * 接收房间广播消息通知
//             *
//             * @param roomID 房间 ID
//             * @param messageList 收到的消息列表
//             */
//            @Override
//            public void onIMRecvBroadcastMessage(String roomID, ArrayList<ZegoBroadcastMessageInfo> messageList) {
//                Log.i(TAG, "onIMRecvBroadcastMessage: roomID = " + roomID + ",  messageList= " + messageList);
//                List<ChatItem> chatItems = new ArrayList<>();
//                for (int i = 0; i < messageList.size(); i++) {
//                    ZegoBroadcastMessageInfo info = messageList.get(i);
//
//                    String content = info.message;
//
//                    Message message = JSON.parseObject(content, Message.class);
//                    Log.e(TAG, "onIMRecvBroadcastMessage: " + message.getType());
//                    ChatItem chatItem = null;
//                    switch (message.getType()) {
//                        case TUTOR:
//                            chatItem = new ChatItem(info.fromUser.userName, message.getContent(), ChatItem.TYPE_NOTIFICATION);
//                            break;
//                        case NORMAL:
//                            chatItem = new ChatItem(info.fromUser.userName, message.getContent(), ChatItem.TYPE_OTHER);
//                            break;
//                        case PRAISE:
//                            //点赞消息
//                            tcHeartLayout.addFavor();
//                            praiseCount++;
//                            setPraise();
//                            break;
//                    }
//                    if (chatItem != null)
//                        chatItems.add(chatItem);
//                }
//                chatAdapter.addData(chatItems);
//            }
//
//
//            //state 房间状态 1.CONNECTING 2 CONNECTED 已连接 0 DISCONNECTED 断开链接
//            @Override
//            public void onRoomStateUpdate(String roomID, ZegoRoomState state, int errorCode, JSONObject extendedData) {
//                super.onRoomStateUpdate(roomID, state, errorCode, extendedData);
//                int value = state.value();
//                if (value == 2) {
//                    rlLoading.setVisibility(View.GONE);
//                    setLiveUserInfo();
//                }
//                Log.e(TAG, "onRoomStateUpdate---" + state.name() + " : " + value);
//            }
//
//
//            @Override
//            public void onPublisherStateUpdate(String streamID, ZegoPublisherState state, int errorCode, JSONObject extendedData) {
//                super.onPublisherStateUpdate(streamID, state, errorCode, extendedData);
//                // 推流状态更新，errorCode 非0 则说明推流失败
//                // 推流常见错误码请看文档: <a>https://doc.zego.im/CN/308.html</a>
//                if (errorCode == 0) {
//
//                    Log.i(TAG, String.format("publish stream success, streamID : %s", streamID));
//                    int value = state.value();
//                    Log.e(TAG, "onPlayerStateUpdate: " + value + "  code: " + errorCode);
//                    if (value == 2) {//拉流成功
//                        setLiveState(true);
//                        setPraise();
//                    }
//
//                    Toast.makeText(LiveRoomActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Log.i(TAG, String.format("publish stream fail, streamID : %s, errorCode : %d", streamID, errorCode));
//                    Toast.makeText(LiveRoomActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });


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


    private void setLiveUserInfo() {
        if (liveUser != null) {
            Glide.with(this).load(liveUser.getFace()).circleCrop().error(R.drawable.default_avatar_72).into(ivAvatar);
            tvMentorName.setText(liveUser.getNickname());
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

    /**
     * 发送广播消息
     *
     * @param msg
     */
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
                Log.i(TAG, "send  message success");

                if (cmdId == Message.command_tutor) {
                    records.add(new ChatItem(userName, msg, ChatItem.TYPE_NOTIFICATION));
                } else if (cmdId == Message.command_normal) {
                    records.add(new ChatItem(userName, msg, ChatItem.TYPE_ME));
                }

                chatAdapter.setNewData(records);
            } else {
                Log.i(TAG, "send barrage message fail");
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "所需权限没有获取到", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private String createMessage(int cmdId, String content) {
        Message message = new Message(content, cmdId);
        return JSON.toJSONString(message);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        exitRoom();
    }

    @Override
    public void showCreateRoomSuccess(@NotNull LiveInfo data) {
        roomId = data.getRoomId();
        String streamID = data.getStreamId();

        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.userId = userID;
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);

        trtcParams.roomId = 123456;
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor;


        //进入房间
        mTRTCCloud.enterRoom(trtcParams, TRTC_APP_SCENE_VOICE_CHATROOM);


        // 调用sdk 开始预览接口 设置view 启用预览
        // 调用sdk 开始预览接口 设置view 启用预览
//        zegoCanvas = new ZegoCanvas(preview);
        startLive();
    }

    @Override
    public void showLoginSuccess(@NotNull LiveInfo data) {

    }
}
