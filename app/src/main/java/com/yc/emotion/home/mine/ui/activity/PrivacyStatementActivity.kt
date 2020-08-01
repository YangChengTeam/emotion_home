package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.utils.UIUtils
import kotlinx.android.synthetic.main.activity_privacy_statement.*

class PrivacyStatementActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_privacy_statement
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        tv_privacy_statement.text = String.format(getString(R.string.privacy_statement), UIUtils.getAppName(this))

    }

    override fun offerActivityTitle(): String {
        return UIUtils.getAppName(this) + "隐私声明"
    }
}
