package com.yc.emotion.home.index.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yc.emotion.home.R
import com.yc.emotion.home.index.adapter.LoveHealDetailsAdapter
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.index.ui.activity.SearchActivity
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.base.domain.engine.MySubscriber
import com.yc.emotion.home.model.bean.AResultInfo
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.model.bean.SearchDialogueBean
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_base_share.*

import rx.Observer

/**
 * Created by mayn on 2019/5/5.
 */

class SearchFragment : BaseLazyFragment<IndexVerbalPresenter>(), IndexVerbalView {


    var mShareActivity: SearchActivity? = null

    private var mLoveEngin: LoveEngine? = null
    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1


    private var mAdapter: LoveHealDetailsAdapter? = null

    private var keyword: String? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchActivity) {
            mShareActivity = context
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_base_share
    }


    override fun initViews() {

        keyword = arguments?.getString("keyword")

        mPresenter = IndexVerbalPresenter(mShareActivity, this)



        mLoveEngin = LoveEngine(activity)

        initRecyclerView()
        initListener()
        netData(keyword)

    }

    private fun initListener() {
        base_share_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        base_share_swipe_refresh.setOnRefreshListener {
            //                obtainWalletData();
            if (!TextUtils.isEmpty(keyword)) {
                PAGE_NUM = 1
                netData(keyword)
            }
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->

        }
        mAdapter?.setOnLoadMoreListener({ netData(keyword) }, base_share_rv)
    }

    override fun lazyLoad() {

    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(activity)
        base_share_rv.layoutManager = layoutManager
        mAdapter = LoveHealDetailsAdapter(null, getString(R.string.search))
        base_share_rv.adapter = mAdapter

    }

    /**
     * @param keyword
     */
    fun netData(keyword: String?) {
        this.keyword = keyword


        mShareActivity?.let {

            if (!UserInfoHelper.instance.goToLogin(mShareActivity)) {
                mPresenter.searchVerbalTalk(keyword, PAGE_NUM, PAGE_SIZE)

            }
        }
    }

    private fun initRecyclerData(loveHealDetBeans: List<LoveHealDetBean>) {

        val mLoveHealDetBeans = addTitle(loveHealDetBeans)
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mLoveHealDetBeans)
        } else {
            mAdapter?.addData(mLoveHealDetBeans)
        }
        if (loveHealDetBeans.size == PAGE_SIZE) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }

    }

    private fun addTitle(loveHealDetBeans: List<LoveHealDetBean>): List<LoveHealDetBean> {
        for (loveHealDetBean in loveHealDetBeans) {
            var details: MutableList<LoveHealDetDetailsBean>? = loveHealDetBean.details
            if (details == null || details.size == 0) {
                details = loveHealDetBean.detail
            }
            details?.add(0, LoveHealDetDetailsBean(0, loveHealDetBean.id, loveHealDetBean.chat_name, loveHealDetBean.ans_sex))
        }
        return loveHealDetBeans
    }

    override fun showLoadingDialog() {
        mShareActivity?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mShareActivity?.hideLoadingDialog()
    }

    override fun showSearchResult(searchDialogueBean: SearchDialogueBean?, keyword: String?) {
        searchDialogueBean?.let {
            val searchBuyVip = searchDialogueBean.search_buy_vip
            if (1 == searchBuyVip) { //1 弹窗 0不弹
                startActivity(Intent(mShareActivity, VipActivity::class.java))
                base_share_rv.postDelayed({ mShareActivity?.childDisposeOnBack() }, 1000)

                //                    notCanShart();
                return
            }
            val mLoveHealDetBeans = searchDialogueBean.list

            initRecyclerData(mLoveHealDetBeans)
        }

    }

    override fun onComplete() {
        if (base_share_swipe_refresh.isRefreshing)
            base_share_swipe_refresh.isRefreshing = false
    }

    override fun onError() {
        if (base_share_swipe_refresh.isRefreshing)
            base_share_swipe_refresh.isRefreshing = false
    }
}

