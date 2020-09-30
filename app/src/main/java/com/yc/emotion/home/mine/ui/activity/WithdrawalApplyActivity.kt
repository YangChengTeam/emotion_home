package com.yc.emotion.home.mine.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.ui.fragment.DisposeConfirmFragment
import com.yc.emotion.home.mine.view.RewardView
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_withdrawal_apply.*
import org.greenrobot.eventbus.EventBus
import yc.com.rthttplibrary.util.ScreenUtil

/**
 *
 * Created by suns  on 2020/8/24 16:00.
 */
class WithdrawalApplyActivity : BaseSameActivity(), RewardView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun offerActivityTitle(): String? {
        return "提现"
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_withdrawal_apply
    }

    override fun initViews() {
        super.initViews()
        val money = intent?.getFloatExtra("money", 0f)
        tv_dispose_money_tint.text = "当前金额为${money}元，可提现金额为100的倍数"
        mPresenter = RewardPresenter(this, this)
        mBaseSameTvSub.text = "提现明细"
        mBaseSameTvSub.setTextColor(ContextCompat.getColor(this, R.color.red_fa4c67))
        mBaseSameTvSub.textSize = 15f
        tv_contact_service.text = HtmlCompat.fromHtml("提现过程遇见问题，请联系客服微信号<font color='#fa4a65'>pai201807</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        initListener()
    }

    private fun initListener() {
        mBaseSameTvSub.clickWithTrigger {
            startActivity(Intent(this, DisposeDetailActivity::class.java))
        }

        tv_deposit.clickWithTrigger {
            val money = et_dispose_money.text.toString().trim()
            val name = et_dispose_name.text.toString().trim()
            val account = et_dispose_account.text.toString().trim()

            if (TextUtils.isEmpty(money)) {
                ToastUtils.showCenterToast("提现金额不能为空")
                return@clickWithTrigger
            }
            if (TextUtils.isEmpty(account)) {
                ToastUtils.showCenterToast("支付宝账号不能为空")
                return@clickWithTrigger
            }
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showCenterToast("姓名不能为空")
                return@clickWithTrigger
            }

            val disposeConfirmFragment = DisposeConfirmFragment.newInstance(name, account, money)
            disposeConfirmFragment.setOnSubmitApplyListener(object : DisposeConfirmFragment.OnSubmitApplyListener {
                override fun onSubmit() {
                    (mPresenter as RewardPresenter).applyDispose(money, account, name)
                }
            })
            disposeConfirmFragment.show(supportFragmentManager, "")
        }
    }

    override fun showRewardInfo(data: RewardInfo?) {

    }

    override fun showBindSuccess() {

    }

    override fun showRewardDetailList(data: List<RewardDetailInfo>?) {

    }

    override fun showDisposeApplySuccess() {
        EventBus.getDefault().post("dispose_success")
        finish()
    }


    override fun showDisposeDetailInfoList(data: List<DisposeDetailInfo>?) {

    }

    override fun showRewardMoneyList(data: List<RewardDetailInfo>?) {

    }

    override fun showRankingList(data: List<RewardDetailInfo>?) {

    }

    override fun showBindInvitation(data: RewardInfo?) {

    }


    companion object {
        fun startActivity(context: Context, totalMoney: Float?) {
            val intent = Intent(context, WithdrawalApplyActivity::class.java)
            intent.putExtra("money", totalMoney)
            context.startActivity(intent)
        }
    }
}