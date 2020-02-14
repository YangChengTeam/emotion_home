package com.yc.emotion.home.message.ui.activity

import android.os.Bundle
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.message.presenter.MessagePresenter
import com.yc.emotion.home.message.view.MessageView
import com.yc.emotion.home.model.bean.MessageInfo
import kotlinx.android.synthetic.main.activity_notification_detail.*
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/11/4 16:34.
 */
class NotificationDetailActivity : BaseSameActivity(), MessageView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_notification_detail
    }


    override fun initViews() {
        val id = intent?.getIntExtra("id", 0)

        mPresenter = MessagePresenter(this, this)

        (mPresenter as? MessagePresenter)?.getNotificationDetail("$id")

    }

    override fun offerActivityTitle(): String {
        return "系统通知"
    }



    private fun initWebview(messageInfo: MessageInfo?) {
        commonWebView.loadDataWithBaseURL(null, messageInfo?.content, null, "utf-8", null)

    }

    override fun showNotificationDetail(data: MessageInfo?) {
        initWebview(data)
    }
}