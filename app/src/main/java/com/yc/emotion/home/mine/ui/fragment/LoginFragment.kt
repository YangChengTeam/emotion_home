package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.fragment_login.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * Created by suns  on 2020/4/27 15:51.
 */
class LoginFragment : BaseFragment<UserInfoPresenter>(), UserInfoView {
    private var phone by Preference(ConstantKey.PhHONE, "")

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initViews() {
        mPresenter = UserInfoPresenter(activity, this)
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone)
            et_phone.setSelection(phone.length)
        }

        tv_code_login.text = HtmlCompat.fromHtml("忘记了？<font color='#ff2d55'>验证码登录</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        tv_user_policy.text = HtmlCompat.fromHtml("登录即代表同意<font color='#ff2d55'>《用户协议》</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        initListener()
    }

    override fun showLoading() {
        (activity as BaseActivity).showLoading()
    }

    override fun hideLoading() {
        (activity as BaseActivity).hideLoading()
    }



    private fun initListener() {
        tv_code_login.clickWithTrigger {

            Navigation.findNavController(it).navigate(R.id.action_to_register)
        }
        tv_user_policy.clickWithTrigger {
            val userPolicyFragment = UserPolicyFragment()
            userPolicyFragment.show(childFragmentManager, "")
        }

        tv_register.clickWithTrigger {
            val bundle = Bundle()
            bundle.putBoolean("isCodeLogin", false)
            Navigation.findNavController(it).navigate(R.id.action_to_register, bundle)
        }

        tv_login_btn.clickWithTrigger {
            val phone = et_phone.text.toString().trim()
            val pwd = et_pwd.text.toString().trim()
            mPresenter?.phoneLogin(phone, pwd, "")
        }
    }

    override fun showPhoneLoginSuccess(userInfo: UserInfo?) {
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED, userInfo))
        activity?.onBackPressed()
    }

    override fun lazyLoad() {

    }

}