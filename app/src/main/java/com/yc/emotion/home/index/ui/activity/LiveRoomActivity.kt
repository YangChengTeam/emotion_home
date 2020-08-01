package com.yc.emotion.home.index.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.bumptech.glide.Glide
import com.tencent.imsdk.v2.*
import com.tencent.liteav.TXLiteAVCode
import com.tencent.trtc.TRTCCloud
import com.tencent.trtc.TRTCCloudDef
import com.tencent.trtc.TRTCCloudDef.TRTCParams
import com.tencent.trtc.TRTCCloudListener
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.im.IMManager
import com.yc.emotion.home.index.adapter.ChatAdapter
import com.yc.emotion.home.index.domain.bean.ChatItem
import com.yc.emotion.home.index.domain.bean.Message
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.mine.presenter.LivePresenter
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.mine.view.LiveView
import com.yc.emotion.home.utils.GenerateTestUserSig
import com.yc.emotion.home.utils.SoftKeyBoardUtils
import com.yc.emotion.home.utils.SoftKeyBoardUtils.OnSoftKeyBoardChangeListener
import com.yc.emotion.home.utils.UIUtils
import kotlinx.android.synthetic.main.live_room_layout.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by suns  on 2020/5/27 15:27.
 */
class LiveRoomActivity : BaseActivity(), LiveView, V2TIMCallback {

    private val random = Random()

    private val viewRect = IntArray(2)
    private val rect = Rect()


    //    消息列表
    private val records = ArrayList<ChatItem>()

    private var chatAdapter: ChatAdapter? = null

    private var roomId = 0
    private var userID: String? = null
    private var userName: String? = null


    private var praiseCount = 200 //点赞人数

    private var onlineCount = 368 //在线人数基数
    private var liveUser: LiveInfo? = null

    private var mTRTCCloud: TRTCCloud? = null

    private var imManager: IMManager? = null

    private var livePresenter: LivePresenter? = null

    private var topHeight = 0
    private var weixins //所有的微信
            : List<String>? = null
    private var endTime //直播结束时间
            : Long = 0
    private var startTime //直播开始时间
            : Long = 0
    private var face: String? = null


    override fun getLayoutId(): Int {
        return R.layout.live_room_layout
    }

    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        requestPermission()
        initView()
    }

    private fun requestPermission() {
        val permissionNeeded = arrayOf("android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionNeeded, 101)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置fitsSystemWindows属性后添加
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutId())
        initViews()
    }

    private fun initView() {
        liveUser = intent.getParcelableExtra("user")
        livePresenter = LivePresenter(this, this)


        iv_intro.visibility = View.GONE
        tv_wx.text = "发送微信"
        val layoutManager = LinearLayoutManager(this)

        recyclerView_chat.layoutManager = layoutManager
        chatAdapter = ChatAdapter(records)
        recyclerView_chat.adapter = chatAdapter
        loading_connect_listen.visibility = View.VISIBLE


        // 创建 trtcCloud 实例
        mTRTCCloud = TRTCCloud.sharedInstance(applicationContext)
        //创建im初始化实例
        imManager = IMManager.getInstance()
        userID = liveUser?.id
        userName = liveUser?.nickname
        livePresenter?.createRoom(userID)
        face = liveUser?.face
        initListener()
        tv_chat.postDelayed({
            tv_chat.getLocationOnScreen(viewRect)

            val width = iv_screen.width
            topHeight = top_container.height
            val tvWidth = tv_chat.width
            val tvHeight = tv_chat.height
            rect.left = viewRect[0]
            rect.top = viewRect[1]
            rect.right = viewRect[0] + tvWidth - width
            rect.bottom = viewRect[1] + tvHeight

//            Log.e(TAG, "${rect.top}--${rect.bottom}")

        }, 1000)

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
//            Log.e(TAG, "x=$x;y=$y");
//            Log.e(TAG, "onTouchEvent: " + rect.left + "---" + rect.right + "--" + rect.top + "--" + rect.bottom);
            if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                user_input_layout.visibility = View.VISIBLE
                SoftKeyBoardUtils.showSoftBoard(live_send_message)
            } else {
                SoftKeyBoardUtils.hideSoftBoard(live_send_message)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun setPraise() {
        praise_count.text = String.format(getString(R.string.praise_count), praiseCount)
        tv_praise_bottom.text = praiseCount.toString()
    }

    private fun initListener() {
        ll_raise.setOnClickListener { v: View? ->
            heart_layout.addFavor()
            //            sendMsg(Message.command_praise, "点赞");
            sendCustomMsg(createMessage(Message.command_praise, "点赞"))
            praiseCount++
            setPraise()
        }
        ll_wx.setOnClickListener { v: View? -> sendCustomMsg(createMessage(Message.command_tutor, "$userName-$face")) }
        SoftKeyBoardUtils.setListener(this, object : OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {}
            override fun keyBoardHide(height: Int) {
                user_input_layout.visibility = View.GONE
            }
        })
        send_txt_msg_submit_container.setOnClickListener { v: View? ->
            val content = live_send_message.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this@LiveRoomActivity, "不能发送空消息", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            sendCustomMsg(createMessage(Message.command_normal, "$userName-$content-$face"))
        }
        iv_wheat.setOnClickListener { v: View? ->
            // 开始推流
            mTRTCCloud?.startLocalAudio()
        }
        live_send_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val result = s.toString()
                if (TextUtils.isEmpty(result)) {
                    send_txt_msg_submit.setBackgroundColor(ContextCompat.getColor(this@LiveRoomActivity, R.color.color_f8f8f8))
                    send_txt_msg_submit.setTextColor(ContextCompat.getColor(this@LiveRoomActivity, R.color.color_bdbcbc))
                } else {
                    send_txt_msg_submit.setBackgroundColor(ContextCompat.getColor(this@LiveRoomActivity, R.color.main_theme_color))
                    send_txt_msg_submit.setTextColor(ContextCompat.getColor(this@LiveRoomActivity, R.color.white))
                }
            }
        })
        close_live.setOnClickListener { v: View? -> showExitPublish() }
        iv_screen.setOnClickListener { v: View? -> if (rl_top_ppt.visibility == View.GONE) rl_top_ppt.visibility = View.VISIBLE }
        tv_pack.setOnClickListener { v: View? -> setPackState() }
        iv_operation.setOnClickListener {
            isOut = !isOut
            startAnimation(isOut)
        }

        ll_operation.setOnClickListener {
            isMuted = !isMuted
            imManager?.muteGroupMember("$roomId", isMuted, object : V2TIMCallback {
                override fun onSuccess() {
//                    Log.e(TAG, "onSuccess")
                    sendCustomMsg(createMessage(Message.command_muted_msg, "$isMuted"))
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
//                    Log.e(TAG, "onError")
                }
            })
            if (isMuted) {
                tv_muted.text = "解除禁言"
            } else {
                tv_muted.text = "全部禁言"
            }
        }
    }

    private var isOut = false
    private var isMuted = false//是否禁言


    private fun startAnimation(isOut: Boolean) {


        val animator: ObjectAnimator?
        val alphaAnimator: ObjectAnimator?
        if (isOut) {
            animator = ObjectAnimator.ofFloat(ll_operation, "translationX", 0f, -170f)
            alphaAnimator = ObjectAnimator.ofFloat(ll_operation, "alpha", 0f, 0.5f, 1.0f)
        } else {

            animator = ObjectAnimator.ofFloat(ll_operation, "translationX", -170f, 0f)
            alphaAnimator = ObjectAnimator.ofFloat(ll_operation, "alpha", 1.0f, 0.5f, 0f)

        }
        //动画基本属性
        animator.duration = 500
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animator, alphaAnimator)

        animatorSet.start()

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

    private fun startLive() {
        mTRTCCloud?.setListener(object : TRTCCloudListener() {
            override fun onError(errCode: Int, errMsg: String, bundle: Bundle) {
                super.onError(errCode, errMsg, bundle)
                Log.e(TAG, "onError: $errCode--$errMsg")
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    exitRoom()
                }
            }

            override fun onEnterRoom(result: Long) {
                super.onEnterRoom(result)
                Log.e(TAG, "onEnterRoom: $result")
                loading_connect_listen.visibility = View.GONE
                setLiveUserInfo()
                //                if (result > 0) {
//                    Toast.makeText(LiveRoomActivity.this, "进房成功，总计耗时[(" + result + ")]ms", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LiveRoomActivity.this, "进房失败，错误码[(" + result + ")]", Toast.LENGTH_SHORT).show();
//                }
                online_count.text = String.format(getString(R.string.online_count), onlineCount)
            }

            //reason	离开房间原因，0：主动调用 exitRoom 退房；1：被服务器踢出当前房间；2：当前房间整个被解散。
            override fun onExitRoom(reason: Int) {
                super.onExitRoom(reason)
                exitRoom()
            }


            override fun onSendFirstLocalAudioFrame() {
                super.onSendFirstLocalAudioFrame()
                Log.e(TAG, "onSendFirstLocalAudioFrame: ")
                setLiveState(true)
                setPraise()
                iv_wheat.visibility = View.GONE
                mHandler?.postDelayed(runnable, 0)
                mHandler?.postDelayed(messageRunnable, 1000 * 60)
            }
        })
    }

    private fun exitRoom() {
        livePresenter?.liveEnd("$roomId")
        livePresenter?.dismissGroup("$roomId")
        mHandler?.removeCallbacks(runnable)
        mHandler?.removeCallbacks(messageRunnable)
        //销毁 trtc 实例
        mTRTCCloud?.let {
            it.stopLocalAudio()
            it.setListener(null)
            it.exitRoom()
        }
        imManager?.let {
            it.dismissGroup("$roomId", null)
            it.logout(null)
        }
        mTRTCCloud = null
        TRTCCloud.destroySharedInstance()
        finish()
    }

    private fun setLiveUserInfo() {
        if (!isDestroyed) {
            liveUser?.let {
                Glide.with(this).load(it.face).circleCrop().error(R.drawable.default_avatar_72).into(mentor_avatar)
                mentor_name.text = it.nickname
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
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "所需权限没有获取到", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createMessage(cmdId: Int, content: String): String {
        val message = Message(content, cmdId)
        return JSON.toJSONString(message)
    }

    val wx: String
        get() {
            var wx = ""
            weixins?.let {
                wx = it[random.nextInt(it.size)]
            }
            return wx
        }

    override fun onDestroy() {
        super.onDestroy()
        exitRoom()
    }

    override fun showCreateRoomSuccess(data: LiveInfo) {
        roomId = data.roomId
        weixins = data.weixin
        startTime = data.start_time
        endTime = data.end_time

//        Log.e(TAG, "showCreateRoomSuccess: " + getWx());
        if (!isDestroyed) Glide.with(this).load(data.ppt_img).into(iv_top_ppt)
        if (data.sdkappid != 0) GenerateTestUserSig.SDKAPPID = data.sdkappid
        if (!TextUtils.isEmpty(data.user_id)) {
            userID = data.user_id
        }
        val trtcParams = TRTCParams()
        trtcParams.userId = userID
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID
        trtcParams.userSig = data.usersig
        trtcParams.roomId = roomId
        trtcParams.role = TRTCCloudDef.TRTCRoleAnchor


        //进入房间
        mTRTCCloud?.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VOICE_CHATROOM)
        imManager?.login(userID, data.usersig, this)
        startLive()
    }

    override fun showLoginSuccess(data: LiveInfo) {}
    override fun onError(code: Int, msg: String) {
        Log.e(TAG, "login room error:  code: $code  msg: $msg")
    }

    override fun onSuccess() {
        createRoom()
    }

    private fun createRoom() {

//        Log.e(TAG, "createRoom: $roomId")
        imManager?.createGroupRoom("$roomId", userName + "主播群", object : V2TIMSendCallback<String?> {
            override fun onProgress(i: Int) {}
            override fun onError(code: Int, msg: String) {
                Log.e(TAG, "create room failure: code： $code  msg: $msg")
                //                    createRoom();
            }

            override fun onSuccess(msg: String?) {
                imManager?.setReceiveMagListener(simpleMsgListener)
            }
        })

    }

    private val simpleMsgListener: V2TIMSimpleMsgListener = object : V2TIMSimpleMsgListener() {
        override fun onRecvGroupTextMessage(msgID: String, groupID: String, sender: V2TIMGroupMemberInfo, text: String) {
            super.onRecvGroupTextMessage(msgID, groupID, sender, text)
            val chatItems: MutableList<ChatItem> = ArrayList()
            val chatItem = ChatItem(sender.nickName, text, ChatItem.TYPE_OTHER)
            chatItems.add(chatItem)
            chatAdapter?.addData(chatItems)
        }

        override fun onRecvGroupCustomMessage(msgID: String, groupID: String, sender: V2TIMGroupMemberInfo, customData: ByteArray) {
            super.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
            val customMsg = String(customData, StandardCharsets.UTF_8)
            Log.e(TAG, "onRecvGroupCustomMessage: " + sender.nickName + "  msg  " + customMsg)
            val message = JSON.parseObject(customMsg, Message::class.java)
            val chatItems: MutableList<ChatItem> = ArrayList()
            var chatItem: ChatItem? = null
            when (message.cmdId) {
                Message.command_normal -> {
                    //普通消息
                    val sts = message.content.split("-").toTypedArray()
                    chatItem = ChatItem(sts[0], sts[1], ChatItem.TYPE_OTHER)
                    chatItem.face = sts[2]
                }
                Message.command_tutor -> chatItem = ChatItem(sender.nickName, message.content, ChatItem.TYPE_NOTIFICATION)
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
                    sendCustomMsg(createMessage(Message.command_come_out, "$onlineCount-$praiseCount"))

                }
                Message.command_get_wx -> chatItem = ChatItem(message.content, ChatItem.TYPE_GET_WX)
                Message.command_leave_room -> {
                    onlineCount--
                    online_count.text = String.format(getString(R.string.online_count), onlineCount)
                    sendCustomMsg(createMessage(Message.command_come_out, "$onlineCount"))
                }

            }
            if (null != chatItem) {
                chatItems.add(chatItem)
            }
            chatAdapter?.let {
                it.addData(chatItems)
                recyclerView_chat.scrollToPosition(it.itemCount - 1)
            }

        }
    }

    private fun sendCustomMsg(msg: String) {

        if (!TextUtils.isEmpty(msg)) {
            imManager?.sendCustomMessage(msg, "$roomId", V2TIMMessage.V2TIM_PRIORITY_HIGH, object : V2TIMSendCallback<V2TIMMessage?> {
                override fun onProgress(i: Int) {}
                override fun onError(code: Int, msg: String) {}
                override fun onSuccess(v2TIMMessage: V2TIMMessage?) {
                    val message = JSON.parseObject(msg, Message::class.java)
                    when (message.cmdId) {
                        Message.command_normal -> {
                            val content = message.content
                            val strs = content.split("-").toTypedArray()
                            if (strs[1].startsWith("#")) {
                                val chatItem = ChatItem(userName, strs[1], ChatItem.TYPE_NOTIFICATION)
                                chatItem.face = face
                                records.add(chatItem)
                            } else {
                                val chatItem = ChatItem(userName, strs[1], ChatItem.TYPE_ME)
                                chatItem.face = face
                                records.add(chatItem)
                            }
                            live_send_message.setText("")
                        }
                        Message.command_get_wx -> {
                            records.add(ChatItem(userName, msg, ChatItem.TYPE_GET_WX))
                        }
                        Message.command_tutor -> {
                            val chatItem = ChatItem(userName, ChatItem.TYPE_NOTIFICATION)
                            chatItem.face = face
                            records.add(chatItem)
                        }
                        Message.command_official_msg -> {
                            val chatItem = ChatItem(UIUtils.getAppName(this@LiveRoomActivity), message.content, ChatItem.TYPE_TOUR_MSG)
                            records.add(chatItem)
                        }
                    }
                    chatAdapter?.let {
                        it.setNewData(records)
                        recyclerView_chat.scrollToPosition(it.itemCount - 1)
                    }

                }
            })
        }

    }

    private var dialog: AlertDialog? = null
    private var isClose = false

    //定时关闭直播间
    private val runnable: Runnable = object : Runnable {
        override fun run() {

            if (System.currentTimeMillis() >= endTime * 1000) {
                if (dialog == null) {
                    dialog = AlertDialog.Builder(this@LiveRoomActivity).setTitle("提示")
                            .setMessage("直播时间已到，请下麦").setPositiveButton("确定") { dialog1: DialogInterface, which: Int ->
                                dialog1.dismiss()
                                exitRoom()
                                isClose = true
                            }.create()
                }
                if (!isDestroyed) {
                    dialog?.let {
                        if (!it.isShowing) {
                            it.show()
                        }
                    }
                }

                if (!isClose) {
                    mHandler?.postDelayed({
                        dialog?.dismiss()
                        exitRoom()
                    }, 5000)
                }
            }
            mHandler?.postDelayed(this, 60 * 1000 * 5.toLong())
        }
    }

    private fun showExitPublish() {
        val exitPublishFragment = ExitPublishFragment.newInstance("是否退出直播？")
        exitPublishFragment.show(supportFragmentManager, "")
        exitPublishFragment.setOnConfirmListener(object : ExitPublishFragment.OnConfirmListener {
            override fun onConfirm() {
                exitRoom()
            }
        })
    }

    override fun onBackPressed() {
        showExitPublish()
    }

    //发送官方消息
    fun sendOfficialMsg() {
        sendCustomMsg(createMessage(Message.command_official_msg,
                "官方24小时巡逻，平台不允许讨论不尊重女性、色情、政治等话题，请大家共同监督，如有发现违规现象，请举报"))
    }

    private val messageRunnable: Runnable = object : Runnable {
        override fun run() {
            sendOfficialMsg()
            mHandler?.postDelayed(this, 1000 * 60 * 5)
        }

    }

    companion object {
        private const val TAG = "LiveRoomActivity"
        fun startActivity(context: Context?, liveUser: LiveInfo?) {
            val intent = Intent(context, LiveRoomActivity::class.java)
            intent.putExtra("user", liveUser)
            context?.startActivity(intent)
        }
    }
}