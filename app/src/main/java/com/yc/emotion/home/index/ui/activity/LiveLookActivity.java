package com.yc.emotion.home.index.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupListener;
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
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment;
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment;
import com.yc.emotion.home.constant.Constant;
import com.yc.emotion.home.im.IMManager;
import com.yc.emotion.home.index.adapter.ChatAdapter;
import com.yc.emotion.home.index.domain.bean.ChatItem;
import com.yc.emotion.home.index.domain.bean.Message;
import com.yc.emotion.home.index.presenter.LiveLookPresenter;
import com.yc.emotion.home.index.ui.fragment.CloseLiveFragment;
import com.yc.emotion.home.index.ui.fragment.LiveEndFragment;
import com.yc.emotion.home.index.ui.fragment.LiveIntroFragment;
import com.yc.emotion.home.index.ui.fragment.WxLoginFragment;
import com.yc.emotion.home.index.view.LiveLookView;
import com.yc.emotion.home.mine.domain.bean.LiveInfo;
import com.yc.emotion.home.model.bean.UserInfo;
import com.yc.emotion.home.model.bean.event.EventLoginState;
import com.yc.emotion.home.utils.GenerateTestUserSig;
import com.yc.emotion.home.utils.PreferenceUtil;
import com.yc.emotion.home.utils.SoftKeyBoardUtils;
import com.yc.emotion.home.utils.UserInfoHelper;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VOICE_CHATROOM;

/**
 * Created by suns  on 2020/5/29 14:28.
 */
public class LiveLookActivity extends BaseActivity implements LiveLookView {

    private RelativeLayout rlLoading;
    private TCHeartLayout tcHeartLayout;

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

    private TextView tvMentorName;
    private ImageView ivMentorAvatar;
    private IMManager imManager;
    private int roomId;
    private ImageView ivIntro;
    private ImageView ivTopPPt;
    private RelativeLayout rlTopPPt;
    private TextView tvPack;
    private int topHeight;

    private String tutorId;//导师id
    private LiveLookPresenter liveLookPresenter;
    private String userSig;//用户签名
    private LiveInfo mLiveInfo;
    private List<String> weixins;
    private String face = "";//用户头像
    private long startTime;
    private long endTime;

    public static void startActivity(Context context, int roomId, long starttime, long endtime) {
        Intent intent = new Intent(context, LiveLookActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("starttime", starttime);
        intent.putExtra("endtime", endtime);
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
        liveLookPresenter = new LiveLookPresenter(this, this);
        roomId = getIntent().getIntExtra("roomId", 0);
        startTime = getIntent().getLongExtra("starttime", 0);
        endTime = getIntent().getLongExtra("endtime", 0);

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
        ImageView ivWheat = findViewById(R.id.iv_wheat);
        tvMentorName = findViewById(R.id.mentor_name);
        ivMentorAvatar = findViewById(R.id.mentor_avatar);
        //简介
        ivIntro = findViewById(R.id.iv_intro);
        ImageView ivScreen = findViewById(R.id.iv_screen);
        ivTopPPt = findViewById(R.id.iv_top_ppt);
        rlTopPPt = findViewById(R.id.rl_top_ppt);
        tvPack = findViewById(R.id.tv_pack);
        RelativeLayout rlTopContainer = findViewById(R.id.top_container);


        ivWheat.setVisibility(View.GONE);
        ivScreen.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(linearLayoutManager);

        chatAdapter = new ChatAdapter(records);
        recyclerViewChat.setAdapter(chatAdapter);


        uid = UserInfoHelper.Companion.getInstance().getUid();

        liveLookPresenter.getLiveLookInfo(roomId + "");

        handleUser();

        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());

        //创建im实例
        imManager = IMManager.getInstance();

        rlLoading.setVisibility(View.VISIBLE);


        initListener();
        tvChat.post(() -> {

            tvChat.getLocationOnScreen(viewRect);
            int tvWidth = tvChat.getWidth();
            int tvHeight = tvChat.getHeight();
            topHeight = rlTopContainer.getHeight();

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

                sendCustomMsg(createMessage(Message.command_praise, "点赞"));
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
//            sendTextMsg(content);
            sendCustomMsg(createMessage(Message.command_normal, userName + "-" + content + "-" + face));
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
                    showWxFragment();
                }
            }
        });
        llWx.setOnClickListener(v -> {
            if (uid == 0) {
                WxLoginFragment wxLoginFragment = new WxLoginFragment();
                wxLoginFragment.show(getSupportFragmentManager(), "");
            } else {
                showWxFragment();
                sendCustomMsg(createMessage(Message.command_get_wx, userName));
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
        ivIntro.setOnClickListener(v -> {
            LiveIntroFragment liveIntroFragment = new LiveIntroFragment();
            Bundle bundle = new Bundle();
            if (mLiveInfo != null) {
                bundle.putString("livetitle", mLiveInfo.getLive_title());
                bundle.putString("start_time", convertTime(startTime));
                bundle.putString("end_time", convertTime(endTime));
            }
            liveIntroFragment.setArguments(bundle);
            liveIntroFragment.show(getSupportFragmentManager(), "");
        });
        tvPack.setOnClickListener(v -> setPackState());
    }

    private void showWxFragment() {
        AddWxFragment addWxFragment = new AddWxFragment();
        String wx = getWx();
        addWxFragment.setWX(wx);
        addWxFragment.show(getSupportFragmentManager(), "");
        addWxFragment.setListener(() -> {
            ClipboardManager myClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData myClip = ClipData.newPlainText("text", wx);
            myClipboard.setPrimaryClip(myClip);
            openWeiXin();
        });
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

    private void setPraise() {
        tvPraiseCount.setText(String.format(getString(R.string.praise_count), praiseCount));
        tvPraiseBottom.setText(String.valueOf(praiseCount));
        if (mLiveInfo != null) {
            tvMentorName.setText(mLiveInfo.getNickname());
            if (!isDestroyed()) {
                Glide.with(this).load(mLiveInfo.getFace()).error(R.drawable.default_avatar_72)
                        .circleCrop().into(ivMentorAvatar);
            }
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
        if (mLiveInfo != null && !isDestroyed())
            Glide.with(this).load(mLiveInfo.getPpt_img()).into(ivTopPPt);

    }


    private void starLook(LiveInfo liveInfo) {
//        loginRoom();
        joinImRoom(liveInfo);

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
                Log.e(TAG, "onEnterRoom: " + result);

                if (result > 0) {
                    rlLoading.setVisibility(View.GONE);
                    List<ChatItem> chatItems = new ArrayList<>();
                    ChatItem chatItem = new ChatItem(userName, ChatItem.TYPE_COME_CHAT);

                    chatItems.add(chatItem);
                    chatAdapter.addData(chatItems);
                    onlineCount++;
                    tvOnLineCount.setText(String.format(getString(R.string.online_count), onlineCount));
                    sendCustomMsg(createMessage(Message.command_come_room, userName));
//                    Toast.makeText(LiveLookActivity.this, "进房成功，总计耗时[(" + result + ")]ms", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(LiveLookActivity.this, "进房失败，错误码[(" + result + ")]", Toast.LENGTH_SHORT).show();
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
                tutorId = s;
//                List<ChatItem> chatItems = new ArrayList<>();
//
//                ChatItem chatItem = new ChatItem(s, ChatItem.TYPE_COME_CHAT);
//
//                chatItems.add(chatItem);
//
//                chatAdapter.addData(chatItems);

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

        });

    }

    private String createMessage(int cmdId, String content) {
        Message message = new Message(content, cmdId);
        return JSON.toJSONString(message);
    }


    private void logoutRoom() {
        sendCustomMsg(createMessage(Message.command_leave_room, "离开房间"));
        if (null != imManager) {
            imManager.logout(null);
        }

    }

    private void exitRoom() {

        logoutRoom();
        //销毁 trtc 实例
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
//            mTRTCCloud.setListener(null);
            mTRTCCloud.exitRoom();
        }
        mTRTCCloud = null;
        imManager = null;
        TRTCCloud.destroySharedInstance();
    }

    private void handleUser() {
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
                face = userInfo.getFace();
            } else {
                userName = "username-" + randomSuffix;
            }
        }
        //获取签名
//        liveLookPresenter.getUserSeg(userID);
    }


    //1.没有登录的用户不能加入房间
    //2.登录的用户能加入房间
    private void joinImRoom(LiveInfo liveInfo) {

        roomId = liveInfo.getRoomId();

        if (imManager == null) {
            imManager = IMManager.getInstance();
        }
        Log.e(TAG, "joinImRoom: ");
        //用户登录
        imManager.login(userID, GenerateTestUserSig.genTestUserSig(userID), new V2TIMCallback() {
            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "login room error: code:  " + code + "  msg:  " + msg);
            }

            @Override
            public void onSuccess() {
                Log.e(TAG, "login room success: ");
                joinRoom();
                loginRoom();
            }
        });


    }

    //进入房间收听语音直播
    private void loginRoom() {

        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.userId = userID;
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);

        trtcParams.roomId = roomId;
        trtcParams.role = TRTCCloudDef.TRTCRoleAudience;

        //进入房间
        mTRTCCloud.enterRoom(trtcParams, TRTC_APP_SCENE_VOICE_CHATROOM);

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
                logoutRoom();
                finish();

            }

            @Override
            public void onClose() {
//                PreferenceUtil.getInstance().setBooleanValue(Constant.is_exit_live, true);
                exitRoom();
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoginState event) {
        if (event.state == EventLoginState.STATE_LOGINED) {
            uid = UserInfoHelper.Companion.getInstance().getUid();
//            loginRoom();
            handleUser();
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

    public String getWx() {
        String wx = "";
        if (null != weixins) {
            wx = weixins.get(random.nextInt(weixins.size()));
        }
        return wx;
    }


    private void joinRoom() {
        if (imManager == null) {
            imManager = IMManager.getInstance();
        }

//        Log.e(TAG, "joinRoom: " + roomId);
        imManager.joinGroupRoom(roomId + "", new V2TIMCallback() {

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "join room failure: code： " + code + "  msg: " + msg);
//                    joinRoom();
            }

            @Override
            public void onSuccess() {
                imManager.setReceiveMagListener(simpleMsgListener);
                imManager.setGroupDismissListener(groupListener);
            }

        });

    }

    private V2TIMGroupListener groupListener = new V2TIMGroupListener() {
        @Override
        public void onGroupDismissed(String groupID, V2TIMGroupMemberInfo opUser) {
            super.onGroupDismissed(groupID, opUser);
            if (!isDestroyed()) {
                LiveEndFragment liveEndFragment = new LiveEndFragment();
                liveEndFragment.show(getSupportFragmentManager(), "");
            }
        }
    };

    private V2TIMSimpleMsgListener simpleMsgListener = new V2TIMSimpleMsgListener() {
        @Override
        public void onRecvGroupTextMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, String text) {
            super.onRecvGroupTextMessage(msgID, groupID, sender, text);
            List<ChatItem> chatItems = new ArrayList<>();
            ChatItem chatItem = new ChatItem(sender.getNickName(), text, ChatItem.TYPE_OTHER);
            chatItem.setFace(face);
            chatItems.add(chatItem);
            chatAdapter.addData(chatItems);
        }

        @Override
        public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
            super.onRecvGroupCustomMessage(msgID, groupID, sender, customData);

            String customMsg = new String(customData, StandardCharsets.UTF_8);
            Log.e(TAG, "onRecvGroupCustomMessage: " + "  msg  " + customMsg);
            Message message = JSON.parseObject(customMsg, Message.class);
            List<ChatItem> chatItems = new ArrayList<>();
            ChatItem chatItem = null;
            switch (message.getCmdId()) {
                case Message.command_normal:
                    String[] strs = message.getContent().split("-");
                    if (strs[1].startsWith("#") && TextUtils.equals(sender.getUserID(), tutorId)) {
                        chatItem = new ChatItem(strs[0], ChatItem.TYPE_NOTIFICATION);
                        chatItem.setFace(strs[2]);
                    } else {
                        chatItem = new ChatItem(strs[0], strs[1], ChatItem.TYPE_OTHER);
                        chatItem.setFace(strs[2]);
                    }
                    break;
                case Message.command_tutor://导师发微信
                    strs = message.getContent().split("-");
                    chatItem = new ChatItem(strs[0], ChatItem.TYPE_NOTIFICATION);
                    chatItem.setFace(strs[1]);
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
        if (imManager == null) {
            imManager = IMManager.getInstance();
        }

        if (!TextUtils.isEmpty(msg)) {
            imManager.sendCustomMessage(msg, roomId + "", V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMSendCallback<V2TIMMessage>() {
                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int code, String msg) {
                    Log.e(TAG, "send customMsg error:  code: " + code + "  msg:  " + msg);
                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {
                    Log.e(TAG, "send customMsg success: ");
                    Message message = JSON.parseObject(msg, Message.class);
                    if (message.getCmdId() == Message.command_normal) {
                        String[] strs = message.getContent().split("-");
                        ChatItem chatItem = new ChatItem(userName, strs[1], ChatItem.TYPE_ME);
                        chatItem.setFace(face);
                        records.add(chatItem);
                        et_send.setText("");
                    } else if (message.getCmdId() == Message.command_get_wx) {
                        records.add(new ChatItem(userName, msg, ChatItem.TYPE_GET_WX));
                    }
                    chatAdapter.setNewData(records);
                    recyclerViewChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                }
            });
        }

    }

    private String convertTime(long time) {
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return sd.format(new Date(time * 1000));
    }

    @Override
    public void showLiveInfo(LiveInfo data) {
        weixins = data.getWeixin();
        this.mLiveInfo = data;
        starLook(data);
    }


    @Override
    public void showUserSeg(@NotNull String usersig) {
        userSig = usersig;

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }

}
