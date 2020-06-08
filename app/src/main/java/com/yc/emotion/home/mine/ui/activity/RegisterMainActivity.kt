package com.yc.emotion.home.mine.ui.activity

import android.content.Intent
import android.os.Bundle
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.ui.fragment.RegisterPhoneFragment
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.utils.UserInfoHelper
import org.greenrobot.eventbus.EventBus

/**
 *
 * Created by suns  on 2019/10/21 11:08.
 */
class RegisterMainActivity : BaseSameActivity(), UserInfoView {

    private var direct_finish: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_register_main
    }


    override fun initViews() {
        mPresenter = UserInfoPresenter(this, this)
        intent?.let {
            direct_finish = intent.getBooleanExtra("direct_finish", false)//是否直接关闭
        }


        val bt = supportFragmentManager.beginTransaction()
        bt.replace(R.id.fl_container, RegisterPhoneFragment.newInstance(), RegisterPhoneFragment.newInstance().fragmentTag)
        bt.commit()

        ivBack.setOnClickListener {
            popBackStack()
        }
    }


    fun switchFragment(fragment: BaseFragment<*>, tag: String) {
        val bt = supportFragmentManager.beginTransaction()
        bt.add(R.id.fl_container, fragment, fragment.fragmentTag)
        supportFragmentManager.findFragmentByTag(tag)?.let { bt.hide(it) }

        bt.addToBackStack(null)

        bt.commit()

    }


    private fun popBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }


    override fun onBackPressed() {
//        super.onBackPressed()

        popBackStack()


    }


    /**
     * 验证码登录
     */
    fun codeLogin(phone: String?, code: String?) {

        (mPresenter as? UserInfoPresenter)?.phoneLogin(phone, "", code)


    }

    /**
     * 验证码注册
     */
    fun phoneRegister(phone: String?, pwd: String, code: String?) {
        (mPresenter as? UserInfoPresenter)?.phoneRegister(phone, pwd, code)
    }


    override fun showPhoneLoginSuccess(userInfo: UserInfo?) {


        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED, userInfo))

        if (direct_finish) {
            finish()
            return
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }


    override fun showPhoneRegisterSuccess(data: UserInfo?) {
        loginSuccess(data)

        if (direct_finish) {
            finish()
            return
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun loginSuccess(data: UserInfo?) {
        //持久化存储登录信息

        UserInfoHelper.instance.saveUserInfo(data)
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED, data))


        //        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
    }


    override fun offerActivityTitle(): String {
        return ""
    }
}