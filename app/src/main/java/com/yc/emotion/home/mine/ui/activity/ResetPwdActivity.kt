package com.yc.emotion.home.mine.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.RegexUtils
import com.yc.emotion.home.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_reset_pwd.*

/**
 *
 * Created by suns  on 2019/10/21 08:46.
 */
class ResetPwdActivity : BaseSameActivity(), UserInfoView {


    private var isShowPwd = false
    private var mobile by Preference(ConstantKey.PhHONE, "")

    private var code: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_reset_pwd
    }

    override fun initViews() {
        mPresenter = UserInfoPresenter(this, this)
        initView()
    }


    private fun initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            et_input_phone.letterSpacing = 0.2f
            et_input_pwd.letterSpacing = 0.2f
        }

        if (!TextUtils.isEmpty(mobile)) {
            et_input_phone.setText(mobile)
            et_input_phone.setSelection(mobile.length)
            et_input_phone.setTextColor(ContextCompat.getColor(this@ResetPwdActivity, R.color.gray_222222))
            ll_verify_code.visibility = View.VISIBLE
            getCode(mobile)
        }

        initListener()
    }


    private fun initListener() {
        iv_hide_pwd.setOnClickListener {
            val phone = et_input_phone.text.toString().trim()

            if (TextUtils.isEmpty(phone)) {

                ToastUtils.showCenterToast("请输入手机号")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showCenterToast("输入手机号格式不正确，请重新输入")
                return@setOnClickListener
            }


            isShowPwd = !isShowPwd
            if (isShowPwd) {
                iv_hide_pwd.setImageResource(R.mipmap.icon_display)
                et_input_pwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                iv_hide_pwd.setImageResource(R.mipmap.icon_hide)
                et_input_pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        et_input_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val phone = s.toString().trim()

                if (!TextUtils.isEmpty(phone)) {
                    et_input_phone.setTextColor(ContextCompat.getColor(this@ResetPwdActivity, R.color.gray_222222))
                } else {
                    et_input_phone.setTextColor(ContextCompat.getColor(this@ResetPwdActivity, R.color.gray_999))
                }
                phone.let {

                    if (phone.length == 11 && RegexUtils.isMobileSimple(phone)) {
                        ll_verify_code.visibility = View.VISIBLE
                    } else {
                        ll_verify_code.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        et_input_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val result = s.toString().trim()
                if (!TextUtils.isEmpty(result)) {
                    et_input_pwd.setTextColor(ContextCompat.getColor(this@ResetPwdActivity, R.color.gray_222222))
                } else {
                    et_input_pwd.setTextColor(ContextCompat.getColor(this@ResetPwdActivity, R.color.gray_999))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        tv_reset_pwd.setOnClickListener {
            val phone = et_input_phone.text.toString().trim()

            val pwd = et_input_pwd.text.toString().trim()

            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showCenterToast("请输入手机号")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showCenterToast("输入手机号格式不正确，请重新输入")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showCenterToast("请输入密码")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showCenterToast("请输入验证码！")
                return@setOnClickListener
            }
            resetPwd(phone, code, pwd)


        }

        tv_get_code.setOnClickListener {
            //发送验证码
            val phone = et_input_phone.text.toString().trim()
            getCode(phone)

        }

        verificationCodeInput.setOnCompleteListener {
            code = it
        }


    }

    private fun getCode(phone: String) {
        (mPresenter as? UserInfoPresenter)?.sendCode(phone)

    }


    private fun resetPwd(phone: String, code: String?, pwd: String) {

        (mPresenter as? UserInfoPresenter)?.resetPwd(phone, code, pwd)

    }

    override fun sendCodeSuccess() {
        showGetCodeDisplay(tv_get_code)
    }

    override fun showResetPwdSuccess() {
        finish()
    }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return ""
    }
}