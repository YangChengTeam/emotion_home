package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import kotlinx.android.synthetic.main.activity_privacy_statement.*

class PrivacyStatementActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_privacy_statement
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        tv_privacy_statement.text = String.format(getString(R.string.privacy_statement), getString(R.string.app_name))

    }

    override fun offerActivityTitle(): String {
        return getString(R.string.app_name) + "隐私声明"
    }
}
