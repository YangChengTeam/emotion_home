package com.yc.emotion.home.community.ui.fragment

import android.content.Context
import android.os.Handler
import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.fragment.common.SuccessFragment
import com.yc.emotion.home.community.adapter.CommunityAdapter
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.ui.activity.CommunityDetailActivity
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.mine.ui.fragment.ExitPublishFragment
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.event.CommunityPublishSuccess
import com.yc.emotion.home.utils.ItemDecorationHelper
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.fragment_community.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by suns  on 2019/8/28 09:17.
 */
class CommunityMyFragment : BaseFragment<CommunityPresenter>(), View.OnClickListener, CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


    private var communityAdapter: CommunityAdapter? = null

    private var page = 1
    private val PAGE_SIZE = 10

    private var mHandler: Handler? = null

    var mMainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_community
    }

    override fun initViews() {

        mPresenter = CommunityPresenter(activity, this)
        mHandler = Handler()


        initRecyclerView()
        tv_login_tint.text = Html.fromHtml("未登录？<font color='#FF2D55'>点击登录</font>")
    }



    private fun initRecyclerView() {
        rv_community.layoutManager = LinearLayoutManager(mMainActivity)
        communityAdapter = CommunityAdapter(null, true)
        rv_community.adapter = communityAdapter
        rv_community.addItemDecoration(ItemDecorationHelper(mMainActivity, 10))
        initListener()
    }

    private fun initListener() {

        mMainActivity?.let {
            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.red_crimson))
        }

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            initData()
        }


        communityAdapter?.setOnItemClickListener { adapter, view, position ->
            val communityInfo = communityAdapter?.getItem(position)
            if (null != communityInfo)
                CommunityDetailActivity.StartActivity(mMainActivity, getString(R.string.community_detail), communityInfo.topicId)
        }


        communityAdapter?.setOnLoadMoreListener({ this.getData() }, rv_community)

        communityAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val communityInfo = communityAdapter?.getItem(position)
            if (communityInfo != null) {

                when (view.id) {
                    R.id.iv_like, R.id.ll_like -> if (communityInfo.is_dig == 0) {//未点赞
                        like(communityInfo, position)
                    }
                    R.id.iv_del -> {

                        deleteConfirm(communityInfo, position)

                    }
                }//todo 删除自己的发帖
            }
        }
        ll_login_tint.clickWithTrigger { UserInfoHelper.instance.goToLogin(mMainActivity) }


    }


    private fun deleteConfirm(communityInfo: CommunityInfo?, position: Int) {

        val exitPublishFragment = ExitPublishFragment.newInstance("确定要删除吗")
        exitPublishFragment.setOnConfirmListener (object :ExitPublishFragment.OnConfirmListener{
            override fun onConfirm() {
                deleteTopic(communityInfo, position)
            }
        })
        exitPublishFragment.show(childFragmentManager, "")

    }

    private fun initData() {

        val userId = UserInfoHelper.instance.getUid() as Int
        if (userId <= 0) {

            ll_login_tint.visibility = View.VISIBLE
            rv_community.visibility = View.GONE
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            return
        }
        ll_login_tint.visibility = View.GONE
        rv_community.visibility = View.VISIBLE


        mPresenter?.getCommunityMyCache()

        getData()

    }

    override fun lazyLoad() {
        initData()
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
    fun publishSuccess(success: CommunityPublishSuccess) {
        page = 1
        getData()
        val successFragment = SuccessFragment()
        successFragment.setTint("发布成功")
        successFragment.show(childFragmentManager, "")
        mHandler?.postDelayed({ successFragment.dismiss() }, 1500)
    }

    fun getData() {
        mPresenter?.getMyCommunityInfos(page, PAGE_SIZE)

    }

    private fun deleteTopic(communityInfo: CommunityInfo?, position: Int) {

        mPresenter?.deleteTopic(communityInfo, position)


    }

    private fun createNewData(list: List<CommunityInfo>?) {

        if (page == 1) {
            communityAdapter?.setNewData(list)
        } else {
            list?.let {
                communityAdapter?.addData(it)
            }

        }

        if (list != null && list.size == PAGE_SIZE) {
            communityAdapter?.loadMoreComplete()
            page++
        } else {
            communityAdapter?.loadMoreEnd()
        }
    }


    private fun like(communityInfo: CommunityInfo, position: Int) {

        mPresenter?.likeTopic(communityInfo, position)

    }


    override fun showCommunityMyList(list: List<CommunityInfo>) {
        createNewData(list)
        top_empty_view.visibility = View.GONE
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }



    override fun showLoading() {
        mMainActivity?.showLoading()
    }

    override fun hideLoading() {
        mMainActivity?.hideLoading()
    }

    override fun showDeleteTopicSuccess(communityInfo: CommunityInfo?, position: Int) {
        val list = communityAdapter?.data
        list?.let {
            list.removeAt(position)
            if (list.size > 0) {
                communityAdapter?.notifyItemRemoved(position)
                top_empty_view.visibility = View.GONE
            } else {
                communityAdapter?.notifyDataSetChanged()
                top_empty_view.visibility = View.VISIBLE
            }
        }
    }


    override fun showLikeTopicSuccess(communityInfo: CommunityInfo, position: Int) {
        communityInfo.is_dig = 1
        communityInfo.like_num = communityInfo.like_num + 1
        communityAdapter?.notifyItemChanged(position)
    }

    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        page = 1
    }
}
