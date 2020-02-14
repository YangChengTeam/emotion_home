package com.yc.emotion.home.mine.ui.fragment

import android.content.Intent
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kk.utils.ScreenUtil
import com.music.player.lib.util.ToastUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.index.ui.activity.TutorCourseDetailActivity
import com.yc.emotion.home.mine.adapter.CourseOrderAdapter
import com.yc.emotion.home.mine.ui.activity.PublishEvaluateActivity
import com.yc.emotion.home.model.bean.OrderInfo
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.base.view.IView

import com.yc.emotion.home.utils.ItemDecorationHelper
import kotlinx.android.synthetic.main.fragment_efficient_course.*

/**
 *
 * Created by suns  on 2019/10/17 10:25.
 */
class CourseOrderFragment : BaseLazyFragment<BasePresenter<IModel, IView>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_efficient_course
    }

    private var mLoadingDialog: LoadDialog? = null

    private var courseOrderAdapter: CourseOrderAdapter? = null
    private var mHandler: Handler? = null



    override fun initViews() {
        mLoadingDialog = LoadDialog(activity)
        mHandler = Handler()

        rv_efficient_course.setPadding(0, 0, 0, 0)
        activity?.let {

            ll_efficient_course.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.gray_f2f2f2))
            ll_efficient_course.setPadding(0, 0, 0, ScreenUtil.dip2px(activity, 30f))

        }
        val layoutManager = LinearLayoutManager(activity)
        courseOrderAdapter = CourseOrderAdapter(null)

        rv_efficient_course.layoutManager = layoutManager
        rv_efficient_course.adapter = courseOrderAdapter
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

        courseOrderAdapter?.setOnItemClickListener { adapter, view, position ->
            val orderinfo = courseOrderAdapter?.getItem(position)
            orderinfo?.let {

                startActivity(Intent(activity, TutorCourseDetailActivity::class.java))
            }
        }

        courseOrderAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val orderInfo = courseOrderAdapter?.getItem(position)
            orderInfo?.let {
                val llCourseOrderMore = courseOrderAdapter?.getViewByPosition(rv_efficient_course, position, R.id.ll_course_order_more)

                when {
                    view.id == R.id.tv_course_order_comment -> //todo 追加评价
                    {
                        ToastUtils.showCenterToast("评价")
                        startActivity(Intent(activity, PublishEvaluateActivity::class.java))
                    }
                    view.id == R.id.tv_course_apply_refund -> //todo 申请退款
                        ToastUtils.showCenterToast("退款")
                    view.id == R.id.iv_course_order_more -> {
                        var tag = llCourseOrderMore?.tag
                        tag = if (tag == "0") "1" else "0"
                        llCourseOrderMore?.tag = tag

                        llCourseOrderMore?.visibility = if (tag == "1") View.VISIBLE else View.GONE
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
            courseOrderAdapter?.setNewData(datas)
            mLoadingDialog?.dismissLoadingDialog()
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false

        }, 500)

    }

    override fun lazyLoad() {
        getData()
    }
}