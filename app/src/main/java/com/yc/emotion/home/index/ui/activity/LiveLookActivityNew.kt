package com.yc.emotion.home.index.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.imsdk.v2.*
import com.tencent.liteav.TXLiteAVCode
import com.tencent.trtc.TRTCCloud
import com.tencent.trtc.TRTCCloudDef
import com.tencent.trtc.TRTCCloudDef.TRTCParams
import com.tencent.trtc.TRTCCloudListener
import com.wapchief.likestarlibrary.like.TCHeartLayout
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment
import com.yc.emotion.home.base.ui.fragment.common.AddWxFragment.OnToWxListener
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment
import com.yc.emotion.home.constant.Constant
import com.yc.emotion.home.im.IMManager
import com.yc.emotion.home.index.adapter.ChatAdapter
import com.yc.emotion.home.index.domain.bean.ChatItem
import com.yc.emotion.home.index.domain.bean.Message
import com.yc.emotion.home.index.presenter.LiveLookPresenter
import com.yc.emotion.home.index.ui.activity.LiveLookActivityNew
import com.yc.emotion.home.index.ui.fragment.CloseLiveFragment
import com.yc.emotion.home.index.ui.fragment.LiveEndFragment
import com.yc.emotion.home.index.ui.fragment.LiveIntroFragment
import com.yc.emotion.home.index.ui.fragment.WxLoginFragment
import com.yc.emotion.home.index.view.LiveLookView
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.utils.GenerateTestUserSig
import com.yc.emotion.home.utils.PreferenceUtil
import com.yc.emotion.home.utils.SoftKeyBoardUtils
import com.yc.emotion.home.utils.SoftKeyBoardUtils.OnSoftKeyBoardChangeListener
import com.yc.emotion.home.utils.UserInfoHelper.Companion.instance
import kotlinx.android.synthetic.main.live_room_layout.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by suns  on 2020/5/29 14:28.
 */
class LiveLookActivityNew : BaseActivity(), LiveLookView {

//    private var tcHeartLayout: TCHeartLayout? = null

    private val random = Random()

    private val viewRect = IntArray(2)
    private val rect = Rect()


    //    消息列表
    private val records = ArrayList<ChatItem>()
    private lateinit var chatAdapter: ChatAdapter

    private var userID: String? = null

    private var praiseCount = 200 //点赞人数

    private var userName: String? = null
    private var uid = 0
    private var onlineCount = 368 //在线人数基数
    private var mTRTCCloud: TRTCCloud? = null

    private var imManager: IMManager? = null
    private var roomId = 0

    private var topHeight = 0
    private var tutorId //导师id
            : String? = null
    private lateinit var liveLookPresenter: LiveLookPresenter
    private var userSig //用户签名
            : String? = null
    private var mLiveInfo: LiveInfo? = null
    private var weixins: List<String>? = null
    private var face: String? = "" //用户头像
    private var startTime: Long = 0
    private var endTime: Long = 0
    override fun getLayoutId(): Int {
        return R.layout.live_room_layout
    }

    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置fitsSystemWindows属性后添加
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.live_room_layout)
        initViews()
    }

    private fun initView() {
        liveLookPresenter = LiveLookPresenter(this, this)
        roomId = intent.getIntExtra("roomId", 0)
        startTime = intent.getLongExtra("starttime", 0)
        endTime = intent.getLongExtra("endtime", 0)

        iv_wheat.visibility = View.GONE
        iv_screen.visibility = View.GONE
        val linearLayoutManager = LinearLayoutManager(this)
        //        linearLayoutManager.setStackFromEnd(true);
        recyclerView_chat.layoutManager = linearLayoutManager
        chatAdapter = ChatAdapter(records)
        recyclerView_chat.adapter = chatAdapter
        uid = instance.getUid()

        liveLookPresenter.getLiveLookInfo(roomId.toString() + "")
        handleUser()
        mTRTCCloud = TRTCCloud.sharedInstance(applicationContext)

        //创建im实例
        imManager = IMManager.getInstance()
        loading_connect_listen.visibility = View.VISIBLE
        initListener()
        tv_chat.post {
            tv_chat.getLocationOnScreen(viewRect)
            val tvWidth = tv_chat.width
            val tvHeight = tv_chat.height
            topHeight = top_container.height
            rect.left = viewRect[0]
            rect.top = viewRect[1]
            rect.right = viewRect[0] + tvWidth
            rect.bottom = viewRect[1] + tvHeight
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                if (uid == 0) { //未登录
                    val wxLoginFragment = WxLoginFragment()
                    wxLoginFragment.show(supportFragmentManager, "")
                } else {
                    user_input_layout.visibility = View.VISIBLE
                    SoftKeyBoardUtils.showSoftBoard(live_send_message)
                }
            } else {
                SoftKeyBoardUtils.hideSoftBoard(live_send_message)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initListener() {
        ll_raise.setOnClickListener { v: View? ->
            if (uid == 0) {
                val wxLoginFragment = WxLoginFragment()
                wxLoginFragment.show(supportFragmentManager, "")
            } else {
                heart_layout.addFavor()
                sendCustomMsg(createMessage(Message.command_praise, "点赞"))
                praiseCount++
                setPraise()
            }
        }
        SoftKeyBoardUtils.setListener(this, object : OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {}
            override fun keyBoardHide(height: Int) {
                user_input_layout.visibility = View.GONE
            }
        })
        send_txt_msg_submit_container.setOnClickListener { v: View? ->
            val content = live_send_message.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this@LiveLookActivityNew, "不能发送空消息", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //            sendTextMsg(content);
            sendCustomMsg(createMessage(Message.command_normal, "$userName-$content-$face"))
        }
        live_send_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val result = s.toString()
                if (TextUtils.isEmpty(result)) {
                    send_txt_msg_submit.setBackgroundColor(ContextCompat.getColor(this@LiveLookActivityNew, R.color.color_f8f8f8))
                    send_txt_msg_submit.setTextColor(ContextCompat.getColor(this@LiveLookActivityNew, R.color.color_bdbcbc))
                } else {
                    send_txt_msg_submit.setBackgroundColor(ContextCompat.getColor(this@LiveLookActivityNew, R.color.main_theme_color))
                    send_txt_msg_submit.setTextColor(ContextCompat.getColor(this@LiveLookActivityNew, R.color.white))
                }
            }
        })
        chatAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val chatItem = chatAdapter.getItem(position)
            if (null != chatItem) {
                if (chatItem.itemType == ChatItem.TYPE_NOTIFICATION) {
                    showWxFragment()
                } else if (chatItem.itemType == ChatItem.TYPE_GET_WX) {
                    showWxFragment()
                }
            }
        }
        ll_wx.setOnClickListener { v: View? ->
            if (uid == 0) {
                val wxLoginFragment = WxLoginFragment()
                wxLoginFragment.show(supportFragmentManager, "")
            } else {
                showWxFragment()
//                sendCustomMsg(createMessage(Message.command_get_wx, userName))
            }
        }
        ll_share.setOnClickListener { v: View? ->
            if (uid == 0) {
                val wxLoginFragment = WxLoginFragment()
                wxLoginFragment.show(supportFragmentManager, "")
            } else {
                val shareAppFragment = ShareAppFragment()
                shareAppFragment.show(supportFragmentManager, "")
            }
        }
        close_live.setOnClickListener { v: View? -> showCloseFragment() }
        iv_intro.setOnClickListener { v: View? ->
            val liveIntroFragment = LiveIntroFragment()
            val bundle = Bundle()
            mLiveInfo?.let {
                bundle.putString("livetitle", it.live_title)
                bundle.putString("start_time", convertTime(startTime))
                bundle.putString("end_time", convertTime(endTime))
            }
            liveIntroFragment.arguments = bundle
            liveIntroFragment.show(supportFragmentManager, "")
        }
        tv_pack.setOnClickListener { v: View? -> setPackState() }
    }

    private fun showWxFragment() {
        sendCustomMsg(createMessage(Message.command_get_wx, userName))
        val addWxFragment = AddWxFragment()
        val wx = wx
        addWxFragment.setWX(wx)
        addWxFragment.show(supportFragmentManager, "")
        addWxFragment.setListener(object : OnToWxListener {
            override fun onToWx() {
                val myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClip = ClipData.newPlainText("text", wx)
                myClipboard.primaryClip = myClip
                openWeiXin()
            }
        })
    }

    private fun setPackState() {
        if (rl_top_ppt.visibility == View.VISIBLE) {
            rl_top_ppt.visibility = View.GONE
        } else {
            rl_top_ppt.visibility = View.VISIBLE
        }
        if (rl_top_ppt.visibility == View.GONE) {
            val drawable = resources.getDrawable(R.mipmap.arrow_down)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            tv_pack.setCompoundDrawables(null, null, drawable, null)
            val layoutParams = tv_pack.layoutParams as RelativeLayout.LayoutParams
            layoutParams.topMargin = topHeight + UIUtil.dip2px(this, 15.0)
            tv_pack.layoutParams = layoutParams
            tv_pack.text = "展开"
        } else {
            val drawable = resources.getDrawable(R.mipmap.arrow_up)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            tv_pack.setCompoundDrawables(null, null, drawable, null)
            //            layoutParams.topMargin = 0;
            tv_pack.text = "收起"
        }
    }

    private fun setPraise() {
        praise_count.text = String.format(getString(R.string.praise_count), praiseCount)
        tv_praise_bottom.text = praiseCount.toString()
        mLiveInfo?.let {
            mentor_name.text = it.nickname
            if (!isDestroyed) {
                Glide.with(this).load(it.face).error(R.drawable.default_avatar_72)
                        .circleCrop().into(mentor_avatar)
            }
        }
    }

    private fun setLiveState(isLive: Boolean) {
        live_state.visibility = if (isLive) View.GONE else View.VISIBLE
        iv_live_anim.visibility = if (isLive) View.VISIBLE else View.GONE
        online_count.visibility = if (isLive) View.VISIBLE else View.GONE
        praise_count.visibility = if (isLive) View.VISIBLE else View.GONE
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        val animationDrawable = iv_live_anim.background as AnimationDrawable
        if (isLive) {
            animationDrawable.start()
        } else {
            animationDrawable.stop()
        }
        if (isLive) {
            if (!isDestroyed) {
                mLiveInfo?.let {
                    Glide.with(this).load(it.ppt_img).into(iv_top_ppt)
                }
            }
        } else {
            if (!isDestroyed) {
                Glide.with(this).clear(iv_top_ppt)
            }
        }
    }

    private fun starLook(liveInfo: LiveInfo) {
//        loginRoom();
        joinImRoom(liveInfo)
        mTRTCCloud?.setListener(object : TRTCCloudListener() {
            override fun onError(errCode: Int, errMsg: String, bundle: Bundle) {
                super.onError(errCode, errMsg, bundle)
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    exitRoom()
                }
            }

            override fun onEnterRoom(result: Long) {
                super.onEnterRoom(result)
                Log.e(TAG, "onEnterRoom: $result")
                if (result > 0) {
                    loading_connect_listen.visibility = View.GONE
                    val chatItems: MutableList<ChatItem> = ArrayList()
                    val chatItem = ChatItem(userName, ChatItem.TYPE_COME_CHAT)
                    chatItems.add(chatItem)
                    chatAdapter.addData(chatItems)
                    onlineCount++
                    online_count.text = String.format(getString(R.string.online_count), onlineCount)
                    sendCustomMsg(createMessage(Message.command_come_room, userName))
                    //                    Toast.makeText(LiveLookActivity.this, "进房成功，总计耗时[(" + result + ")]ms", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(LiveLookActivity.this, "进房失败，错误码[(" + result + ")]", Toast.LENGTH_SHORT).show();
                }
                online_count.text = String.format(getString(R.string.online_count), onlineCount)
                //
            }

            //reason	离开房间原因，0：主动调用 exitRoom 退房；1：被服务器踢出当前房间；2：当前房间整个被解散。
            override fun onExitRoom(reason: Int) {
                super.onExitRoom(reason)
                Log.e(TAG, "onExitRoom: ")
            }

            //导师进入房间会回调
            override fun onRemoteUserEnterRoom(s: String) {
                super.onRemoteUserEnterRoom(s)
                Log.e(TAG, "onRemoteUserEnterRoom: $s")
                tutorId = s

            }

            override fun onRemoteUserLeaveRoom(s: String, i: Int) {
                super.onRemoteUserLeaveRoom(s, i)
                Log.e(TAG, "onRemoteUserLeaveRoom: ")
            }

            override fun onFirstAudioFrame(s: String) {
                super.onFirstAudioFrame(s)
                Log.e(TAG, "onFirstAudioFrame: $s")
                setLiveState(true)
                setPraise()
            }
        })
    }

    private fun createMessage(cmdId: Int, content: String?): String {
        val message = Message(content, cmdId)
        return JSON.toJSONString(message)
    }

    private fun logoutRoom() {
        sendCustomMsg(createMessage(Message.command_leave_room, "离开房间"))

        imManager?.logout(null)

    }

    private fun exitRoom() {
        logoutRoom()
        //销毁 trtc 实例
        mTRTCCloud?.let {
            it.stopLocalAudio()
            //            mTRTCCloud.setListener(null);
            it.exitRoom()
        }

        mTRTCCloud = null
        imManager = null
        TRTCCloud.destroySharedInstance()
    }

    private fun handleUser() {
        val randomSuffix = (Date().time % (Date().time / 1000)).toString() + ""
        if (uid == 0) {
            userID = "userid-$randomSuffix"
            userName = "username-$randomSuffix"
        } else {
            userID = uid.toString() + ""
            val userInfo = instance.getUserInfo()
            Log.e(TAG, "loginRoom: $userInfo")
            if (null != userInfo && !TextUtils.isEmpty(userInfo.nick_name)) {
                userName = userInfo.nick_name
                face = userInfo.face
            } else {
                userName = "username-$randomSuffix"
            }
        }
        //获取签名
//        liveLookPresenter.getUserSeg(userID);
    }

    //1.没有登录的用户不能加入房间
    //2.登录的用户能加入房间
    private fun joinImRoom(liveInfo: LiveInfo) {
        roomId = liveInfo.roomId
        if (imManager == null) {
            imManager = IMManager.getInstance()
        }
//        Log.e(TAG, "joinImRoom: ")
        //用户登录
        imManager?.login(userID, GenerateTestUserSig.genTestUserSig(userID), object : V2TIMCallback {
            override fun onError(code: Int, msg: String) {
                Log.e(TAG, "login room error: code:  $code  msg:  $msg")
            }

            override fun onSuccess() {
                Log.e(TAG, "login room success: ")
                joinRoom()
                loginRoom()
            }
        })
    }

    //进入房间收听语音直播
    private fun loginRoom() {
        val trtcParams = TRTCParams()
        trtcParams.userId = userID
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId)
        trtcParams.roomId = roomId
        trtcParams.role = TRTCCloudDef.TRTCRoleAudience

        //进入房间
        mTRTCCloud?.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VOICE_CHATROOM)
    }

    private fun showCloseFragment() {
        val closeLiveFragment = CloseLiveFragment()
        closeLiveFragment.show(supportFragmentManager, "")
        closeLiveFragment.setOnCloseListener(object : CloseLiveFragment.OnCloseListener {
            override fun onMinimize() {
                PreferenceUtil.getInstance().setBooleanValue(Constant.is_exit_live, false)
                //                moveTaskToBack(true);
//                engine.setEventHandler(null);
                logoutRoom()
                finish()
            }

            override fun onClose() {
//                PreferenceUtil.getInstance().setBooleanValue(Constant.is_exit_live, true);
                exitRoom()
                finish()
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventLoginState) {
        if (event.state == EventLoginState.STATE_LOGINED) {
            uid = instance.getUid()
            //            loginRoom();
            handleUser()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        showCloseFragment()
    }

    val wx: String
        get() {
            var wx = ""
            weixins?.let {
                wx = it[random.nextInt(it.size)]
            }
            return wx
        }

    private fun joinRoom() {
        if (imManager == null) {
            imManager = IMManager.getInstance()
        }

//        Log.e(TAG, "joinRoom: " + roomId);
        imManager?.joinGroupRoom(roomId.toString() + "", object : V2TIMCallback {
            override fun onError(code: Int, msg: String) {
                Log.e(TAG, "join room failure: code： $code  msg: $msg")
                //                    joinRoom();
            }

            override fun onSuccess() {
                imManager?.setReceiveMagListener(simpleMsgListener)
                imManager?.setGroupDismissListener(groupListener)
            }
        })
    }

    private val groupListener: V2TIMGroupListener = object : V2TIMGroupListener() {
        override fun onGroupDismissed(groupID: String, opUser: V2TIMGroupMemberInfo) {
            super.onGroupDismissed(groupID, opUser)
            if (!isDestroyed) {
                val liveEndFragment = LiveEndFragment()
                liveEndFragment.show(supportFragmentManager, "")
            }
        }
    }
    private val simpleMsgListener: V2TIMSimpleMsgListener = object : V2TIMSimpleMsgListener() {
        override fun onRecvGroupTextMessage(msgID: String, groupID: String, sender: V2TIMGroupMemberInfo, text: String) {
            super.onRecvGroupTextMessage(msgID, groupID, sender, text)
            val chatItems: MutableList<ChatItem> = ArrayList()
            val chatItem = ChatItem(sender.nickName, text, ChatItem.TYPE_OTHER)
            chatItem.face = face
            chatItems.add(chatItem)
            chatAdapter.addData(chatItems)
        }

        override fun onRecvGroupCustomMessage(msgID: String, groupID: String, sender: V2TIMGroupMemberInfo, customData: ByteArray) {
            super.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
            val customMsg = String(customData, StandardCharsets.UTF_8)
            Log.e(TAG, "onRecvGroupCustomMessage:   msg  $customMsg")
            val message = JSON.parseObject(customMsg, Message::class.java)
            val chatItems: MutableList<ChatItem> = ArrayList()
            var chatItem: ChatItem? = null
            when (message.cmdId) {
                Message.command_normal -> {
                    val strs = message.content.split("-").toTypedArray()
                    if (strs[1].startsWith("#") && TextUtils.equals(sender.userID, tutorId)) {
                        chatItem = ChatItem(strs[0], ChatItem.TYPE_NOTIFICATION)
                        chatItem.face = strs[2]
                    } else {
                        chatItem = ChatItem(strs[0], strs[1], ChatItem.TYPE_OTHER)
                        chatItem.face = strs[2]
                    }
                }
                Message.command_tutor -> {
                    val strs = message.content.split("-").toTypedArray()
                    chatItem = ChatItem(strs[0], ChatItem.TYPE_NOTIFICATION)
                    chatItem.face = strs[1]
                }
                Message.command_praise -> {
                    //点赞
                    //点赞消息
                    heart_layout.addFavor()
                    praiseCount++
                    setPraise()
                }
                Message.command_come_room -> {
                    //进入房间
                    onlineCount++
                    online_count.text = String.format(getString(R.string.online_count), onlineCount)
                    chatItem = ChatItem(message.content, ChatItem.TYPE_COME_CHAT)
                }
                Message.command_get_wx -> chatItem = ChatItem(message.content, ChatItem.TYPE_GET_WX)
                Message.command_leave_room -> {
                    onlineCount--
                    online_count.text = String.format(getString(R.string.online_count), onlineCount)
                }
            }
            if (null != chatItem) {
                chatItems.add(chatItem)
            }
            chatAdapter.addData(chatItems)
            recyclerView_chat?.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun sendCustomMsg(msg: String) {
        if (imManager == null) {
            imManager = IMManager.getInstance()
        }
        if (!TextUtils.isEmpty(msg)) {
            imManager?.sendCustomMessage(msg, "$roomId", V2TIMMessage.V2TIM_PRIORITY_HIGH, object : V2TIMSendCallback<V2TIMMessage?> {
                override fun onProgress(i: Int) {}
                override fun onError(code: Int, msg: String) {
                    Log.e(TAG, "send customMsg error:  code: $code  msg:  $msg")
                }

                override fun onSuccess(v2TIMMessage: V2TIMMessage?) {
                    Log.e(TAG, "send customMsg success: ")
                    val message = JSON.parseObject(msg, Message::class.java)
                    if (message.cmdId == Message.command_normal) {
                        val strs = message.content.split("-").toTypedArray()
                        val chatItem = ChatItem(userName, strs[1], ChatItem.TYPE_ME)
                        chatItem.face = face
                        records.add(chatItem)
                        live_send_message.setText("")
                    } else if (message.cmdId == Message.command_get_wx) {
                        records.add(ChatItem(userName, msg, ChatItem.TYPE_GET_WX))
                    }
                    chatAdapter.setNewData(records)
                    recyclerView_chat.scrollToPosition(chatAdapter.itemCount - 1)
                }
            })
        }
    }

    private fun convertTime(time: Long): String {
        val sd = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sd.format(Date(time * 1000))
    }

    override fun showLiveInfo(data: LiveInfo?) {
        weixins = data?.weixin
        mLiveInfo = data
        data?.let {
            starLook(data)
        }

    }

    override fun showUserSeg(usersig: String) {
        userSig = usersig
    } //    @Override


    override fun onDestroy() {
        super.onDestroy()
        setLiveState(false)
    }

    companion object {
        private const val TAG = "LiveLookActivity"
        fun startActivity(context: Context, roomId: Int, starttime: Long, endtime: Long) {
            val intent = Intent(context, LiveLookActivityNew::class.java)
            intent.putExtra("roomId", roomId)
            intent.putExtra("starttime", starttime)
            intent.putExtra("endtime", endtime)
            context.startActivity(intent)
        }
    }
}