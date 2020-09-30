package com.yc.emotion.home.mine.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.listener.OnUserInfoListener
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.fragment.common.ShareAppFragment
import com.yc.emotion.home.message.ui.activity.MessageActivity
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.domain.model.UserInfoModel
import com.yc.emotion.home.mine.ui.activity.*
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivity
import com.yc.emotion.home.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_mine.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import yc.com.rthttplibrary.bean.ResultInfo
import yc.com.rthttplibrary.config.HttpConfig

/**
 * Created by mayn on 2019/4/23.
 */

class MineFragment : BaseFragment<BasePresenter<*, *>>() {

    private var mMainActivity: MainActivity? = null

    private var invatationcode by Preference(ConstantKey.INVITATION_CODE, "")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_mine
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun initViews() {


        tv_business.text = HtmlCompat.fromHtml("商务微信号 ：<font color='#FF2D55'>qgzj0001</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        main_t5_tv_btn_info.clickWithTrigger { toUserInfo() }

        mineItemView_feedback.clickWithTrigger { mMainActivity?.startActivity(Intent(mMainActivity, FeedbackActivity::class.java)) }
//        mineItemView_help.clickWithTrigger {  }
        mineItemView_privacy_policy.clickWithTrigger { startActivity(Intent(mMainActivity, PrivacyStatementActivity::class.java)) }
        mineItemView_setting.clickWithTrigger {
            mMainActivity?.startActivity(Intent(mMainActivity, SettingActivity::class.java))
        }
        ll_mine_collect.clickWithTrigger {
            mMainActivity?.let {
                if (!UserInfoHelper.instance.goToLogin(mMainActivity)) {
                    mMainActivity?.startActivity(Intent(mMainActivity, CollectActivity::class.java))
                }
            }
        }
        ll_mine_order.clickWithTrigger {
            mMainActivity?.let {
                if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                    startActivity(Intent(mMainActivity, OrderActivity::class.java))
            }
        }
        ll_mine_test_report.clickWithTrigger {
            mMainActivity?.let {
                if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                    startActivity(Intent(mMainActivity, MyTestReportActivity::class.java))
            }
        }
        rl_person_face.clickWithTrigger { toUserInfo() }
        main_t5_tv_name.clickWithTrigger { toUserInfo() }
        mineItemView_invite.clickWithTrigger {
//            startActivity(Intent(mMainActivity, ShareActivity::class.java))
            val shareAppFragment = ShareAppFragment()
            mMainActivity?.let {

                shareAppFragment.show(it.supportFragmentManager, "")
            }
            MobclickAgent.onEvent(mMainActivity, "share_reward", "分享赚钱")
        }
        ll_vip.clickWithTrigger {
            MobclickAgent.onEvent(mMainActivity, "vip_pay_id", "VIP特权卡点击")
            mMainActivity?.let {
                if (!UserInfoHelper.instance.goToLogin(mMainActivity)) {
                    startActivity(Intent(mMainActivity, BecomeVipActivity::class.java))
                }
            }
        }
        ll_make_money.clickWithTrigger {
//            startActivity(Intent(mMainActivity, ConsultAppointActivity::class.java))
            MobclickAgent.onEvent(mMainActivity, "reward_click", "赚现金点击")
            mMainActivity?.let {
                if (!UserInfoHelper.instance.goToLogin(mMainActivity)) {
                    startActivity(Intent(mMainActivity, RewardActivity::class.java))
                }
            }


        }
        rl_user_info.clickWithTrigger { toUserInfo() }
        mineItemView_message.clickWithTrigger {
            startActivity(Intent(mMainActivity, MessageActivity::class.java))
        }
        mineItemView_live.clickWithTrigger {
            val livePermissionFragment = LivePermissionFragment()
            livePermissionFragment.show(childFragmentManager, "")
        }
        tv_business.clickWithTrigger {
            copyBusiness("qgzj0001", "微信号已复制，请到微信里添加！")
        }
        tv_invitation_copy.clickWithTrigger {
            copyBusiness(invatationcode, "邀请码已复制，快去邀请好友赚钱吧！")
        }
    }

    override fun lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_PERSONAL_ID)
        initData()
    }


    private fun initData() {
        val id = UserInfoHelper.instance.getUid()
        if (id <= 0) {

            fillNoLoginData()
            return
        }

//        CommonInfoHelper.getO<UserInfo>(mMainActivity, "${id}_mine_user_info", object : TypeReference<UserInfo>() {
//
//        }.type) { idCorrelationLoginBean ->
//
//            Log.d("securityhttp", "netIsVipData: idCorrelationLoginBean $idCorrelationLoginBean")
//            idCorrelationLoginBean?.let { fillData(idCorrelationLoginBean) }
//
//        }

        fillData(UserInfoHelper.instance.getUserInfo())

    }

    private fun getInvitationCode() {

        val userInfoModel = UserInfoModel(mMainActivity)
        userInfoModel.getRewardInfo()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(object : DisposableObserver<ResultInfo<RewardInfo>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ResultInfo<RewardInfo>) {
                t.let {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        if (!TextUtils.isEmpty(t.data.code))
                            invatationcode = t.data.code
                        tv_invitation.text = "邀请码：$invatationcode"
                    }

                }
            }

            override fun onError(e: Throwable) {

            }
        })

    }

    private fun netIsVipData() {

        mMainActivity?.getUserInfo(listener = object : OnUserInfoListener {
            override fun onUserInfo(userInfo: UserInfo) {
                fillData(userInfo)
            }

        })

    }

    private fun fillNoLoginData() {
        main_t5_tv_name.text = "登录/注册"
        iv_person_face.setImageResource(R.mipmap.main_icon_default_head)
        main_t5_tv_btn_info.visibility = View.GONE
        iv_vip_tips.visibility = View.GONE
        ll_invitation_container.visibility = View.GONE
        tv_vip_expire_time.visibility = View.GONE
    }


    @SuppressLint("SetTextI18n")
    private fun fillData(userInfo: UserInfo?) {
        var count = 0//统计完成程度

        var nickName = userInfo?.nick_name
        val face = userInfo?.face
        val sex = userInfo?.sex
        val signature = userInfo?.signature
        val age = userInfo?.age
        val profession = userInfo?.profession


        if (!TextUtils.isEmpty(nickName)) {
            count += 1
        }

        when (userInfo?.vip_tips) {
            0, 2 -> {//0 未开通
                if (TextUtils.isEmpty(nickName))
                    nickName = "普通用户"
//                main_t5_tv_btn_info.visibility = View.VISIBLE
                ll_invitation_container.visibility = View.GONE
                tv_vip_expire_time.visibility = View.GONE
            }
            1 -> {// 1 已开通
                if (TextUtils.isEmpty(nickName))
                    nickName = "VIP用户"
                iv_vip_tips.visibility = View.VISIBLE
                main_t5_tv_btn_info.visibility = View.GONE
                tv_vip_expire_time.visibility = View.VISIBLE
                tv_vip_expire_time.text = "到期时间：${DateUtils.formatTimeToStr(userInfo.vip_end_time.toLong(), "yyyy.MM.dd")}"
                if (!TextUtils.isEmpty(invatationcode)) {
                    ll_invitation_container.visibility = View.VISIBLE
                    tv_invitation.text = "邀请码：$invatationcode"
                }
            }
        }
        main_t5_tv_name.text = nickName

        if (!TextUtils.isEmpty(signature)) {
            count += 1
        }
        if (sex != 0) {
            count += 1
        }

        if (!TextUtils.isEmpty(age)) {
            count += 1
        }

        if (!TextUtils.isEmpty(profession)) {
            count += 1
        }

        val inters = userInfo?.inters
        inters?.let {
            if (inters.isNotEmpty()) {
                count += 1
            }
        }

        if (!TextUtils.isEmpty(face)) {
            count += 1
            Glide.with(this).load(face).apply(RequestOptions.circleCropTransform().error(R.mipmap.main_icon_default_head)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)).into(iv_person_face)
        }

        val ratio = String.format("%.0f", (count / 7f) * 100)
        var text = "信息完善度$ratio%"
        if (ratio == "100") {
            text = "信息已完善"
        }
//        main_t5_tv_btn_info.visibility = View.VISIBLE
        main_t5_tv_btn_info.text = text

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventLoginState) {
        when (event.state) {
            EventLoginState.STATE_EXIT -> {

                fillNoLoginData()
            }
            EventLoginState.STATE_LOGINED ->
                //                setTitleName();
            {
                netIsVipData()
                getInvitationCode()
            }
            EventLoginState.STATE_UPDATE_INFO -> {
                val userInfo = event.userInfo
                fillData(userInfo)
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        netIsVipData()
        getInvitationCode()
    }

    private fun copyBusiness(wx: String, mess: String) {
        val myClipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", wx)
        myClipboard.primaryClip = myClip
        ToastUtils.showCenterToast(mess)
        mMainActivity?.openWeiXin()
    }

    private fun toUserInfo() {
        mMainActivity?.let {
            if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                mMainActivity?.startActivity(Intent(mMainActivity, UserInfoActivity::class.java))
        }
    }

}
