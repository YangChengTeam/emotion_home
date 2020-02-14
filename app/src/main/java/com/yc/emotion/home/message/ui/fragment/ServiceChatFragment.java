package com.yc.emotion.home.message.ui.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.paradigm.botkit.BotKitClient;
import com.paradigm.botkit.EmojiViewHolder;
import com.paradigm.botkit.ImageActivity;
import com.paradigm.botkit.MessageAdapter;
import com.paradigm.botkit.PluginViewHolder;
import com.paradigm.botkit.RecordViewHolder;
import com.paradigm.botkit.SuggestionAdapter;
import com.paradigm.botkit.WebActivity;
import com.paradigm.botkit.message.AudioMessageItemProvider;
import com.paradigm.botkit.message.EvaluateMessageItemProvider;
import com.paradigm.botkit.message.ImageMessageItemProvider;
import com.paradigm.botkit.message.MenuMessageItemProvider;
import com.paradigm.botkit.message.RichtextMessageItemProvider;
import com.paradigm.botkit.message.TextMessageItemProvider;
import com.paradigm.botkit.message.TipMessageItemProvider;
import com.paradigm.botkit.message.WorkorderMessageItemProvider;
import com.paradigm.botkit.util.AudioTools;
import com.paradigm.botkit.util.ImageUtil;
import com.paradigm.botkit.util.StreamUtil;
import com.paradigm.botlib.BotLibClient;
import com.paradigm.botlib.MenuItem;
import com.paradigm.botlib.Message;
import com.paradigm.botlib.MessageContentAudio;
import com.yc.emotion.home.message.ui.activity.ServiceChatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suns  on 2019/9/29 15:08.
 */
public class ServiceChatFragment extends Fragment implements BotLibClient.ConnectionListener, BotLibClient.MessageListener {
    private static final String TAG = "ChatActivity";
    private static final int RequestCodeCamera = 1;
    private static final int RequestCodeAlbum = 2;
    private static final int PluginPhoto = 1001;
    private static final int PluginCamera = 1002;
    private static final int PluginHuman = 1003;
    private static final int PluginMessage = 1004;
    private static final int PluginEvaluate = 1005;
    private static final long MaxFileSize = 67108864L;
    private static final long MaxImageSize = 1048576L;
    protected ListView messageList;
    protected ListView suggestionList;
    protected EditText inputText;
    protected Button inputRecord;
    protected ImageButton inputKey;
    protected ImageButton inputAudio;
    protected ImageButton inputEmoji;
    protected ImageButton inputPlugin;
    protected Button inputSend;
    private LinearLayout emojiBack;
    private EmojiViewHolder emojiViewHolder;
    private GridLayout pluginBack;
    private PluginViewHolder pluginViewHolder;
    protected View recordBack;
    protected RecordViewHolder recordViewHolder;
    protected Timer recordTimer;
    private AudioTools audioTools;
    private MessageContentAudio audioPlayingMessage;
    private Uri uriCapture;
    private MessageAdapter messageAdapter;
    private SuggestionAdapter suggestionAdapter;
    private ArrayList<Message> messageData;
    private Handler handler = new Handler();

    public ServiceChatFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.paradigm.botkit.R.layout.pd_fragment_chat, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageUtil.init(this.getActivity());
        this.audioTools = new AudioTools(this.getActivity());
        this.initViews();
        this.initBotClient();
    }

    public void onStop() {
        super.onStop();
        this.audioTools.stopPlay();
        this.audioTools.cancelRecord();
    }

    private void initViews() {
        this.messageList = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_message_list);
        this.suggestionList = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_suggestion_list);
        this.inputText = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_text);
        this.inputRecord = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_record);
        this.inputKey = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_key);
        this.inputAudio = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_audio);
        this.inputPlugin = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_plugin);
        this.inputSend = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_send);
        this.emojiBack = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_emoji_back);
        this.emojiViewHolder = new EmojiViewHolder(this.emojiBack);
        this.pluginBack = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_plgin_back);
        this.pluginViewHolder = new PluginViewHolder(this.pluginBack);
        this.recordBack = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_record_back);
        this.recordViewHolder = new RecordViewHolder(this.recordBack);
        this.inputEmoji = this.getActivity().findViewById(com.paradigm.botkit.R.id.pd_input_emoji);
        this.inputKey.setOnClickListener(v -> {
            ServiceChatFragment.this.inputKey.setVisibility(View.GONE);
            ServiceChatFragment.this.inputAudio.setVisibility(View.VISIBLE);
            ServiceChatFragment.this.inputText.setVisibility(View.VISIBLE);
            ServiceChatFragment.this.inputRecord.setVisibility(View.GONE);
            ServiceChatFragment.this.showKeyboard();
            ServiceChatFragment.this.setSendButton();
        });
        this.inputAudio.setOnClickListener(v -> {
            ServiceChatFragment.this.inputKey.setVisibility(View.VISIBLE);
            ServiceChatFragment.this.inputAudio.setVisibility(View.GONE);
            ServiceChatFragment.this.inputText.setVisibility(View.GONE);
            ServiceChatFragment.this.inputRecord.setVisibility(View.VISIBLE);
            ServiceChatFragment.this.emojiBack.setVisibility(View.GONE);
            ServiceChatFragment.this.pluginBack.setVisibility(View.GONE);
            ServiceChatFragment.this.suggestionList.setVisibility(View.GONE);
            ServiceChatFragment.this.hideKeyboard();
            ServiceChatFragment.this.setSendButton();
        });
        this.inputEmoji.setOnClickListener(view -> {
            if (ServiceChatFragment.this.emojiBack.getVisibility() == View.GONE) {
                ServiceChatFragment.this.emojiBack.setVisibility(View.VISIBLE);
                ServiceChatFragment.this.pluginBack.setVisibility(View.GONE);
                ServiceChatFragment.this.hideKeyboard();
                if (ServiceChatFragment.this.inputRecord.getVisibility() == View.VISIBLE) {
                    ServiceChatFragment.this.inputKey.setVisibility(View.GONE);
                    ServiceChatFragment.this.inputAudio.setVisibility(View.VISIBLE);
                    ServiceChatFragment.this.inputText.setVisibility(View.VISIBLE);
                    ServiceChatFragment.this.inputRecord.setVisibility(View.GONE);
                    ServiceChatFragment.this.setSendButton();
                }
            } else {
                ServiceChatFragment.this.emojiBack.setVisibility(View.GONE);
            }

        });
        this.inputPlugin.setOnClickListener(v -> {
            if (ServiceChatFragment.this.pluginBack.getVisibility() == View.GONE) {
                ServiceChatFragment.this.pluginBack.setVisibility(View.VISIBLE);
                ServiceChatFragment.this.emojiBack.setVisibility(View.GONE);
                ServiceChatFragment.this.hideKeyboard();
                if (ServiceChatFragment.this.inputRecord.getVisibility() == View.VISIBLE) {
                    ServiceChatFragment.this.inputKey.setVisibility(View.GONE);
                    ServiceChatFragment.this.inputAudio.setVisibility(View.VISIBLE);
                    ServiceChatFragment.this.inputText.setVisibility(View.VISIBLE);
                    ServiceChatFragment.this.inputRecord.setVisibility(View.GONE);
                    ServiceChatFragment.this.setSendButton();
                }
            } else {
                ServiceChatFragment.this.pluginBack.setVisibility(View.GONE);
            }

        });
        this.inputText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                ServiceChatFragment.this.setSendButton();
                String text = s.toString().trim();
                if (text.length() > View.VISIBLE) {
                    BotKitClient.getInstance().askSuggestion(text);
                } else {
                    ServiceChatFragment.this.suggestionList.setVisibility(View.GONE);
                }

            }
        });
        this.inputText.setOnTouchListener((v, event) -> {
            ServiceChatFragment.this.emojiBack.setVisibility(View.GONE);
            ServiceChatFragment.this.pluginBack.setVisibility(View.GONE);
            ServiceChatFragment.this.showKeyboard();
            return false;
        });
        this.inputRecord.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case View.VISIBLE:
                    ServiceChatFragment.this.inputRecord.setText(com.paradigm.botkit.R.string.pd_release_to_send);
                    ServiceChatFragment.this.startRecord();
                    break;
                case 1:
                case 3:
                    ServiceChatFragment.this.inputRecord.setText(com.paradigm.botkit.R.string.pd_hold_to_talk);
                    if (ServiceChatFragment.this.recordViewHolder.isCancel()) {
                        ServiceChatFragment.this.cancelRecord();
                    } else {
                        ServiceChatFragment.this.finishRecord();
                    }
                    break;
                case 2:
                    if (event.getY() < 10.0F) {
                        ServiceChatFragment.this.inputRecord.setText(com.paradigm.botkit.R.string.pd_release_to_cancel);
                        ServiceChatFragment.this.recordViewHolder.setCancel(true);
                    } else {
                        ServiceChatFragment.this.inputRecord.setText(com.paradigm.botkit.R.string.pd_release_to_send);
                        ServiceChatFragment.this.recordViewHolder.setCancel(false);
                    }
            }

            return true;
        });
        this.emojiViewHolder.setOnEmojiClickListener(emoji -> {
            int index = ServiceChatFragment.this.inputText.getSelectionStart();
            String text = "I want to input str";
            Editable edit = ServiceChatFragment.this.inputText.getEditableText();
            if (index >= 0 && index < edit.length()) {
                edit.insert(index, emoji);
            } else {
                edit.append(emoji);
            }

        });
        this.pluginViewHolder.insertItem(com.paradigm.botkit.R.drawable.pd_plugin_photo, com.paradigm.botkit.R.string.pd_photo, 1001);
        this.pluginViewHolder.insertItem(com.paradigm.botkit.R.drawable.pd_plugin_camera, com.paradigm.botkit.R.string.pd_camera, 1002);
        this.pluginViewHolder.insertItem(com.paradigm.botkit.R.drawable.pd_plugin_human, com.paradigm.botkit.R.string.pd_human, 1003);
//        this.pluginViewHolder.insertItem(com.paradigm.botkit.R.drawable.pd_plugin_message, com.paradigm.botkit.R.string.pd_message, 1004);
        this.pluginViewHolder.insertItem(com.paradigm.botkit.R.drawable.pd_plugin_evaluate, com.paradigm.botkit.R.string.pd_evaluate, 1005);
        this.pluginViewHolder.setOnPluginClickListener(tag -> {
            ServiceChatFragment.this.pluginBack.setVisibility(View.GONE);
            if (tag == 1001) {
                ServiceChatFragment.this.getPhotoFromAlbum();
            } else if (tag == 1002) {
                ServiceChatFragment.this.getPhotoFromCamera();
            } else if (tag == 1003) {
                BotKitClient.getInstance().transferToHumanServices();
            } else {
                Intent intent;
                if (tag == 1004) {
                    intent = new Intent();
                    intent.setClass(ServiceChatFragment.this.getActivity(), WebActivity.activityClass);
                    intent.putExtra("url", BotKitClient.getInstance().getLeaveMessageUrl());
                    ServiceChatFragment.this.startActivity(intent);
                } else if (tag == 1005) {
                    intent = new Intent();
                    intent.setClass(ServiceChatFragment.this.getActivity(), WebActivity.activityClass);
                    intent.putExtra("url", BotKitClient.getInstance().getHumanEvaluateUrl());
                    ServiceChatFragment.this.startActivity(intent);
                }
            }

        });
        this.inputSend.setOnClickListener(v -> {
            String text = ServiceChatFragment.this.inputText.getText().toString().trim();
            if (text.length() > View.VISIBLE) {
                BotKitClient.getInstance().askQuestion(text);
                ServiceChatFragment.this.inputText.setText("");
            }

        });
    }



    public void initBotClient() {
        BotKitClient botClient = BotKitClient.getInstance();
        botClient.setConnectionListener(this);
        botClient.setMessageListener(this);
        this.messageAdapter = new MessageAdapter(this.getActivity());
        this.messageAdapter.setMessageItemProvider(7, new TipMessageItemProvider());
        this.messageAdapter.setMessageItemProvider(1, new TextMessageItemProvider());
        this.messageAdapter.setMessageItemProvider(2, new MenuMessageItemProvider((item, type) -> BotKitClient.getInstance().askQuestion(item, type)));
        this.messageAdapter.setMessageItemProvider(3, new ImageMessageItemProvider(content -> {
            Intent intent = new Intent();
            intent.setClass(ServiceChatFragment.this.getActivity(), ImageActivity.class);
            intent.putExtra("path", content.getDataPath());
            ServiceChatFragment.this.startActivity(intent);
        }));
        this.messageAdapter.setMessageItemProvider(5, new AudioMessageItemProvider(content -> {

            File file = new File(ServiceChatFragment.this.getActivity().getFilesDir(), content.getDataPath());
            if (ServiceChatFragment.this.audioTools.getPlayingFile() != null && ServiceChatFragment.this.audioPlayingMessage == content) {
                ServiceChatFragment.this.audioTools.stopPlay();
                ServiceChatFragment.this.audioPlayingMessage = null;
            } else {
                ServiceChatFragment.this.audioTools.startPlay(file);
                ServiceChatFragment.this.audioPlayingMessage = content;
            }

        }));
        this.messageAdapter.setMessageItemProvider(4, new RichtextMessageItemProvider(content -> {
            Intent intent = new Intent();
            intent.setClass(ServiceChatFragment.this.getActivity(), WebActivity.activityClass);
            intent.putExtra("url", content.getUrl());
            ServiceChatFragment.this.startActivity(intent);
        }));
        this.messageAdapter.setMessageItemProvider(8, new WorkorderMessageItemProvider(content -> {
            Intent intent = new Intent();
            intent.setClass(ServiceChatFragment.this.getActivity(), WebActivity.activityClass);
            intent.putExtra("url", content.getUrl());
            ServiceChatFragment.this.startActivity(intent);
        }));
        this.messageAdapter.setMessageItemProvider(9, new EvaluateMessageItemProvider(content -> {
            Intent intent = new Intent();
            intent.setClass(ServiceChatFragment.this.getActivity(), WebActivity.activityClass);
            intent.putExtra("url", content.getUrl());
            ServiceChatFragment.this.startActivity(intent);
        }));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        botClient.removeMessage(cal.getTime());
        this.messageData = new ArrayList<>();
        this.messageAdapter.setMessageData(this.messageData);
        this.messageList.setAdapter(this.messageAdapter);
        this.suggestionAdapter = new SuggestionAdapter(this.getActivity());
        this.suggestionList.setAdapter(this.suggestionAdapter);
        this.suggestionList.setOnItemClickListener((parent, view, position, id) -> {
            MenuItem item = (MenuItem) ServiceChatFragment.this.suggestionList.getItemAtPosition(position);
            BotKitClient.getInstance().askQuestion(item.getContent());
            ServiceChatFragment.this.inputText.setText("");
        });
        this.reloadMessageList();
        botClient.connect();
    }

    public void reloadMessageList() {
        this.messageData.clear();
        this.messageData.addAll(BotKitClient.getInstance().getMessageList());
        this.messageAdapter.notifyDataSetChanged();
        if (this.messageData.size() > 0) {
            this.messageList.setSelection(this.messageData.size() - 1);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream is;
        if (requestCode == 1) {
            if (resultCode == -1) {
                File imgFile = null;

                try {
                    imgFile = File.createTempFile("photo", ".jpg", this.getActivity().getCacheDir());
                    is = this.getActivity().getContentResolver().openInputStream(this.uriCapture);
                    FileOutputStream os = new FileOutputStream(imgFile);
                    long fileSize = StreamUtil.StreamTransfer(is, os, 67108864L);
                    os.close();
                    is.close();
                    if (fileSize > 67108864L) {
                        imgFile = null;
                    } else if (fileSize > 1048576L) {
                        File tmpFile = File.createTempFile("photo", ".jpg", this.getActivity().getCacheDir());
                        ImageUtil.compressImage(imgFile, 4000, 4000, Bitmap.CompressFormat.JPEG, 80, tmpFile.getPath());
                        imgFile.delete();
                        imgFile = tmpFile;
                    }
                } catch (Exception var12) {
                    var12.printStackTrace();
                    imgFile = null;
                }

                this.getActivity().getContentResolver().delete(this.uriCapture, null, null);
                if (imgFile != null) {
                    BotKitClient.getInstance().askQuestionImage(imgFile);
                }
            }
        } else if (requestCode == 2 && resultCode == -1) {
            Uri imgUri = data.getData();
            is = null;

            File imgFile;
            try {
                imgFile = File.createTempFile("photo", ".jpg", this.getActivity().getCacheDir());
                is = this.getActivity().getContentResolver().openInputStream(imgUri);
                FileOutputStream os = new FileOutputStream(imgFile);
                long fileSize = StreamUtil.StreamTransfer(is, os, 67108864L);
                os.close();
                is.close();
                if (fileSize > 67108864L) {
                    imgFile = null;
                } else if (fileSize > 1048576L) {
                    File tmpFile = File.createTempFile("photo", ".jpg", this.getActivity().getCacheDir());
                    ImageUtil.compressImage(imgFile, 4000, 4000, Bitmap.CompressFormat.JPEG, 80, tmpFile.getPath());
                    imgFile.delete();
                    imgFile = tmpFile;
                }
            } catch (Exception var11) {
                var11.printStackTrace();
                imgFile = null;
            }

            if (imgFile != null) {
                BotKitClient.getInstance().askQuestionImage(imgFile);
            }
        }

    }

    private void showKeyboard() {
        this.inputText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(this.inputText,0);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.inputText.getWindowToken(), 2);
    }

    private void setSendButton() {
        boolean showSend = this.inputText.getVisibility() == View.VISIBLE && this.inputText.getText().length() > 0;
        this.inputSend.setVisibility(showSend ? View.VISIBLE : View.GONE);
        this.inputPlugin.setVisibility(showSend ? View.GONE : View.VISIBLE);
    }

    private void startRecord() {
        if (this.selfPermissionGranted("android.permission.RECORD_AUDIO")) {
            this.audioTools.startRecord();
            this.recordBack.setVisibility(View.VISIBLE);
            this.recordViewHolder.setCancel(false);
            this.recordViewHolder.setVolume(0.0f);
            this.recordTimer = new Timer();
            this.recordTimer.schedule(new TimerTask() {
                public void run() {
                    ServiceChatFragment.this.handler.post(() -> {
                        ServiceChatFragment.this.recordViewHolder.setVolume(ServiceChatFragment.this.audioTools.getRecordVolume());
                        if (ServiceChatFragment.this.audioTools.getRecordDuration() > 60000L) {
                            ServiceChatFragment.this.finishRecord();
                        }

                    });
                }
            }, 500L, 500L);
        }
    }

    private void finishRecord() {
        if (this.recordTimer != null) {
            this.recordTimer.cancel();
            this.recordTimer = null;
        }

        File file = this.audioTools.finishRecord();
        this.recordBack.setVisibility(View.GONE);
        if (file != null) {
            BotKitClient.getInstance().askQuestionAudio(file);
        }

    }

    private void cancelRecord() {
        if (this.recordTimer != null) {
            this.recordTimer.cancel();
            this.recordTimer = null;
        }

        this.audioTools.cancelRecord();
        this.recordBack.setVisibility(View.GONE);
    }

    private void getPhotoFromCamera() {
        if (this.selfPermissionGranted("android.permission.CAMERA")) {
            if (this.selfPermissionGranted("android.permission.WRITE_EXTERNAL_STORAGE")) {
                String name = "photo" + System.currentTimeMillis();
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", name);
                contentValues.put("_display_name", name + ".jpeg");
                contentValues.put("mime_type", "image/jpeg");
                this.uriCapture = this.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("output", this.uriCapture);
                this.startActivityForResult(intent, 1);
            }
        }
    }

    private void getPhotoFromAlbum() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(intent, 2);
    }

    public boolean selfPermissionGranted(String permission) {
        boolean result = true;
        int targetSdkVersion = 0;

        try {
            PackageInfo info = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (targetSdkVersion >= 23) {
                result = this.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if (!result) {
                    this.requestPermissions(new String[]{permission}, 0);
                }
            } else {
                result = PermissionChecker.checkSelfPermission(this.getContext(), permission) == PackageManager.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    public ListView getMessageListView() {
        return this.messageList;
    }

    public MessageAdapter getMessageAdapter() {
        return this.messageAdapter;
    }

    public ArrayList<Message> getMessageData() {
        return this.messageData;
    }

    public void onConnectionStateChanged(int state) {
        BotKitClient botClient = BotKitClient.getInstance();
        ServiceChatActivity activity = (ServiceChatActivity) this.getActivity();
        if (activity != null) {
            switch (state) {
                case 0:
                    activity.setTitle(com.paradigm.botkit.R.string.pd_connection_closed);
                    break;
                case 1:
                    activity.setTitle(com.paradigm.botkit.R.string.pd_connecting);
                    break;
                case 2:
                    activity.setTitle(botClient.getRobotName());
                    break;
                case 3:
                    activity.setTitle(botClient.getRobotName());
                    break;
                default:
                    activity.setTitle(com.paradigm.botkit.R.string.pd_connection_failed);
            }
        }

        boolean isEnableHuman = state == 2 && botClient.isEnableHuman();
        boolean isEnableMessage = state == 2;
        boolean isEnableEvaluate = state == 3 && botClient.isEnableEvaluate();
        this.pluginViewHolder.hideItemWithTag(1003, !isEnableHuman);
        this.pluginViewHolder.hideItemWithTag(1004, !isEnableMessage);
        this.pluginViewHolder.hideItemWithTag(1005, !isEnableEvaluate);
        if (activity instanceof BotLibClient.ConnectionListener) {
            activity.onConnectionStateChanged(state);
        }

    }

    public void onReceivedSuggestion(ArrayList<MenuItem> suggestions) {
        this.suggestionAdapter.setSuggestionList(suggestions);
        this.suggestionList.setVisibility(suggestions.isEmpty() ? View.GONE : View.VISIBLE);
        if (this.getActivity() instanceof BotLibClient.MessageListener) {
            ((BotLibClient.MessageListener) this.getActivity()).onReceivedSuggestion(suggestions);
        }

    }

    public void onAppendMessage(Message message) {
        this.messageData.add(message);
        this.messageAdapter.notifyDataSetChanged();
        if (this.getActivity() instanceof BotLibClient.MessageListener) {
            ((BotLibClient.MessageListener) this.getActivity()).onAppendMessage(message);
        }

    }
}
