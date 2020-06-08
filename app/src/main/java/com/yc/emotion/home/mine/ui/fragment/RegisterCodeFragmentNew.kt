package com.yc.emotion.home.mine.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import androidx.navigation.Navigation
import com.music.player.lib.util.ToastUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.constant.Constant
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.ui.activity.RegisterMainActivityNew
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_register_code.*

/**
 *
 * Created by suns  on 2019/10/19 10:27.
 */
class RegisterCodeFragmentNew : BaseNavFragment<UserInfoPresenter>(), UserInfoView {


    private var mMainActivity: RegisterMainActivityNew? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RegisterMainActivityNew) {
            mMainActivity = context
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_register_code
    }

    private var mLoveEngine: LoveEngine? = null

    private var mobile by Preference(ConstantKey.PhHONE, "")

    companion object {
        fun newInstance(phone: String): RegisterCodeFragmentNew {
            val registerCodeFragment = RegisterCodeFragmentNew()

            registerCodeFragment.fragmentTag = Constant.REGISTER_CODE

            val bundle = Bundle()

            bundle.putString("phone", phone)

            registerCodeFragment.arguments = bundle

            return registerCodeFragment
        }
    }


    override fun initViews() {
        mPresenter = UserInfoPresenter(mMainActivity, this)
        mLoveEngine = LoveEngine(mMainActivity)
        initView()
    }

    override fun lazyLoad() {

    }

    private var inputCode = ""//获取输入验证码

    private var phone: String? = null


    private fun initView() {

        phone = arguments?.getString("phone")
        tv_code_tint.text = Html.fromHtml("验证码已发送至<font color='#222222'>+86 $phone</font>")


        getCode()

        initListener()
    }

    private fun initListener() {
//        tv_set_pwd.setOnClickListener { startActivity(Intent(this, RegisterPwdActivity::class.java)) }
        verificationCodeInput.setOnCompleteListener {
            inputCode = it

//            if (it.length == 4) {
////                YcSingle.getInstance().
//                val uId = UserInfoHelper.instance.getUid() as Int
//
//                if (uId <= 0 && TextUtils.isEmpty(mobile)) {
////                    mMainActivity?.switchFragment(RegisterPwdFragment.newInstance(phone, it), fragmentTag)
//                    val bundle = Bundle()
//                    bundle.putString("phone", phone)
//                    bundle.putString("code", it)
//
//                    Navigation.findNavController(verificationCodeInput).navigate(R.id.action_to_registerPwd, bundle)
//
//
//                } else {
//                    mMainActivity?.codeLogin(phone, it)
//                }
//
//            }

        }

        tv_register_code_login.setOnClickListener {
//            Log.e("TAG", "${inputCode.length}")
            if (TextUtils.isEmpty(inputCode)) {
                ToastUtils.showCenterToast("请输入验证码")
                return@setOnClickListener
            }

            if (inputCode.length < 4) {
                ToastUtils.showCenterToast("验证码输入长度有误")
                return@setOnClickListener
            }



            if (inputCode.length == 4) {
//                YcSingle.getInstance().
                val uId = UserInfoHelper.instance.getUid() as Int

                if (uId <= 0 && TextUtils.isEmpty(mobile)) {
//                    mMainActivity?.switchFragment(RegisterPwdFragment.newInstance(phone, it), fragmentTag)
                    val bundle = Bundle()
                    bundle.putString("phone", phone)
                    bundle.putString("code", inputCode)

                    Navigation.findNavController(verificationCodeInput).navigate(R.id.action_to_registerPwd, bundle)


                } else {
                    mMainActivity?.codeLogin(phone, inputCode)
                }

            }

        }

        tv_get_code_again.setOnClickListener {
            getCode()
        }
    }


    private fun getCode() {
        mPresenter?.sendCode(phone)

    }

    override fun sendCodeSuccess() {
        mMainActivity?.showGetCodeDisplay(tv_get_code_again)
    }

    override fun showLoadingDialog() {

    }

    override fun hideLoadingDialog() {

    }
}