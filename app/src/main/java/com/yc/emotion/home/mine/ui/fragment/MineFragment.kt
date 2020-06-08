package com.yc.emotion.home.mine.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.listener.OnUserInfoListener
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.ui.activity.ConsultAppointActivity
import com.yc.emotion.home.message.ui.fragment.MessageActivity
import com.yc.emotion.home.mine.ui.activity.*
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_main_mine.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by mayn on 2019/4/23.
 */

class MineFragment : BaseFragment<BasePresenter<*, *>>(), View.OnClickListener {

    private var mMainActivity: MainActivity? = null
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


        main_t5_tv_btn_info.setOnClickListener(this)

        mineItemView_feedback.setOnClickListener(this)
        mineItemView_help.setOnClickListener(this)
        mineItemView_privacy_policy.setOnClickListener(this)
        mineItemView_setting.setOnClickListener(this)
        ll_mine_collect.setOnClickListener(this)
        ll_mine_order.setOnClickListener(this)
        ll_mine_test_report.setOnClickListener(this)
        rl_person_face.setOnClickListener(this)
        main_t5_tv_name.setOnClickListener(this)
        mineItemView_invite.setOnClickListener(this)
        ll_vip.setOnClickListener(this)
        ll_free_order.setOnClickListener(this)
        rl_user_info.setOnClickListener(this)
        mineItemView_message.setOnClickListener(this)
        mineItemView_live.setOnClickListener(this)
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
    }


    @SuppressLint("SetTextI18n")
    private fun fillData(userInfo: UserInfo?) {
        var count = 0//统计完成程度
        Log.d("mylog", "fillData: idCorrelationLoginBean $userInfo")

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
            }
            1 -> {// 1 已开通
                if (TextUtils.isEmpty(nickName))
                    nickName = "VIP用户"
                iv_vip_tips.visibility = View.VISIBLE
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
        main_t5_tv_btn_info.visibility = View.VISIBLE
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
                netIsVipData()
            EventLoginState.STATE_UPDATE_INFO -> {
                val userInfo = event.userInfo
                fillData(userInfo)
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        netIsVipData()
    }


    override fun onClick(v: View) {

        when (v.id) {
            R.id.rl_person_face,
            R.id.main_t5_tv_name,
            R.id.main_t5_tv_btn_info,
            R.id.rl_user_info -> {
                mMainActivity?.let {
                    if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                        mMainActivity?.startActivity(Intent(mMainActivity, UserInfoActivity::class.java))
                }

            }


            R.id.ll_mine_collect -> {
                mMainActivity?.let {
                    if (!UserInfoHelper.instance.goToLogin(mMainActivity)) {
                        mMainActivity?.startActivity(Intent(mMainActivity, CollectActivity::class.java))
                    }
                }
            }
            R.id.mineItemView_help -> {
            }
            R.id.mineItemView_feedback -> mMainActivity?.startActivity(Intent(mMainActivity, FeedbackActivity::class.java))
            R.id.mineItemView_setting -> mMainActivity?.startActivity(Intent(mMainActivity, SettingActivity::class.java))
            R.id.ll_mine_order ->
                mMainActivity?.let {
                    if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                        startActivity(Intent(mMainActivity, OrderActivityNew::class.java))
                }
            R.id.ll_mine_test_report ->
                mMainActivity?.let {
                    if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                        startActivity(Intent(mMainActivity, MyTestReportActivity::class.java))
                }
            R.id.mineItemView_privacy_policy -> startActivity(Intent(mMainActivity, PrivacyStatementActivity::class.java))
            R.id.mineItemView_invite ->
                startActivity(Intent(mMainActivity, ShareActivity::class.java))
            R.id.ll_vip -> {
                MobclickAgent.onEvent(mMainActivity, "vip_pay_id", "VIP特权卡点击")
                startActivity(Intent(mMainActivity, VipActivity::class.java))
            }
            R.id.ll_free_order -> {
                startActivity(Intent(mMainActivity, ConsultAppointActivity::class.java))
                MobclickAgent.onEvent(mMainActivity, "free_booking_id", "我的免费预约")
            }
            R.id.mineItemView_message -> {
                startActivity(Intent(mMainActivity, MessageActivity::class.java))
            }
            R.id.mineItemView_live -> {
                val livePermissonFragment = LivePermissonFragment()
                livePermissonFragment.show(childFragmentManager, "")
            }
        }
    }


}
