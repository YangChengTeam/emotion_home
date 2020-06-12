package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import androidx.navigation.findNavController
import com.yc.emotion.home.R

import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import kotlinx.android.synthetic.main.activity_login_register.*

/**
 *
 * Created by suns  on 2020/4/27 14:24.
 */
class LoginRegisterActivity : BaseSameActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login_register
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun offerActivityTitle(): String? {
        return ""
    }

    override fun hindActivityBar() = true

    override fun hindActivityTitle() = true

    override fun isSupportSwipeBack() = false

    override fun initViews() {
        initListener()
    }

    private fun initListener() {

        iv_login_back.setOnClickListener { onBackPressed() }

    }
}