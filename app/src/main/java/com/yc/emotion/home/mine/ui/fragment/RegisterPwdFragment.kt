package com.yc.emotion.home.mine.ui.fragment

import android.os.Bundle
import android.text.TextUtils

import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.constant.Constant
import com.yc.emotion.home.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_register_pwd.*

/**
 *
 * Created by suns  on 2019/10/21 12:32.
 */
class RegisterPwdFragment : BaseRegisterPhoneFragment() {


    private var mLoadingDialog: LoadDialog? = null

    private var mLoveEngine: LoveEngine? = null


    companion object {
        fun newInstance(phone: String?, code: String): RegisterPwdFragment {
            val registerPwdFragment = RegisterPwdFragment()
            registerPwdFragment.fragmentTag = Constant.REGISTER_PWD

            val bundle = Bundle()
            bundle.putString("phone", phone)
            bundle.putString("code", code)
            registerPwdFragment.arguments = bundle

            return registerPwdFragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_register_pwd
    }

    override fun initViews() {
        initView()
    }

    override fun lazyLoad() {

    }

    private var phone: String? = null
    private var code: String? = null


    private fun initView() {


        phone = arguments?.getString("phone")
        code = arguments?.getString("code")
        mLoadingDialog = LoadDialog(activity)
        mLoveEngine = LoveEngine(activity)

        initListener()
    }

    private fun initListener() {
        tv_register_pwd.setOnClickListener {
            val inputPwd = et_register_pwd.text.toString().trim()
            if (TextUtils.isEmpty(inputPwd)) {
                ToastUtils.showCenterToast("请输入密码")
                return@setOnClickListener
            }

            if (inputPwd.length < 6) {
                ToastUtils.showCenterToast("请设置至少6位密码")
                return@setOnClickListener
            }

            registerPhone(inputPwd)


        }
    }


    private fun registerPhone(pwd: String) {

        mMainActivity?.phoneRegister(phone, pwd, code)

    }
}