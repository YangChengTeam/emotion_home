package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity

class PrivacyStatementActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_privacy_statement
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    override fun offerActivityTitle(): String {
        return getString(R.string.app_name) + "隐私声明"
    }
}
