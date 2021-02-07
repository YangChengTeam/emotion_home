package com.yc.emotion.home.mine.ui.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.mine.adapter.RewardDetailAdapter
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.view.RewardView
import kotlinx.android.synthetic.main.activity_order.*

/**
 *
 * Created by suns  on 2020/8/24 13:59.
 */
class RewardDetailFragment : BaseFragment<RewardPresenter>(), RewardView {
    private var rewardDetailAdapter: RewardDetailAdapter? = null
    private var page: Int = 1
    private val pageSize: Int = 10


    override fun getLayoutId(): Int {
        return R.layout.activity_order
    }

    override fun initViews() {

        mPresenter = RewardPresenter(activity, this)
        rcv_order.layoutManager = LinearLayoutManager(activity)

        rewardDetailAdapter = RewardDetailAdapter(null)
        rcv_order.adapter = rewardDetailAdapter

        initListener()
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



    fun initListener() {
        rewardDetailAdapter?.setOnLoadMoreListener({ getData() }, rcv_order)
        activity?.let { ContextCompat.getColor(it, R.color.app_color) }?.let { swipeRefreshLayout.setColorSchemeColors(it) }
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }
    }

    private fun getData() {
        (mPresenter as RewardPresenter).getRewardDetailInfoList(page, pageSize)
    }

    override fun showRewardInfo(data: RewardInfo?) {

    }

    override fun showBindSuccess() {

    }

    override fun showRewardDetailList(data: List<RewardDetailInfo>?) {
        top_empty_view.visibility = View.GONE
        if (page == 1) {
            rewardDetailAdapter?.setNewData(data)
        } else {
            data?.let {
                rewardDetailAdapter?.addData(data)
            }
        }

        if (data?.size == pageSize) {
            rewardDetailAdapter?.loadMoreComplete()
            page++
        } else {
            rewardDetailAdapter?.loadMoreEnd()
        }

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }

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


    override fun onNoData() {
        super.onNoData()
        top_empty_view.visibility = View.VISIBLE
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

    override fun lazyLoad() {
        getData()
    }
}