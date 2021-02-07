package com.yc.emotion.home.skill.ui.fragment

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.PracticeItemAdapter
import com.yc.emotion.home.index.presenter.PracticePresenter
import com.yc.emotion.home.index.ui.activity.LoveCaseDetailActivity
import com.yc.emotion.home.index.view.PracticeView
import com.yc.emotion.home.model.bean.MainT2Bean
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivity
import com.yc.emotion.home.skill.ui.activity.PromotionPlanActivity
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_practice_teach.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 实战教学
 */

class PracticeTeachFragment : BaseFragment<PracticePresenter>(), PracticeView {


    private var mMainT2Beans: MutableList<MainT2Bean>? = null

    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1

    private var mAdapter: PracticeItemAdapter? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_practice_teach
    }


    override fun initViews() {

        mPresenter = PracticePresenter(activity, this)

        initRecyclerView()
    }


    override fun lazyLoad() {

        mPresenter?.getCache()
    }

    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(activity)
        lchild_main_t2_t1_rl.layoutManager = layoutManager
        layoutManager.orientation = RecyclerView.VERTICAL
        //设置增加或删除条目的动画
        lchild_main_t2_t1_rl.itemAnimator = DefaultItemAnimator()
        mAdapter = PracticeItemAdapter(mMainT2Beans)
        lchild_main_t2_t1_rl.adapter = mAdapter


        initListener()

    }

    private fun initListener() {
        child_main_t2_t1_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        child_main_t2_t1_swipe_refresh.setOnRefreshListener {
            PAGE_NUM = 1
            netData(true)
        }
        mAdapter?.setOnLoadMoreListener({ netData(false) }, lchild_main_t2_t1_rl)
        mAdapter?.setOnItemClickListener { adapter, view, position ->

            val mainT2Bean = mAdapter?.getItem(position)
            if (mainT2Bean != null) {
                if (MainT2Bean.VIEW_ITEM == mainT2Bean.type) {
                    MobclickAgent.onEvent(activity, "practice_click", "实战教学点击")
                    LoveCaseDetailActivity.startExampleDetailActivity(activity, mainT2Bean.id, mainT2Bean.post_title)
                } else if (MainT2Bean.VIEW_TITLE == mainT2Bean.type) {
                    startActivity(Intent(activity, PromotionPlanActivity::class.java))
                    MobclickAgent.onEvent(activity, "promotion_plan_click", "情感星动力点击")
                } else {
                    if (!UserInfoHelper.instance.goToLogin(activity)) {
                        if (MainT2Bean.VIEW_TO_PAY_VIP == mainT2Bean.type || MainT2Bean.VIEW_VIP == mainT2Bean.type) {
                            MobclickAgent.onEvent(activity, "practice_pay_click", "实战教学付费点击")
                            startActivity(Intent(activity, BecomeVipActivity::class.java))
                        }
                    }
                }
            }
        }
    }


    private fun netData(isRefresh: Boolean) {
        mPresenter?.getPracticeInfos(PAGE_NUM, PAGE_SIZE, isRefresh)

    }


    override fun loadMoreComplete() {
        mAdapter?.loadMoreComplete()
        PAGE_NUM++
    }

    override fun loadMoreEnd() {
        mAdapter?.loadMoreEnd()
    }

    override fun onComplete() {
        child_main_t2_t1_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }

    }

    override fun onError() {
        child_main_t2_t1_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }
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
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        PAGE_NUM = 1
        netData(false)
    }

    override fun showPracticeInfoList(exampDataBean: List<MainT2Bean>) {
//        if (PAGE_NUM > 1) {
//            for (i in exampDataBean.indices) {
//                val item = exampDataBean[i]
////                if (i >= 10) {
//                item.post_title = "********************************";
////                }
//            }
//        }


        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(exampDataBean)
        } else {
            exampDataBean.let {
                mAdapter?.addData(exampDataBean)
            }
        }

        child_main_t2_t1_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }


    }


    override fun showLoading() {
        (activity as? BaseActivity)?.showLoading()
    }

    override fun hideLoading() {
        (activity as? BaseActivity)?.hideLoading()
    }


}
