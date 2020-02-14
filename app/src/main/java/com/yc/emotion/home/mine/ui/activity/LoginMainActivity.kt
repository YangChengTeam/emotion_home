package com.yc.emotion.home.mine.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.kk.utils.LogUtil
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.index.ui.activity.SelectSexActivity
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.ui.activity.ProtocolActivity
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UserLoginManager
import kotlinx.android.synthetic.main.activity_login_main.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * Created by suns  on 2019/10/19 09:08.
 */
class LoginMainActivity : BaseActivity(), UserInfoView {


    private var manager: UserLoginManager? = null

    private var sex by Preference(ConstantKey.SEX, 0)


    override fun getLayoutId(): Int {
        return R.layout.activity_login_main
    }

    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
        mPresenter = UserInfoPresenter(this,this)
        initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)
        initViews()
    }

    private fun initView() {
        val finish = intent?.getBooleanExtra("direct_finish", false)//是否直接关闭

        tv_login_protocal.text = Html.fromHtml("登录即代表同意" + "<font color='#FF2D55'>《用户协议》</font>")
        manager = UserLoginManager.get()
        initListener(finish)
    }

    private fun initListener(finish: Boolean?) {
        tv_expire_go.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))

            finish()
        }

        iv_login_close.setOnClickListener {
            //            finish?.let {

            if (null != finish && finish) {

                finish()
                return@setOnClickListener
            }
            if (sex != 0) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, SelectSexActivity::class.java))
            }

            finish()
        }

        tv_register.setOnClickListener {
            val intent = Intent(this, RegisterMainActivity::class.java)
            intent.putExtra("direct_finish", finish)
            startActivity(intent)
            finish()
        }

        ll_pwd_login.setOnClickListener {
            val intent =Intent(this, LoginActivity::class.java)
            intent.putExtra("direct_finish", finish)

            startActivity(intent)
            finish()
        }

        ll_wx_login.setOnClickListener {
            //todo 微信登录
            manager?.setCallBack(this) {

                Log.e("TAG", it.toString())
                thirdLogin(it.openid, 2, it.iconUrl, it.gender, it.nickname, finish)
            }?.oauthAndLogin(SHARE_MEDIA.WEIXIN)
        }

        ll_qq_login.setOnClickListener {
            manager?.setCallBack(this) {
                LogUtil.msg("userinfo:  ${it.accessToken}--${it.openid}")

                thirdLogin(it.openid, 1, it.iconUrl, it.gender, it.nickname, finish)
            }?.oauthAndLogin(SHARE_MEDIA.QQ)
        }

        tv_login_protocal.setOnClickListener {
            startActivity(Intent(this, ProtocolActivity::class.java))
        }
    }


    private fun thirdLogin(access_token: String?, account_type: Int, face: String?, sex: String?, nick_name: String?, finish: Boolean?) {

        (mPresenter as? UserInfoPresenter)?.thirdLogin(access_token,account_type,face,sex,nick_name,finish)

    }

    override fun thirdLoginSuccess(data: UserInfo?, finish: Boolean?) {
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED,data))

        if (null != finish && finish) {
            finish()
            return
        }
        startActivity(Intent(this@LoginMainActivity, MainActivity::class.java))
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        UMShareAPI.get(this).onSaveInstanceState(outState)
    }
}