package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.LoveHealDetailsAdapter
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_love_heal_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoveHealDetailsActivity : BaseSameActivity(), IndexVerbalView {


    private var mTitle: String? = null
    private var mCategoryId: String? = null

    private var mAdapter: LoveHealDetailsAdapter? = null

    private val PAGE_SIZE = 8
    private var PAGE_NUM = 1
    private var mLoveHealDetBeans: List<LoveHealDetBean>? = null


    override fun initIntentData() {
        val intent = intent
        mTitle = intent.getStringExtra("title")
        mCategoryId = intent.getStringExtra("category_id")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()

    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun initData() {
        netData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_love_heal_details
    }


    override fun initViews() {

        mPresenter = IndexVerbalPresenter(this, this)
        initData()


        love_heal_details_iv_to_wx.setOnClickListener(this)
        initRecyclerView()
        initListener()
    }

    private fun initListener() {
        love_heal_details_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        love_heal_details_swipe_refresh.setOnRefreshListener {


            PAGE_NUM = 1
            netData()
        }
        mAdapter?.setOnLoadMoreListener({ this.netData() }, love_heal_details_rl)

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val loveHealDetBean = mAdapter?.getItem(position)
            if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                if (!UserInfoHelper.instance.goToLogin(this@LoveHealDetailsActivity))
                //TODO 购买VIP刷新数据
                    startActivity(Intent(this@LoveHealDetailsActivity, VipActivity::class.java))
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val loveHealDetBean = mAdapter?.getItem(position)
            if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                if (!UserInfoHelper.instance.goToLogin(this@LoveHealDetailsActivity))
                //TODO 购买VIP刷新数据
                    startActivity(Intent(this@LoveHealDetailsActivity, VipActivity::class.java))
        }

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.love_heal_details_iv_to_wx -> showToWxServiceDialog()
        }
    }

    private fun initRecyclerView() {


        val layoutManager = LinearLayoutManager(this)
        love_heal_details_rl.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //设置增加或删除条目的动画
        love_heal_details_rl.itemAnimator = DefaultItemAnimator()
        mAdapter = LoveHealDetailsAdapter(mLoveHealDetBeans, mTitle)
        love_heal_details_rl.adapter = mAdapter


    }

    private fun netData() {

        (mPresenter as? IndexVerbalPresenter)?.loveListCategory(mCategoryId, page = PAGE_NUM, page_size = PAGE_SIZE)

    }

    private fun createNewData(loveHealDetBeans: List<LoveHealDetBean>?) {

        if (loveHealDetBeans != null && loveHealDetBeans.isNotEmpty()) {
            for (loveHealDetBean in loveHealDetBeans) {
                if (loveHealDetBean.is_vip == 0) {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_ITEM
                } else {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_VIP
                }

            }
        }
        mLoveHealDetBeans = loveHealDetBeans
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mLoveHealDetBeans)
        } else {
            mLoveHealDetBeans?.let {

                mAdapter?.addData(it)
            }
        }
        if (loveHealDetBeans != null && loveHealDetBeans.size == PAGE_SIZE) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }


    }

    override fun onComplete() {
        if (love_heal_details_swipe_refresh.isRefreshing)
            love_heal_details_swipe_refresh.isRefreshing = false

    }

    override fun showVerbalDetailInfos(data: List<LoveHealDetBean>?) {
        createNewData(data)
    }

    override fun offerActivityTitle(): String? {
        return mTitle
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        netData()
    }

    companion object {

        fun startLoveHealDetailsActivity(context: Context, title: String, categoryId: String) {
            val intent = Intent(context, LoveHealDetailsActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("category_id", categoryId)
            context.startActivity(intent)
        }
    }


}
