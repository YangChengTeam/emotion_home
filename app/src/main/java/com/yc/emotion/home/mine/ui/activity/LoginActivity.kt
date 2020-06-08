package com.yc.emotion.home.mine.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.*
import androidx.core.content.ContextCompat
import com.music.player.lib.util.ToastUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.RegexUtils
import kotlinx.android.synthetic.main.activity_login.*

import org.greenrobot.eventbus.EventBus

/**
 *
 * Created by suns  on 2019/10/21 08:46.
 */
class LoginActivity : BaseSameActivity(), UserInfoView {


    private var isShowPwd = false
    private var mobile by Preference(ConstantKey.PhHONE, "")

    private var direct_finish: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {

        mPresenter = UserInfoPresenter(this, this)

        intent?.let {
            direct_finish = intent.getBooleanExtra("direct_finish", false)//是否直接关闭
        }


        if (!TextUtils.isEmpty(mobile)) {
            et_input_phone.setText(mobile)
            et_input_phone.setSelection(mobile.length)
            et_input_phone.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray_222222))
        }
        tv_login_register.text = Html.fromHtml("没有账号？" + "<font color='#FF2D55'>立即注册</font>")

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

        tv_register_pwd.setOnClickListener {
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


            login(phone, pwd)


        }
        et_input_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val result = s.toString().trim()
                if (!TextUtils.isEmpty(result)) {
                    et_input_phone.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray_222222))
                } else {
                    et_input_phone.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray_999))
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
                    et_input_pwd.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray_222222))
                } else {
                    et_input_pwd.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.gray_999))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        tv_login_register.setOnClickListener {
            startActivity(Intent(this, RegisterMainActivity::class.java))
        }

        tv_forget_pwd.setOnClickListener {
            startActivity(Intent(this, ResetPwdActivity::class.java))
        }
    }


    private fun login(phone: String, pwd: String) {


        (mPresenter as? UserInfoPresenter)?.phoneLogin(phone, pwd, "")


    }

    override fun showPhoneLoginSuccess(userInfo: UserInfo?) {
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED, userInfo))

        if (direct_finish) {
            finish()
            return
        }
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return ""
    }
}