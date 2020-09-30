package com.yc.emotion.home.message.ui.fragment

import android.content.Intent
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.ConfessionAdapter
import com.yc.emotion.home.index.presenter.ExpressPresenter
import com.yc.emotion.home.index.ui.activity.CreateBeforeActivity
import com.yc.emotion.home.index.view.ExpressView
import com.yc.emotion.home.model.bean.confession.ConfessionDataBean
import com.yc.emotion.home.skill.ui.activity.PromotionPlanActivity
import kotlinx.android.synthetic.main.activity_express.*

//表白
class ExpressFragment : BaseFragment<ExpressPresenter>(), ExpressView {


    private val PAGE_SIZE = 20
    private var PAGE_NUM = 1
    private var mAdapter: ConfessionAdapter? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_express
    }

    override fun initViews() {
        mPresenter = ExpressPresenter(activity, this)
        initRecyclerView()

        initListener()
    }

    override fun lazyLoad() {
        initData()
    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(activity)
        express_rl.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        //设置增加或删除条目的动画
        express_rl.itemAnimator = DefaultItemAnimator()
        mAdapter = ConfessionAdapter(null)
        express_rl.adapter = mAdapter
    }

    private fun initListener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val confessionDataBean = mAdapter?.getItem(position)

            confessionDataBean?.let {
                if (ConfessionDataBean.VIEW_ITEM == confessionDataBean.itemType) {
                    CreateBeforeActivity.startCreateBeforeActivity(activity, confessionDataBean)
                    MobclickAgent.onEvent(activity, "chat_confession_click", "表白神器点击")
                }else if (ConfessionDataBean.VIEW_TITLE==confessionDataBean.itemType){
                    startActivity(Intent(activity, PromotionPlanActivity::class.java))
                    MobclickAgent.onEvent(activity,"promotion_plan_click","情感星动力点击")
                }
            }


        }
        mAdapter?.setOnLoadMoreListener({ this.getData() }, express_rl)
        swipeRefreshLayout.setColorSchemeResources(R.color.red_crimson)
        swipeRefreshLayout.setOnRefreshListener {
            PAGE_NUM = 1
            getData()
        }
    }

    private fun initData() {

        mPresenter?.getExpressCache()

        getData()

    }

    private fun getData() {
        mPresenter?.getExpressData(PAGE_NUM, PAGE_SIZE)
    }


    override fun loadEnd() {
        mAdapter?.loadMoreEnd()
    }

    override fun loadMoreComplete() {
        mAdapter?.loadMoreComplete()
        PAGE_NUM++

    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onError() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showConfessionInfos(mConfessionDataBeans: List<ConfessionDataBean>) {
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mConfessionDataBeans)

        } else {

            mAdapter?.addData(mConfessionDataBeans)
        }
    }


    override fun showLoadingDialog() {
        (activity as? BaseActivity)?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        (activity as? BaseActivity)?.hideLoadingDialog()
    }

}
