package com.yc.emotion.home.pay.ui.activity

import android.os.Bundle

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.utils.UIUtils


class ProtocolActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_protocol
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    override fun offerActivityTitle(): String {
        return UIUtils.getAppName(this) + "用户协议"
    }
}
