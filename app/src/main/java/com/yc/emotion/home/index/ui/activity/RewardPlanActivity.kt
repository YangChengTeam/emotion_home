package com.yc.emotion.home.index.ui.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.ui.activity.WithdrawalApplyActivity
import com.yc.emotion.home.mine.view.RewardView
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivityNew
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_reward_plan.*

/**
 *
 * Created by suns  on 2020/8/27 14:51.
 */
class RewardPlanActivity : BaseSameActivity(), RewardView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        invadeStatusBar()

        initViews()

    }

    override fun hindActivityBar(): Boolean {
        return true
    }

    override fun hindActivityTitle(): Boolean {
        return true
    }

    override fun offerActivityTitle(): String? {
        return "现金活动"
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_reward_plan
    }


    override fun initViews() {
        super.initViews()
        mPresenter = RewardPresenter(this, this)
        (mPresenter as RewardPresenter).getRewardInfo()

        initListener()

    }

    private fun initListener() {
        iv_plan_back.clickWithTrigger { finish() }
        tv_dispose.clickWithTrigger {
            if (isDispose) {
                WithdrawalApplyActivity.startActivity(this, money)
                MobclickAgent.onEvent(this, "dispose_click", "提现点击")
            } else {
//                startActivity(Intent(this, BecomeVipActivity::class.java))
                BecomeVipActivityNew.startActivity(this, true)
                MobclickAgent.onEvent(this, "reward_vip_click", "赚现金VIP点击")
            }
        }
    }

    var isDispose = false
    var money: Float = 0f
    override fun showRewardInfo(data: RewardInfo?) {
        data?.let {
            Glide.with(this).load(it.cover).into(iv_reward_plan_detail)
            if (it.has_permission == 1) {
                money = it.money
                if (money > 0) {
                    tv_reward_money.text = "可提现金额${money.toInt()}元"
                    iv_plan_icon.visibility = View.VISIBLE
                    tv_dispose.text = "提现"
                    isDispose = true
                } else {
                    tv_reward_money.text = "没有可以提现的金额,快去邀请吧~"
                    iv_plan_icon.visibility = View.GONE
                    tv_dispose.visibility = View.GONE
                }
            } else {
                tv_reward_money.text = "成为VIP合伙人 获取专属邀请码~"
                iv_plan_icon.visibility = View.GONE
                tv_dispose.text = "立即开通"
                isDispose = false
            }
        }
    }

    override fun showBindSuccess() {

    }

    override fun showRewardDetailList(data: List<RewardDetailInfo>?) {

    }

    override fun showDisposeApplySuccess() {

    }

    override fun showDisposeDetailInfoList(data: List<DisposeDetailInfo>?) {

    }

    override fun showRewardMoneyList(data: List<RewardDetailInfo>?) {

    }

    override fun showRankingList(data: List<RewardDetailInfo>?) {

    }

    override fun showBindInvitation(data: RewardInfo?) {

    }


}