package com.yc.emotion.home.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

import com.bumptech.glide.Glide;
import com.wapchief.likestarlibrary.like.TCHeartLayout;
import com.yc.emotion.home.R;
import com.yc.emotion.home.base.ui.activity.BaseActivity;
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment;
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment;
import com.yc.emotion.home.index.adapter.ChatAdapter;
import com.yc.emotion.home.index.domain.bean.ChatItem;
import com.yc.emotion.home.index.ui.fragment.CloseLiveFragment;
import com.yc.emotion.home.index.ui.fragment.LiveIntroFragment;
import com.yc.emotion.home.index.ui.fragment.WxLoginFragment;
import com.yc.emotion.home.mine.domain.bean.LiveInfo;
import com.yc.emotion.home.model.bean.event.EventLoginState;
import com.yc.emotion.home.utils.SoftKeyBoardUtils;
import com.yc.emotion.home.utils.UserInfoHelper;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



/**
 * Created by suns  on 2020/5/29 14:28.
 */
public class LiveVideoActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

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

    private int uid;

    private TextView tvMentorName;
    private ImageView ivMentorAvatar;


    private ImageView ivIntro;
    private ImageView ivTopPPt;
    private RelativeLayout rlTopPPt;
    private TextView tvPack;
    private int topHeight;


    private List<String> weixins;
    private LiveInfo liveInfo;
    private MediaPlayer mediaPlayer;
    private ImageView ivWheat;
    private boolean isPrepared;
    private ImageView ivPlayCon;
    private LinearLayout llPlayVideo;

    public static void startActivity(Context context, LiveInfo liveInfo) {
        Intent intent = new Intent(context, LiveVideoActivity.class);
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
        weixins = liveInfo.getWeixin();

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
        ivWheat = findViewById(R.id.iv_wheat);
        tvMentorName = findViewById(R.id.mentor_name);
        ivMentorAvatar = findViewById(R.id.mentor_avatar);
        //简介
        ivIntro = findViewById(R.id.iv_intro);
        ImageView ivScreen = findViewById(R.id.iv_screen);
        ivTopPPt = findViewById(R.id.iv_top_ppt);
        rlTopPPt = findViewById(R.id.rl_top_ppt);
        tvPack = findViewById(R.id.tv_pack);
        RelativeLayout rlTopContainer = findViewById(R.id.top_container);
        llPlayVideo = findViewById(R.id.ll_play_video);
        ivPlayCon = findViewById(R.id.iv_play_icon);


        llPlayVideo.setVisibility(View.VISIBLE);
        tvPack.setVisibility(View.GONE);

        tvLiveState.setText("直播回放");
//        ivWheat.setVisibility(View.GONE);
        ivScreen.setVisibility(View.GONE);
        ivWheat.setVisibility(View.GONE);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        chatAdapter = new ChatAdapter(records);
        recyclerViewChat.setAdapter(chatAdapter);

        uid = UserInfoHelper.Companion.getInstance().getUid();
        if (liveInfo != null)
            startPlay(liveInfo.getRecord_url());


        setPraise();

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

    private void startPlay(String url) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void destroyPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }

    private void initListener() {
        llRaise.setOnClickListener(v -> {

            tcHeartLayout.addFavor();
//                sendMsg(Message.command_praise, "点赞");
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
                Toast.makeText(LiveVideoActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                return;
            }
//            sendTextMsg(content);
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
                    tvSendBg.setBackgroundColor(ContextCompat.getColor(LiveVideoActivity.this, R.color.color_f8f8f8));
                    tvSendBg.setTextColor(ContextCompat.getColor(LiveVideoActivity.this, R.color.color_bdbcbc));
                } else {
                    tvSendBg.setBackgroundColor(ContextCompat.getColor(LiveVideoActivity.this, R.color.main_theme_color));

                    tvSendBg.setTextColor(ContextCompat.getColor(LiveVideoActivity.this, R.color.white));
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
            AddWxFragment addWxFragment = new AddWxFragment();
            addWxFragment.show(getSupportFragmentManager(), "");

        });
        llShare.setOnClickListener(v -> {

            ShareAppFragment shareAppFragment = new ShareAppFragment();
            shareAppFragment.show(getSupportFragmentManager(), "");

        });
        ivClose.setOnClickListener(v -> {
            showCloseFragment();
        });
        ivIntro.setOnClickListener(v -> {
            LiveIntroFragment liveIntroFragment = new LiveIntroFragment();
            Bundle bundle = new Bundle();
            if (liveInfo != null)
                bundle.putString("livetitle", liveInfo.getLive_title());
            liveIntroFragment.setArguments(bundle);
            liveIntroFragment.show(getSupportFragmentManager(), "");
        });
        tvPack.setOnClickListener(v -> setPackState());
        ivWheat.setOnClickListener(v -> {
            if (isPrepared && mediaPlayer != null) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    setLiveState(true);
                }
            }
        });
        llPlayVideo.setOnClickListener(v -> {
            if (isPrepared && mediaPlayer != null) {
                playOrPause();
                setLiveState(true);
            }
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
        if (liveInfo != null) {
            tvMentorName.setText(liveInfo.getNickname());
            if (!isDestroyed()) {
                Glide.with(this).load(liveInfo.getFace()).error(R.drawable.default_avatar_72)
                        .circleCrop().into(ivMentorAvatar);
                Glide.with(this).load(liveInfo.getPpt_img()).into(ivTopPPt);
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


    }


    private void playOrPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                ivPlayCon.setImageResource(R.mipmap.live_play_start_icon);
            } else {
                mediaPlayer.start();
                ivPlayCon.setImageResource(R.mipmap.live_play_pause_icon);
            }
        }
    }


    private void showCloseFragment() {
        CloseLiveFragment closeLiveFragment = new CloseLiveFragment();
        closeLiveFragment.show(getSupportFragmentManager(), "");
        closeLiveFragment.setOnCloseListener(new CloseLiveFragment.OnCloseListener() {
            @Override
            public void onMinimize() {
                destroyPlayer();


            }

            @Override
            public void onClose() {

                destroyPlayer();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoginState event) {
        if (event.state == EventLoginState.STATE_LOGINED) {
            uid = UserInfoHelper.Companion.getInstance().getUid();
//            loginRoom();
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


    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        rlLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError: code: " + what + "  extra: " + extra);
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        ivPlayCon.setImageResource(R.mipmap.live_play_start_icon);
    }


}
