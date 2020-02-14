package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.mine.adapter.OrderAdapter
import com.yc.emotion.home.mine.presenter.OrderPresenter
import com.yc.emotion.home.mine.view.OrderView
import com.yc.emotion.home.model.bean.OrderInfo
import com.yc.emotion.home.utils.ItemDecorationHelper
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_order_new.*

import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/17 09:34.
 */
class OrderActivityNew : BaseSameActivity(), OrderView {


    override fun getLayoutId(): Int {
        return R.layout.activity_order_new
    }

    private var orderAdapter: OrderAdapter? = null

    private var page = 1
    private val PAGE_SIZE = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }


    override fun initViews() {

        mPresenter = OrderPresenter(this, this)

        rcv_order.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(null)

        rcv_order.adapter = orderAdapter
        rcv_order.addItemDecoration(ItemDecorationHelper(this, 10))
        getData()
        initListener()
    }

    private fun initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }

        orderAdapter?.setOnLoadMoreListener({
            getData()
        }, rcv_order)

//        orderAdapter?.setOnItemClickListener { adapter, view, position -> }
    }

    private fun getData() {

        (mPresenter as? OrderPresenter)?.getOrderList(page, PAGE_SIZE)

    }

    private fun createNewData(list: List<OrderInfo>?) {

        top_empty_view.visibility = View.GONE

        if (page == 1) {
            orderAdapter?.setNewData(list)
        } else {
            list?.let {
                orderAdapter?.addData(list)
            }
        }
        if (list != null && list.size == PAGE_SIZE) {
            orderAdapter?.loadMoreComplete()
            page++
        } else {
            orderAdapter?.loadMoreEnd()
        }

    }

    override fun showOrderList(data: List<OrderInfo>?) {
        createNewData(data)
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false

    }

    override fun onError() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return "我的订单"
    }
}