package com.yc.emotion.home.index.ui.fragment

import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.listener.ThirdLoginListener
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseBottomSheetDialogFragment
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.presenter.UserInfoPresenter
import com.yc.emotion.home.mine.ui.activity.LoginRegisterActivity
import com.yc.emotion.home.mine.ui.activity.PrivacyStatementActivity
import com.yc.emotion.home.mine.ui.activity.UserPolicyActivity
import com.yc.emotion.home.mine.view.UserInfoView
import com.yc.emotion.home.model.bean.UserAccreditInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.utils.UserLoginManager
import com.yc.emotion.home.utils.clickWithTrigger
import org.greenrobot.eventbus.EventBus

/**
 * Created by suns  on 2020/6/4 11:48.
 */
class WxLoginFragment : BaseBottomSheetDialogFragment(), UserInfoView {

    private var ivWxClose: ImageView? = null

    private var ivWxLogin: ImageView? = null

    private var ivPhoneLogin: ImageView? = null

    private var tvUserPolicy: TextView? = null

    private var tvPrivacy: TextView? = null

    private var tvOtherLogin: TextView? = null
    private var manager: UserLoginManager? = null

    private var mPresenter: BasePresenter<out IModel, out IView>? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_wx_login
    }

    override fun initViews() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }



    override fun initView() {
        super.initView()
        ivWxClose = rootView?.findViewById(R.id.iv_wx_close)

        ivWxLogin = rootView?.findViewById(R.id.iv_wx_login)

        ivPhoneLogin = rootView?.findViewById(R.id.iv_phone_login)

        tvUserPolicy = rootView?.findViewById(R.id.tv_user_policy)

        tvPrivacy = rootView?.findViewById(R.id.tv_privacy)
        tvOtherLogin = rootView?.findViewById(R.id.tv_other_login)
        mPresenter = UserInfoPresenter(activity, this)
        manager = UserLoginManager.get()
        initListener()
    }

    private fun initListener() {
        ivWxClose?.clickWithTrigger { dismiss() }
        ivWxLogin?.clickWithTrigger {

            manager?.setCallBack(activity, object : ThirdLoginListener {
                override fun onLoginResult(userDataInfo: UserAccreditInfo) {
                    thirdLogin(userDataInfo.openid, userDataInfo.iconUrl, userDataInfo.gender, userDataInfo.nickname)
                }

            })?.oauthAndLogin(SHARE_MEDIA.WEIXIN)
        }

        tvUserPolicy?.clickWithTrigger {

            startActivity(Intent(activity, UserPolicyActivity::class.java))
        }

        tvPrivacy?.clickWithTrigger {
            startActivity(Intent(activity, PrivacyStatementActivity::class.java))
        }
        ivPhoneLogin?.clickWithTrigger {
            startActivity(Intent(activity, LoginRegisterActivity::class.java))
            dismiss()
        }
    }

    private fun thirdLogin(access_token: String?, face: String?, sex: String?, nick_name: String?) {

        (mPresenter as? UserInfoPresenter)?.thirdLogin(access_token, 2, face, sex, nick_name, false)

    }

    override fun thirdLoginSuccess(data: UserInfo?, finish: Boolean?) {
        super.thirdLoginSuccess(data, finish)
        EventBus.getDefault().post(EventLoginState(EventLoginState.STATE_LOGINED, data))
        dismiss()
    }
}