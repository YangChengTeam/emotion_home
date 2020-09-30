package com.yc.emotion.home.mine.ui.fragment

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.mine.adapter.RewardMoneyAdapter
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.view.RewardView
import com.yc.emotion.home.utils.ItemDecorationHelper
import kotlinx.android.synthetic.main.fragment_reward_money.*

/**
 *
 * Created by suns  on 2020/8/28 20:14.
 */
class RewardMoneyFragment : BaseFragment<RewardPresenter>(), RewardView {

    private var page = 1
    private var pageSize = 10
    private var rewardMoneyAdapter: RewardMoneyAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_reward_money
    }

    override fun initViews() {
        mPresenter = RewardPresenter(activity, this)
        rcv_reward.layoutManager = LinearLayoutManager(activity)
        rewardMoneyAdapter = RewardMoneyAdapter(null)
        rcv_reward.adapter = rewardMoneyAdapter
        rcv_reward.addItemDecoration(ItemDecorationHelper(activity, 10))
        initListener()

    }

    private fun initListener() {
        rewardMoneyAdapter?.setOnLoadMoreListener({
            getData()
        }, rcv_reward)
        activity?.let { ContextCompat.getColor(it, R.color.app_color) }?.let { swipeRefreshLayout.setColorSchemeColors(it) }
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }
    }

    override fun showLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).showLoadingDialog()
        }
    }

    override fun hideLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).hideLoadingDialog()
        }
    }

    fun getData() {
        mPresenter?.getRewardMoneyList(page, pageSize)
    }

    override fun lazyLoad() {
        getData()
    }

    override fun showRewardInfo(data: RewardInfo?) {

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
        if (page == 1) {
            rewardMoneyAdapter?.setNewData(data)
        } else {
            data?.let {
                rewardMoneyAdapter?.addData(data)
            }
        }

        if (data?.size == pageSize) {
            page++
            rewardMoneyAdapter?.loadMoreComplete()
        } else {
            rewardMoneyAdapter?.loadMoreEnd()
        }

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }

    }

    override fun showRankingList(data: List<RewardDetailInfo>?) {

    }

    override fun showBindInvitation(data: RewardInfo?) {

    }

    override fun onNoData() {
        super.onNoData()
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onError() {
        super.onError()
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }
}