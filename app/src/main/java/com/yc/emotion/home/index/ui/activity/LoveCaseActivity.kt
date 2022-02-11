package com.yc.emotion.home.index.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.PracticeItemAdapter
import com.yc.emotion.home.index.presenter.LoveCasePresenter
import com.yc.emotion.home.index.view.LoveCaseView
import com.yc.emotion.home.model.bean.MainT2Bean
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivityNew
import com.yc.emotion.home.utils.CommonInfoHelper
import kotlinx.android.synthetic.main.activity_love_case.*

class LoveCaseActivity : BaseSameActivity(), LoveCaseView {


    private var mMainT2Beans: List<MainT2Bean>? = null

    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1

    private var mAdapter: PracticeItemAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        mPresenter = LoveCasePresenter(this, this)

        initViews()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_love_case
    }

    override fun initViews() {
        initData()
        love_case_iv_to_wx.setOnClickListener(this)
        initRecyclerView()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.love_case_iv_to_wx -> showToWxServiceDialog()
        }
    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        //        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        love_case_rl.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //设置增加或删除条目的动画
        love_case_rl.itemAnimator = DefaultItemAnimator()
        mAdapter = PracticeItemAdapter(null)
        love_case_rl.adapter = mAdapter


        love_case_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        love_case_swipe_refresh.setOnRefreshListener {
            PAGE_NUM = 1
            netData()
        }
        initListener()
    }

    private fun initListener() {
        mAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val mainT2Bean = mAdapter?.getItem(position)
            if (mainT2Bean != null) {
                if (MainT2Bean.VIEW_ITEM == mainT2Bean.type) {
                    LoveCaseDetailActivity.startExampleDetailActivity(this@LoveCaseActivity, mainT2Bean.id, mainT2Bean.post_title)
                } else if (MainT2Bean.VIEW_TO_PAY_VIP == mainT2Bean.type || MainT2Bean.VIEW_VIP == mainT2Bean.type) {
                    startActivity(Intent(this@LoveCaseActivity, BecomeVipActivityNew::class.java))
                }
            }
        }

        mAdapter?.setOnLoadMoreListener({ netData() }, love_case_rl)


    }

    private fun initData() {
        (mPresenter as? LoveCasePresenter)?.getLoveCaseCache()
        netData()
    }

    private fun netData() {
        (mPresenter as? LoveCasePresenter)?.exampLists(PAGE_NUM, PAGE_SIZE)


    }

    override fun showLoveCaseList(data: List<MainT2Bean>?) {
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mMainT2Beans)
            CommonInfoHelper.setO(this, mMainT2Beans, "main2_example_lists")
        } else {
            mMainT2Beans?.let {
                mAdapter?.addData(it)
            }
        }

        if (love_case_swipe_refresh.isRefreshing) love_case_swipe_refresh.isRefreshing = false
    }

    override fun onComplete() {
        if (love_case_swipe_refresh.isRefreshing) love_case_swipe_refresh.isRefreshing = false
    }

    override fun onError() {
        if (love_case_swipe_refresh.isRefreshing) love_case_swipe_refresh.isRefreshing = false

    }

    override fun loadMoreComplete() {
        mAdapter?.loadMoreComplete()
        PAGE_NUM++
    }

    override fun loadEnd() {
        mAdapter?.loadMoreEnd()
    }

    override fun offerActivityTitle(): String {
        return "实战学习"
    }
}
