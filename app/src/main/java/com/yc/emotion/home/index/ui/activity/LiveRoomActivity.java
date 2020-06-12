package com.yc.emotion.home.index.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.wapchief.likestarlibrary.like.TCHeartLayout;
import com.yc.emotion.home.R;
import com.yc.emotion.home.base.ui.activity.BaseActivity;
import com.yc.emotion.home.im.IMManager;
import com.yc.emotion.home.index.adapter.ChatAdapter;
import com.yc.emotion.home.index.domain.bean.ChatItem;
import com.yc.emotion.home.index.domain.bean.Message;
import com.yc.emotion.home.mine.domain.bean.LiveInfo;
import com.yc.emotion.home.mine.presenter.LivePresenter;
import com.yc.emotion.home.mine.view.LiveView;
import com.yc.emotion.home.model.util.ScreenUtils;
import com.yc.emotion.home.utils.GenerateTestUserSig;
import com.yc.emotion.home.utils.SoftKeyBoardUtils;
import com.yc.emotion.home.utils.UIUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VOICE_CHATROOM;

/**
 * Created by suns  on 2020/5/27 15:27.
 */
public class LiveRoomActivity extends BaseActivity implements LiveView, V2TIMCallback {

    private static final String TAG = "LiveRoomActivity";


    private RelativeLayout rlLoading;
    private TCHeartLayout tcHeartLayout;


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
    private int roomId = 0;
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
    private ImageView ivWheat;
    private IMManager imManager;
    private ImageView ivCloseLive;
    private ImageView ivScreen;
    private LivePresenter livePresenter;
    private ImageView ivTopPPt;
    private RelativeLayout rlTopPPt;
    private TextView tvPack;
    private int topHeight;
    private List<String> weixins;//所有的微信
    private long endTime;//直播结束时间
    private long startTime;//直播开始时间
    private String face;

    public static void startActivity(Context context, LiveInfo liveUser) {
        Intent intent = new Intent(context, LiveRoomActivity.class);
        intent.putExtra("user", liveUser);
        context.startActivity(intent);
    }

    private Handler mHandler = new Handler();

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

        livePresenter = new LivePresenter(this, this);

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
        ivWheat = findViewById(R.id.iv_wheat);
        ImageView ivIntro = findViewById(R.id.iv_intro);
        ivCloseLive = findViewById(R.id.close_live);
        ivScreen = findViewById(R.id.iv_screen);
        ivTopPPt = findViewById(R.id.iv_top_ppt);
        rlTopPPt = findViewById(R.id.rl_top_ppt);
        tvPack = findViewById(R.id.tv_pack);
        TextView tvWx = findViewById(R.id.tv_wx);
        RelativeLayout rlTopContainer = findViewById(R.id.top_container);


        ivIntro.setVisibility(View.GONE);
        tvWx.setText("发送微信");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(records);
        recyclerViewChat.setAdapter(chatAdapter);

        rlLoading.setVisibility(View.VISIBLE);

        // 创建 trtcCloud 实例
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        //创建im初始化实例
        imManager = IMManager.getInstance();

        userID = liveUser.getId();
        userName = liveUser.getNickname();
        livePresenter.createRoom(userID);
        face = liveUser.getFace();

        initListener();
        tvChat.post(() -> {

            tvChat.getLocationOnScreen(viewRect);
            int width = ivScreen.getWidth();
            topHeight = rlTopContainer.getHeight();

            int tvWidth = tvChat.getWidth();
            int tvHeight = tvChat.getHeight();
            rect.left = viewRect[0];
            rect.top = viewRect[1];
            rect.right = viewRect[0] + tvWidth - width;
            rect.bottom = viewRect[1] + tvHeight;
//            Log.e(TAG, "run: " + rect.left + "--" + rect.top);
        });

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
//            Log.e(TAG, "x=" + x + ";y=" + y);
//            Log.e(TAG, "onTouchEvent: " + rect.left + "---" + rect.right + "--" + rect.top + "--" + rect.bottom);
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
//            sendMsg(Message.command_praise, "点赞");
            sendCustomMsg(createMessage(Message.command_praise, "点赞"));
            praiseCount++;
            setPraise();
        });
        llWx.setOnClickListener(v -> {
            sendCustomMsg(createMessage(Message.command_tutor, userName + "-" + face));
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

            sendCustomMsg(createMessage(Message.command_normal, userName + "-" + content + "-" + face));
        });
        ivWheat.setOnClickListener(v -> {
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
        ivCloseLive.setOnClickListener(v -> exitRoom());
        ivScreen.setOnClickListener(v -> {
            if (rlTopPPt.getVisibility() == View.GONE) rlTopPPt.setVisibility(View.VISIBLE);
        });

        tvPack.setOnClickListener(v -> setPackState());
    }

    private void setPackState() {
        if (rlTopPPt.getVisibility() == View.VISIBLE) {
            rlTopPPt.setVisibility(View.GONE);
        } else {
            rlTopPPt.setVisibility(View.VISIBLE);
        }

        if (rlTopPPt.getVisibility() == View.GONE) {
            Drawable drawable = getResources().getDrawable(R.mipmap.arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvPack.setCompoundDrawables(null, null, drawable, null);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvPack.getLayoutParams();

            layoutParams.topMargin = topHeight + UIUtil.dip2px(this, 15);
            tvPack.setLayoutParams(layoutParams);
            tvPack.setText("展开");
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvPack.setCompoundDrawables(null, null, drawable, null);
//            layoutParams.topMargin = 0;
            tvPack.setText("收起");
        }

    }

    private void startLive() {

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
                Log.e(TAG, "onEnterRoom: " + result);
                rlLoading.setVisibility(View.GONE);
                setLiveUserInfo();
//                if (result > 0) {
//                    Toast.makeText(LiveRoomActivity.this, "进房成功，总计耗时[(" + result + ")]ms", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LiveRoomActivity.this, "进房失败，错误码[(" + result + ")]", Toast.LENGTH_SHORT).show();
//                }

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

            }


            @Override
            public void onSendFirstLocalAudioFrame() {
                super.onSendFirstLocalAudioFrame();
                Log.e(TAG, "onSendFirstLocalAudioFrame: ");
                setLiveState(true);
                setPraise();
                ivWheat.setVisibility(View.GONE);
                mHandler.postDelayed(runnable, 0);

            }
        });

    }

    private void exitRoom() {

        livePresenter.liveEnd(roomId + "");
        livePresenter.dismissGroup(roomId + "");
        mHandler.removeCallbacks(runnable);
        //销毁 trtc 实例
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.setListener(null);
            mTRTCCloud.exitRoom();
        }

        if (null != imManager) {
            imManager.dismissGroup(roomId + "", null);
            imManager.logout(null);
        }

        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
        finish();
    }


    private void setLiveUserInfo() {
        if (liveUser != null && !isDestroyed()) {
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

    public String getWx() {
        String wx = "";
        if (null != weixins) {
            wx = weixins.get(random.nextInt(weixins.size()));
        }
        return wx;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitRoom();
    }

    @Override
    public void showCreateRoomSuccess(@NotNull LiveInfo data) {
        roomId = data.getRoomId();
        weixins = data.getWeixin();
        startTime = data.getStart_time();
        endTime = data.getEnd_time();

//        Log.e(TAG, "showCreateRoomSuccess: " + getWx());
        if (!isDestroyed())
            Glide.with(this).load(data.getPpt_img()).into(ivTopPPt);

        if (data.getSdkappid() != 0)
            GenerateTestUserSig.SDKAPPID = data.getSdkappid();
        if (!TextUtils.isEmpty(data.getUser_id())) {
            userID = data.getUser_id();
        }

        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.userId = userID;
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userSig = data.getUsersig();

        trtcParams.roomId = roomId;
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor;

        //进入房间
        mTRTCCloud.enterRoom(trtcParams, TRTC_APP_SCENE_VOICE_CHATROOM);

        if (null != imManager)
            imManager.login(userID, data.getUsersig(), this);

        startLive();
    }

    @Override
    public void showLoginSuccess(@NotNull LiveInfo data) {

    }

    @Override
    public void onError(int code, String msg) {
        Log.e(TAG, "login room error:  code: " + code + "  msg: " + msg);
    }


    @Override
    public void onSuccess() {
        createRoom();
    }

    private void createRoom() {
        if (null != imManager) {
            Log.e(TAG, "createRoom: " + roomId);
            imManager.createGroupRoom(roomId + "", userName + "主播群", new V2TIMSendCallback<String>() {
                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int code, String msg) {
                    Log.e(TAG, "create room failure: code： " + code + "  msg: " + msg);
//                    createRoom();
                }

                @Override
                public void onSuccess(String msg) {
                    imManager.setReceiveMagListener(simpleMsgListener);
                }
            });
        }
    }


    private V2TIMSimpleMsgListener simpleMsgListener = new V2TIMSimpleMsgListener() {
        @Override
        public void onRecvGroupTextMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, String text) {
            super.onRecvGroupTextMessage(msgID, groupID, sender, text);
            List<ChatItem> chatItems = new ArrayList<>();
            ChatItem chatItem = new ChatItem(sender.getNickName(), text, ChatItem.TYPE_OTHER);
            chatItems.add(chatItem);
            chatAdapter.addData(chatItems);
        }

        @Override
        public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
            super.onRecvGroupCustomMessage(msgID, groupID, sender, customData);

            String customMsg = new String(customData, StandardCharsets.UTF_8);
            Log.e(TAG, "onRecvGroupCustomMessage: " + sender.getNickName() + "  msg  " + customMsg);
            Message message = JSON.parseObject(customMsg, Message.class);
            List<ChatItem> chatItems = new ArrayList<>();
            ChatItem chatItem = null;
            switch (message.getCmdId()) {
                case Message.command_normal:
                    //普通消息
                    String[] sts = message.getContent().split("-");
                    chatItem = new ChatItem(sts[0], sts[1], ChatItem.TYPE_OTHER);
                    chatItem.setFace(sts[2]);
                    break;
                case Message.command_tutor://导师发微信
                    chatItem = new ChatItem(sender.getNickName(), message.getContent(), ChatItem.TYPE_NOTIFICATION);
                    break;
                case Message.command_praise://点赞
                    //点赞
                    //点赞消息
                    tcHeartLayout.addFavor();
                    praiseCount++;
                    setPraise();
                    break;
                case Message.command_come_room://进入聊天室
                    //进入房间
                    onlineCount++;
                    tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
                    chatItem = new ChatItem(message.getContent(), ChatItem.TYPE_COME_CHAT);
                    break;
                case Message.command_get_wx://用户获取微信
                    chatItem = new ChatItem(message.getContent(), ChatItem.TYPE_GET_WX);
                    break;
                case Message.command_leave_room://有用户离开房间
                    onlineCount--;
                    tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
                    break;

            }
            if (null != chatItem) {
                chatItems.add(chatItem);
            }

            chatAdapter.addData(chatItems);
            recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    };


    private void sendCustomMsg(String msg) {
        if (null != imManager) {
            if (!TextUtils.isEmpty(msg)) {
                imManager.sendCustomMessage(msg, roomId + "", V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMSendCallback<V2TIMMessage>() {
                    @Override
                    public void onProgress(int i) {

                    }

                    @Override
                    public void onError(int code, String msg) {

                    }

                    @Override
                    public void onSuccess(V2TIMMessage v2TIMMessage) {
                        Message message = JSON.parseObject(msg, Message.class);
                        if (message.getCmdId() == Message.command_normal) {
                            String content = message.getContent();
                            String[] strs = content.split("-");
                            if (strs[1].startsWith("#")) {
                                ChatItem chatItem = new ChatItem(userName, strs[1], ChatItem.TYPE_NOTIFICATION);
                                chatItem.setFace(face);
                                records.add(chatItem);
                            } else {
                                ChatItem chatItem = new ChatItem(userName, strs[1], ChatItem.TYPE_ME);
                                chatItem.setFace(face);
                                records.add(chatItem);
                            }
                            et_send.setText("");
                        } else if (message.getCmdId() == Message.command_get_wx) {
                            records.add(new ChatItem(userName, msg, ChatItem.TYPE_GET_WX));
                        } else if (message.getCmdId() == Message.command_tutor) {
                            ChatItem chatItem = new ChatItem(userName, ChatItem.TYPE_NOTIFICATION);
                            chatItem.setFace(face);
                            records.add(chatItem);
                        }

                        chatAdapter.setNewData(records);
                        recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                });
            }
        }
    }

    private AlertDialog dialog;
    private boolean isClose;
    //定时关闭直播间
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: " + System.currentTimeMillis() + "----" + endTime * 1000);
            if (System.currentTimeMillis() >= endTime * 1000) {

                if (dialog == null) {
                    dialog = new AlertDialog.Builder(LiveRoomActivity.this).setTitle("提示")
                            .setMessage("直播时间已到，请下麦").setPositiveButton("确定", (dialog1, which) -> {
                                dialog1.dismiss();

                                exitRoom();
                                isClose = true;
                            }).create();
                }
                if (!dialog.isShowing() && !isDestroyed()) {
                    dialog.show();
                }

                if (!isClose) {
                    mHandler.postDelayed(() -> {
                        dialog.dismiss();
                        exitRoom();
                    }, 5000);
                }
            }
            mHandler.postDelayed(this, 60 * 1000 * 5);
        }
    };

    @Override
    public void onBackPressed() {
        exitRoom();
    }
}
