package com.yc.emotion.home.mine.ui.fragment

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kk.utils.ScreenUtil

import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.mine.adapter.ServiceOrderAdapter
import com.yc.emotion.home.model.bean.OrderInfo
import com.yc.emotion.home.utils.ItemDecorationHelper
import com.yc.emotion.home.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_efficient_course.*


/**
 *
 * Created by suns  on 2019/10/17 10:28.
 */
class ServiceOrderFragment : BaseFragment<BasePresenter<IModel, IView>>() {
    private var mLoadingDialog: LoadDialog? = null

    private var serviceOrderAdapter: ServiceOrderAdapter? = null
    private var mHandler: Handler? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_efficient_course
    }

    override fun initViews() {
        mLoadingDialog = LoadDialog(activity)
        mHandler = Handler()

        rv_efficient_course.setPadding(0, 0, 0, 0)
        activity?.let {

            ll_efficient_course.setBackgroundColor(ContextCompat.getColor(it, R.color.gray_f2f2f2))
            ll_efficient_course.setPadding(0, 0, 0, ScreenUtil.dip2px(activity, 30f))

        }
        val layoutManager = LinearLayoutManager(activity)
        serviceOrderAdapter = ServiceOrderAdapter(null)

        rv_efficient_course.layoutManager = layoutManager
        rv_efficient_course.adapter = serviceOrderAdapter
        rv_efficient_course.addItemDecoration(ItemDecorationHelper(activity, 10))

        initListener()

    }


    fun initListener() {
        val mActivity = activity
        mActivity?.let {
            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.app_color))
        }


        swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        serviceOrderAdapter?.setOnItemClickListener { adapter, view, position ->
            //            startActivity(Intent(activity, EmotionTestDescActivity::class.java))
        }

        serviceOrderAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val orderInfo = serviceOrderAdapter?.getItem(position)
            orderInfo?.let {
                val llServiceMore = serviceOrderAdapter?.getViewByPosition(rv_efficient_course, position, R.id.ll_service_order_more)

                when (view.id) {
                    R.id.tv_service_order_comment -> //todo 追加评价
                        ToastUtils.showCenterToast("评价")
                    R.id.tv_service_apply_refund -> //todo 申请退款
                        ToastUtils.showCenterToast("退款")
                    R.id.iv_service_order_more -> {
                        var tag = llServiceMore?.tag
                        tag = if (tag == "0") "1" else "0"
                        llServiceMore?.tag = tag
                        llServiceMore?.visibility = if (tag == "1") View.VISIBLE else View.GONE
                    }
                }
            }
        }

    }


    fun getData() {

        mLoadingDialog?.showLoadingDialog()
        mHandler?.postDelayed({
            val datas = arrayListOf<OrderInfo>()
            for (i in 0..5) {
                datas.add(OrderInfo())
            }
            serviceOrderAdapter?.setNewData(datas)
            mLoadingDialog?.dismissLoadingDialog()
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false

        }, 500)

    }

    override fun lazyLoad() {
        getData()
    }


}