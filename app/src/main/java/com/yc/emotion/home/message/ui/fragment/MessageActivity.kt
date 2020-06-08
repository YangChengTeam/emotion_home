package com.yc.emotion.home.message.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.factory.MainFragmentFactory
import com.yc.emotion.home.message.adapter.MessageMainAdapter
import com.yc.emotion.home.message.presenter.MessagePresenter
import com.yc.emotion.home.message.ui.activity.NotificationListActivity
import com.yc.emotion.home.message.ui.activity.ServiceChatActivity
import com.yc.emotion.home.message.view.MessageView
import com.yc.emotion.home.model.bean.MessageInfo
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_main_message.*
import java.util.*

/**
 *
 * Created by suns  on 2019/9/28 17:36.
 */
class MessageActivity : BaseSameActivity(), MessageView {


    private var messageAdapter: MessageMainAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_main_message
    }

    override fun initViews() {

        mPresenter = MessagePresenter(this, this)
//        mLoadDialog = LoadDialog(mMainActivity)
        recyclerView_message.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageMainAdapter(null)

        recyclerView_message.adapter = messageAdapter

        initData()
        initListener()


    }


    private fun initData() {

        (mPresenter as? MessagePresenter)?.getMessageCache()

    }

    private fun initListener() {


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))


        swipeRefreshLayout.setOnRefreshListener { getData() }


        messageAdapter?.setOnItemClickListener { _, view, position ->
            val messageInfo = messageAdapter?.getItem(position)
            messageInfo?.let {
                when (messageInfo.itemType) {
                    MessageInfo.TYPE_MESSAGE -> {
                        if (!UserInfoHelper.instance.goToLogin(this)) {
                            MobclickAgent.onEvent(this, "official_customer_service_id", "消息页官方客服")
                            startActivity(Intent(this, ServiceChatActivity::class.java))
                        } else {
                        }
                    }
                    MessageInfo.TYPE_NOTIFICATION -> {
                        startActivity(Intent(this, NotificationListActivity::class.java))
                    }
                    MessageInfo.TYPE_COMMUNITY -> {

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("pos", MainFragmentFactory.MAIN_FRAGMENT_2)

                        startActivity(intent)


                    }
                    else -> {
                    }
                }
            }
        }

    }

    private fun getData() {
        (mPresenter as? MessagePresenter)?.getMessageInfoList()

    }


    private fun createNewData(data: List<MessageInfo>) {
        val messageInfos = ArrayList<MessageInfo>()

        val messageInfo = MessageInfo(MessageInfo.TYPE_MESSAGE, R.mipmap.message_icon_service, "官方客服", "有任何问题可以随时联系我哦")
        messageInfos.add(messageInfo)


        data.let {

            if (data.isNotEmpty()) {
                val notification = data[0]

                notification.setType(MessageInfo.TYPE_NOTIFICATION)
                notification.img = R.mipmap.message_icon_system
                notification.name = "系统通知"
                messageInfos.add(notification)

            } else {
                val messageInfo1 = MessageInfo(MessageInfo.TYPE_NOTIFICATION, R.mipmap.message_icon_system, "系统通知", "暂时没有系统通知")
                messageInfos.add(messageInfo1)
            }
        }
        val messageInfo3 = MessageInfo(MessageInfo.TYPE_COMMUNITY, R.mipmap.message_icon_community, "社区消息", "快去社区发内容吧")
        messageInfos.add(messageInfo3)
        messageAdapter?.setNewData(messageInfos)

    }

    override fun showMessageInfos(messageInfos: List<MessageInfo>) {
        createNewData(messageInfos)
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun offerActivityTitle(): String {
        return "消息"
    }

}

