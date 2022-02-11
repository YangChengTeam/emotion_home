package com.yc.emotion.home.index.ui.fragment

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.listener.OnUserInfoListener
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.LoveHealDetailsAdapter
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.ui.activity.SearchActivity
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean
import com.yc.emotion.home.model.bean.SearchDialogueBean
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.bean.event.EventLoginState
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivityNew
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_base_share.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by mayn on 2019/5/5.
 */

class SearchFragment : BaseFragment<IndexVerbalPresenter>(), IndexVerbalView {


    private var mShareActivity: SearchActivity? = null


    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1
    private var i = 0

    private var mAdapter: LoveHealDetailsAdapter? = null

    private var keyword: String? = null

    private var uid: Int = 0
    private var vipTips: Int? = 0

    override fun onAttach(context: Context) {
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



        initRecyclerView()
        initListener()

        uid = UserInfoHelper.instance.getUid()
        vipTips = UserInfoHelper.instance.getUserInfo()?.vip_tips
        netData(keyword)


    }


    private fun initListener() {
        base_share_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        base_share_swipe_refresh.setOnRefreshListener {
            //                obtainWalletData();
            if (!TextUtils.isEmpty(keyword)) {
                PAGE_NUM = 1
                i = 0
                netData(keyword)
            }
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val item = mAdapter?.getItem(position)
            item?.let {
                when (it.type) {
                    LoveHealDetBean.VIEW_VIP ->
                        if (!UserInfoHelper.instance.goToLogin(mShareActivity)) {
                            startActivity(Intent(mShareActivity, BecomeVipActivityNew::class.java))
                        }

                    else -> {
                    }
                }
            }
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = mAdapter?.getItem(position)
            item?.let {
                when (it.type) {
                    LoveHealDetBean.VIEW_VIP -> {
                        if (view.id == R.id.tv_look) {
                            if (!UserInfoHelper.instance.goToLogin(mShareActivity)) {
                                startActivity(Intent(mShareActivity, BecomeVipActivityNew::class.java))
                            }
                        }
                    }
                }

            }
        }
        mAdapter?.setOnLoadMoreListener({
//            if (!UserInfoHelper.instance.goToLogin(mShareActivity))
            netData(keyword)
        }, base_share_rv)
    }

    override fun lazyLoad() {

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventLoginState) {
        when (event.state) {
            EventLoginState.STATE_LOGINED -> {
                PAGE_NUM = 1
                i = 0
                mShareActivity?.getUserInfo(listener = object : OnUserInfoListener {
                    override fun onUserInfo(userInfo: UserInfo) {
                        uid = userInfo.id
                        vipTips = userInfo.vip_tips

                    }
                })

                netData(keyword)
            }


        }
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


//        val uid = UserInfoHelper.instance.getUid()
//        if (uid == 0) {//未登录
//            mPresenter?.searchVerbalTalk(keyword, PAGE_NUM, PAGE_SIZE)
//        } else {
//
//        }
        mPresenter?.searchVerbalTalk(keyword, PAGE_NUM, PAGE_SIZE)
//        mShareActivity?.let {

//            if (!UserInfoHelper.instance.goToLogin(mShareActivity)) {


//            }
//        }
    }

    private fun initRecyclerData(loveHealDetBeans: List<LoveHealDetBean>) {

        val mLoveHealDetBeans = addTitle(loveHealDetBeans)
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mLoveHealDetBeans)
        } else {
            mAdapter?.addData(mLoveHealDetBeans)
        }

//        val uid = UserInfoHelper.instance.getUid()
//        if (uid == 0) {
//            if (loveHealDetBeans.size >= 5) {
//                mAdapter?.loadMoreComplete()
//                PAGE_NUM++
//            } else {
//                mAdapter?.loadMoreEnd()
//            }
//        } else {

        if (loveHealDetBeans.size == PAGE_SIZE) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }
//        }

    }


    private fun addTitle(loveHealDetBeans: List<LoveHealDetBean>): List<LoveHealDetBean> {

        for (loveHealDetBean in loveHealDetBeans) {
            var details: MutableList<LoveHealDetDetailsBean>? = loveHealDetBean.details
            if (details == null || details.size == 0) {
                details = loveHealDetBean.detail
            }


            details?.add(0, LoveHealDetDetailsBean(0, loveHealDetBean.id, loveHealDetBean.chat_name, loveHealDetBean.ans_sex))
            if (uid == 0) {//未登录
                if (i < 3) {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_ITEM
                } else {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_VIP
                }

            } else {//已登录
                if (i < 3 || vipTips == 1) {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_ITEM
                } else {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_VIP
                }
            }
            i += 1
        }
        return loveHealDetBeans
    }


    override fun showLoading() {
        mShareActivity?.showLoading()
    }

    override fun hideLoading() {
        mShareActivity?.hideLoading()
    }

    override fun showSearchResult(searchDialogueBean: SearchDialogueBean?, keyword: String?) {
        searchDialogueBean?.let {
//            val searchBuyVip = searchDialogueBean.search_buy_vip
//            if (1 == searchBuyVip) { //1 弹窗 0不弹
////                if (!UserInfoHelper.instance.goToLogin(mShareActivity)) {
//                    startActivity(Intent(mShareActivity, VipActivity::class.java))
////                    base_share_rv.postDelayed({ mShareActivity?.childDisposeOnBack() }, 1000)
////                }
//                //                    notCanShart();
//                return
//            }
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

