package com.yc.emotion.home.mine.ui.fragment


import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.mine.adapter.RankingListAdapter
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
class RankingListFragment : BaseFragment<RewardPresenter>(), RewardView {

    private var page = 1
    private val pageSize = 10
    private var rankingListAdapter: RankingListAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_reward_money
    }

    override fun initViews() {
        mPresenter = RewardPresenter(activity, this)
        rcv_reward.layoutManager = LinearLayoutManager(activity)
        rankingListAdapter = RankingListAdapter(null)
        rankingListAdapter?.setEnableLoadMore(false)
        rcv_reward.adapter = rankingListAdapter



        rcv_reward.addItemDecoration(ItemDecorationHelper(activity, 10))
        initListener()
    }



    fun initListener() {
        rankingListAdapter?.setOnLoadMoreListener({ getData() }, rcv_reward)
        activity?.let { ContextCompat.getColor(it, R.color.app_color) }?.let { swipeRefreshLayout.setColorSchemeColors(it) }
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }
    }

    fun getData() {
        mPresenter?.getRankingList(page, pageSize)
    }



    override fun showLoading() {
        activity?.let {
            (activity as BaseActivity).showLoading()
        }
    }

    override fun hideLoading() {
        activity?.let {
            (activity as BaseActivity).hideLoading()
        }
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

    }

    override fun showRankingList(data: List<RewardDetailInfo>?) {
        if (page == 1) {
            rankingListAdapter?.setNewData(data)
        } else {
            data?.let { rankingListAdapter?.addData(it) }
        }

        if (data?.size == pageSize) {
            page++
            rankingListAdapter?.loadMoreComplete()
        } else {
            rankingListAdapter?.loadMoreEnd()
        }

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false

        }
    }

    override fun showBindInvitation(data: RewardInfo?) {

    }

    override fun onNoData() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false

        }

    }

    override fun onError() {

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false

        }
    }
}