package com.yc.emotion.home.mine.ui.fragment

import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.navigation.Navigation
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.fragment_register.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * Created by suns  on 2020/4/27 15:54.
 */
class RegisterFragment : BaseFragment<UserInfoPresenter>(), UserInfoView {

    var isCodeLogin = true//是否是验证码登录
    private var phone by Preference(ConstantKey.PhHONE, "")

    override fun lazyLoad() {

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initViews() {

        arguments?.let {
            isCodeLogin = it.getBoolean("isCodeLogin")
        }
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone)
            et_phone.setSelection(phone.length)
            tv_login_tint.text = "欢迎登录"
            tv_register_btn.text = getString(R.string.login)
            tv_user_policy.text = HtmlCompat.fromHtml("登录即代表同意<font color='#ff2d55'>《用户协议》</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            tv_user_policy.text = HtmlCompat.fromHtml("注册即代表同意<font color='#ff2d55'>《用户协议》</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
        mPresenter = UserInfoPresenter(activity, this)
        pwd_group.visibility = if (isCodeLogin) View.GONE else View.VISIBLE

        initListener()


    }

    override fun showLoading() {
        (activity as BaseActivity).showLoading()
    }

    override fun hideLoading() {
        (activity as BaseActivity).hideLoading()
    }


    private fun initListener() {
        tv_register_btn.clickWithTrigger { Navigation.findNavController(it).navigate(R.id.action_to_setPwd) }
        tv_user_policy.clickWithTrigger { // todo 用户协议
            val userPolicyFragment = UserPolicyFragment()
            userPolicyFragment.show(childFragmentManager, "")
        }

        tv_register_btn.clickWithTrigger {
            val phone = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            val pwd = et_pwd.text.toString().trim()
            val invitationCode = et_invitation_code.text.toString().trim()
            mPresenter?.phoneRegister(phone, pwd, code, invitationCode)
        }

        tv_get_code.clickWithTrigger {
            val phone = et_phone.text.toString().trim()
            mPresenter?.sendCode(phone)
        }
    }

    override fun showPhoneRegisterSuccess(data: UserInfo?) {
        super.showPhoneRegisterSuccess(data)
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED, data))
        activity?.finish()
    }

    override fun sendCodeSuccess() {
        super.sendCodeSuccess()
        countDownTimer.start()
    }

    /**
     * 验证码倒计时
     */

    private val countDownTimer = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            tv_get_code.visibility = View.GONE
            tv_code_count.visibility = View.VISIBLE
            tv_code_count.text = "${(millisUntilFinished / 1000)}s"

        }

        override fun onFinish() {
            tv_get_code.visibility = View.VISIBLE
            tv_code_count.visibility = View.GONE

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }


}