package com.yc.emotion.home.mine.ui.fragment

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseDialogFragment
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.view.RewardView
import com.yc.emotion.home.utils.clickWithTrigger
import org.greenrobot.eventbus.EventBus

/**
 * Created by suns  on 2019/9/26 17:48.
 */
class BindInvitationFragment : BaseDialogFragment(), RewardView {


    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }

    private fun initView() {

        val presenter = RewardPresenter(activity, this)
        val roundCornerImg = getView(R.id.roundCornerImg) as RoundCornerImg

        roundCornerImg.setCorner(20)

        val etInvitation = getView(R.id.et_invitation) as EditText
        val tvConfirm = getView(R.id.tv_confirm) as TextView
        tvConfirm.clickWithTrigger {
            val invitation = etInvitation.text.toString().trim()
            presenter.bindInvitationCode(invitation)
        }


        val ivClose = getView(R.id.iv_close) as ImageView
        ivClose.clickWithTrigger { v: View? -> dismiss() }
    }

    override val width: Float
        protected get() = 0.7f

    override val height: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

//    fun setWX(wx: String) {
//        mWx = wx
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.e("->", "setWX: $mWx")
//        tvWx?.text = Html.fromHtml("添加客服微信：<font color='#ddae52'>$mWx</font>")
//    }


    override fun showRewardInfo(data: RewardInfo?) {

    }

    override fun showBindSuccess() {
        dismiss()
        EventBus.getDefault().post("bind_success")
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_bind_invitation
    }

    override fun initViews() {
        initView()
    }

    override fun showLoadingDialog() {
        activity?.let {
            (it as BaseActivity).showLoadingDialog()
        }
    }

    override fun hideLoadingDialog() {
        activity?.let {
            (it as BaseActivity).hideLoadingDialog()
        }
    }

    override val animationId: Int
        get() = R.style.share_anim


}