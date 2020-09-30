package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.adapter.DisposeDetailAdapter
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo
import com.yc.emotion.home.mine.domain.bean.RewardInfo
import com.yc.emotion.home.mine.presenter.RewardPresenter
import com.yc.emotion.home.mine.view.RewardView
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.fragment_live_permisson.*

/**
 *
 * Created by suns  on 2020/8/24 19:39.
 */
class DisposeDetailActivity : BaseSameActivity(), RewardView {
    private var page: Int = 1
    private val pageSize = 10

    private var disposeDetailAdapter: DisposeDetailAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun offerActivityTitle(): String? {
        return "提现明细"
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_order
    }

    override fun initViews() {
        super.initViews()
        mPresenter = RewardPresenter(this, this)
        rcv_order.layoutManager = LinearLayoutManager(this)
        disposeDetailAdapter = DisposeDetailAdapter(null)
        rcv_order.adapter = disposeDetailAdapter
        getData()
        initListener()

    }

    private fun initListener() {
        disposeDetailAdapter?.setOnLoadMoreListener({ getData() }, rcv_order)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }
    }

    private fun getData() {
        (mPresenter as RewardPresenter).getDisposeDetailInfoList(page, pageSize)
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
        top_empty_view.visibility = View.GONE
        if (page == 1) {
            disposeDetailAdapter?.setNewData(data)
        } else {
            data?.let {
                disposeDetailAdapter?.addData(data)
            }
        }
        if (data?.size == pageSize) {
            disposeDetailAdapter?.loadMoreComplete()
            page++
        } else {
            disposeDetailAdapter?.loadMoreEnd()
        }

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }

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
}