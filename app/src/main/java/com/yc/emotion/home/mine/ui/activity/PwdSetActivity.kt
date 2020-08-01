package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_set_pwd.*

/**
 *
 * Created by suns  on 2020/4/29 08:25.
 */
class PwdSetActivity : BaseSameActivity() {
    private var isSetPwd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userInfo = UserInfoHelper.instance.getUserInfo()
        userInfo?.let {
            val pwd = userInfo.pwd
            isSetPwd = !TextUtils.isEmpty(pwd)
        }
        setContentView(getLayoutId())
        initViews()
    }

    override fun offerActivityTitle(): String? {
        return if (isSetPwd) "修改密码" else "设置密码"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_set_pwd
    }

    override fun isSupportSwipeBack() = false

    override fun initViews() {

//            mPresenter= UserInfoPresenter(this,this)
        offerActivitySubTitleView().text = "完成"


        tv_pwd_title.text = if (isSetPwd) "原密码" else "设置密码"
        et_pwd.hint = if (isSetPwd) "请输入原密码" else "请输入密码"
        new_pwd_group.visibility = if (isSetPwd) View.VISIBLE else View.GONE

        offerActivitySubTitleView().setOnClickListener {
            val pwd = et_pwd.text.toString().trim()
            val newPwd = et_new_pwd.toString().trim()
            if (isSetPwd) (mPresenter as UserInfoPresenter).modifyPwd(pwd, newPwd)
            else (mPresenter as UserInfoPresenter).setPwd(pwd)
        }
    }

    override fun setPwdSuccess() {
        super.setPwdSuccess()
        finish()
    }
}