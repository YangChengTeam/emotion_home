package com.yc.emotion.home.message.ui.activity


import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.message.adapter.NotificationAdapter
import com.yc.emotion.home.message.presenter.MessagePresenter
import com.yc.emotion.home.message.view.MessageView
import com.yc.emotion.home.model.bean.MessageInfo
import kotlinx.android.synthetic.main.fragment_collect_view.*

/**
 *
 * Created by suns  on 2019/11/4 18:07.
 */
class NotificationListActivity : BaseSameActivity(), MessageView {


    private var notificationAdapter: NotificationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }

    override fun initViews() {


        mPresenter = MessagePresenter(this, this)


        fragment_collect_love_healing_rv.layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationAdapter(null)
        fragment_collect_love_healing_rv.adapter = notificationAdapter
        initData()
        initListener()
    }

    private fun initData() {

        (mPresenter as? MessagePresenter)?.getMessageCache()

    }

    private fun initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener { getData() }

        notificationAdapter?.setOnItemClickListener { adapter, view, position ->
            val messageInfo = notificationAdapter?.getItem(position)
            messageInfo?.let {
                val intent = Intent(this, NotificationDetailActivity::class.java)
                intent.putExtra("id", messageInfo.id)
                startActivity(intent)
            }
        }
    }


    private fun getData() {

        (mPresenter as? MessagePresenter)?.getMessageInfoList()

    }

    override fun showMessageInfos(messageInfos: List<MessageInfo>) {
        createNewData(messageInfos)
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    private fun createNewData(data: List<MessageInfo>?) {
        notificationAdapter?.setNewData(data)
    }


    override fun offerActivityTitle(): String {
        return "系统通知"
    }
}