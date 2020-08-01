package com.yc.emotion.home.index.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.utils.RegexUtils
import com.yc.emotion.home.utils.StatusBarUtil
import com.yc.emotion.home.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_consult_appoint.*

/**
 *
 * Created by suns  on 2019/9/18 15:33.
 */
class ConsultAppointActivity : BaseSameActivity(), UserInfoView {

    private var code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_consult_appoint
    }


    override fun initViews() {
        mPresenter = UserInfoPresenter(this, this)
        initListener()
    }


    fun initListener() {

        et_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val phone = s.toString().trim()
                iv_phone_close.visibility = if (TextUtils.isEmpty(phone)) View.GONE else View.VISIBLE

                if (!TextUtils.isEmpty(phone)) {
                    if (phone.length == 11 && RegexUtils.isMobileSimple(phone)) {
                        ll_verify_code.visibility = View.VISIBLE
                        tv_get_code.setTextColor(ContextCompat.getColor(this@ConsultAppointActivity, R.color.red_crimson))
                        tv_get_code.isClickable = true
                    } else {
                        resetVerifyCodeState()
                    }
                } else {
                    resetVerifyCodeState()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        et_wx.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val wx = s.toString().trim()
                iv_wx_close.visibility = if (TextUtils.isEmpty(wx)) View.GONE else View.VISIBLE
            }
        })

        iv_phone_close.setOnClickListener {
            et_phone.setText("")
            iv_phone_close.visibility = View.GONE
        }

        iv_wx_close.setOnClickListener {
            et_wx.setText("")
            iv_wx_close.visibility = View.GONE
        }

        tv_get_code.setOnClickListener {

            val phone = et_phone.text.toString().trim()
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showCenterToast("手机号不能为空")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showCenterToast("手机号格式不正确")
                return@setOnClickListener
            }

            //todo 获取验证码
            sendCode(phone)

        }
        tv_advise.setOnClickListener {
            //todo 立即预约
            val phone = et_phone.text.toString().trim()
            val wx = et_wx.text.toString().trim()
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showCenterToast("手机号不能为空")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showCenterToast("手机号格式不正确")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showCenterToast("验证码不能为空")
                return@setOnClickListener
            }
            consult(phone, wx, code)
            MobclickAgent.onEvent(this, "free_consultation_id", "导师页面免费咨询")

        }
        verificationCodeInput.setOnCompleteListener { content -> code = content }

    }

    fun resetVerifyCodeState() {
        tv_get_code.setTextColor(ContextCompat.getColor(this@ConsultAppointActivity, R.color.gray_999))
        tv_get_code.isClickable = false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val layoutParams = nestedScrollView.layoutParams as ViewGroup.MarginLayoutParams

        var bottom = 0


        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this)
        }

        layoutParams.bottomMargin = bottom
        nestedScrollView.layoutParams = layoutParams


    }

    private fun sendCode(phone: String) {
        (mPresenter as? UserInfoPresenter)?.sendCode(phone)

    }


    private fun consult(phone: String, wx: String?, code: String) {

        (mPresenter as? UserInfoPresenter)?.consultAppoint(phone, wx, code)

    }

    override fun sendCodeSuccess() {
        showGetCodeDisplay(tv_get_code)
    }


    override fun offerActivityTitle(): String {
        return "情感咨询"
    }
}