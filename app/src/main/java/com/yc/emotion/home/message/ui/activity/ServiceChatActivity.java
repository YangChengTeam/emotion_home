package com.yc.emotion.home.message.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paradigm.botkit.BotKitClient;
import com.paradigm.botkit.MessageAdapter;
import com.paradigm.botlib.BotLibClient;
import com.paradigm.botlib.Message;
import com.paradigm.botlib.MessageContentAudio;
import com.paradigm.botlib.MessageContentImage;
import com.paradigm.botlib.MessageContentMenu;
import com.paradigm.botlib.MessageContentText;
import com.yc.emotion.home.R;
import com.yc.emotion.home.base.ui.activity.BaseSlidingActivity;
import com.yc.emotion.home.message.manager.HistoryDataManager;
import com.yc.emotion.home.message.ui.fragment.ServiceChatFragment;
import com.yc.emotion.home.utils.StatusBarUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by suns  on 2019/9/30 11:47.
 */
public class ServiceChatActivity extends BaseSlidingActivity implements BotLibClient.ConnectionListener, BotLibClient.MessageListener {
    private TextView tvTitle = null;


    private static final String TAG = "DemoChatActivity";
    private ServiceChatFragment chatFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_service_chat;
    }

    @Override
    public void initViews() {

        invadeStatusBar();
        setAndroidNativeLightStatusBar();
        initView();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BotKitClient.getInstance().setPortraitRobot(ContextCompat.getDrawable(this,R.mipmap.ic_launcher1));
        BotKitClient.getInstance().setPortraitRobot(ContextCompat.getDrawable(this, R.mipmap.head_service));
        BotKitClient.getInstance().setPortraitUser(ContextCompat.getDrawable(this, R.mipmap.head_user_two));

        setContentView(getLayoutId());
        initViews();

    }

    private LinearLayout editContainer;

    private void initView() {
        ImageView ivBack = findViewById(R.id.activity_base_same_iv_back);
        tvTitle = findViewById(R.id.activity_base_same_tv_title);


        ivBack.setOnClickListener(v -> finish());
        this.chatFragment = (ServiceChatFragment) this.getFragmentManager().findFragmentById(R.id.pd_char_fragment);

        if (null != this.chatFragment) {
            View view = this.chatFragment.getView();
            if (view != null) {
                editContainer = view.findViewById(R.id.ll_edit_container);
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (null != editContainer) {

            LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) editContainer.getLayoutParams();
            int bottom = 0;
            if (StatusBarUtil.isNavigationBarExist(this)) {
                bottom = StatusBarUtil.getNavigationBarHeight(this);

            }

            layoutParams.bottomMargin = bottom;
            editContainer.setLayoutParams(layoutParams);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 为ContentTypeText类型消息注册自定义视图生成器
//        if (getMessageAdapter() != null)
//            this.getMessageAdapter().setMessageItemProvider(Message.ContentTypeText, new MyTextMessageItemProvider());

    }


    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.chatFragment.initBotClient();
    }

    protected void reloadMessageList() {
        this.chatFragment.reloadMessageList();
    }

    protected ListView getMessageListView() {
        return this.chatFragment.getMessageListView();
    }

    protected MessageAdapter getMessageAdapter() {
        return this.chatFragment.getMessageAdapter();
    }

    protected ArrayList<Message> getMessageData() {
        return this.chatFragment.getMessageData();
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void onReceivedSuggestion(ArrayList<com.paradigm.botlib.MenuItem> suggestions) {
    }


    @Override
    public void onConnectionStateChanged(int state) {


        if (state == BotLibClient.ConnectionConnectedRobot) {
            BotKitClient botClient = BotKitClient.getInstance();
            HistoryDataManager.getInstance().addHistory(this, botClient.getAccessKey(), botClient.getRobotName());
        }

//        switch (state) {
//            case BotKitClient.ConnectionIdel:
//                setTitle("连接断开");
//
//                break;
//            case BotKitClient.ConnectionConnecting:
//                setTitle("正在连接...");
//                break;
//            case BotKitClient.ConnectionConnectedRobot:
//                setTitle(BotLibClient.getInstance().getRobotName());  // 显示机器人名字
//                break;
//            default:
//                setTitle("连接失败");
//        }
    }

    @Override
    public void onAppendMessage(Message message) {

        switch (message.getContentType()) {
            case Message.ContentTypeText:
                MessageContentText contentText = (MessageContentText) message.getContent();
                Log.e(TAG, "onAppendMessage: " + contentText.getText());

                break;
            case Message.ContentTypeImage:
                MessageContentImage contentImage = (MessageContentImage) message.getContent();

                break;
            case Message.ContentTypeAudio:
                MessageContentAudio contentAudio = (MessageContentAudio) message.getContent();

                break;
            case Message.ContentTypeMenu:
                MessageContentMenu contentMenu = (MessageContentMenu) message.getContent();

                break;
        }
        Log.e(TAG, "onAppendMessage: " + message.getContent() + "---" + message.getContentType());
    }


}
