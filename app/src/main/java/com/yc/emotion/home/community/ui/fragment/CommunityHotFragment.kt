package com.yc.emotion.home.community.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.community.adapter.CommunityAdapter
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.ui.activity.CommunityDetailActivity
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.event.EventCommunityTag
import com.yc.emotion.home.utils.ItemDecorationHelper
import kotlinx.android.synthetic.main.fragment_community_hot.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by suns  on 2019/8/28 09:17.
 */
class CommunityHotFragment : BaseFragment<CommunityPresenter>(), View.OnClickListener, CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


    private var communityAdapter: CommunityAdapter? = null

    private var page = 1
    private val PAGE_SIZE = 10


    private var mMainActivity: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_community_hot
    }


    override fun initViews() {

        mPresenter = CommunityPresenter(activity, this)

        initRecyclerView()

    }

    private fun initRecyclerView() {
        rv_hot_community.layoutManager = LinearLayoutManager(mMainActivity)
        communityAdapter = CommunityAdapter(null, false)

        rv_hot_community.adapter = communityAdapter
        rv_hot_community.addItemDecoration(ItemDecorationHelper(mMainActivity, 10))


    }

    private fun initListener() {
        mMainActivity?.let {

            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.red_crimson))
        }
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }

        communityAdapter?.setOnItemClickListener { adapter, view, position ->
            val communityInfo = communityAdapter?.getItem(position)
            if (communityInfo != null) {
                //                if (CommunityInfo.ITEM_TOP_ACTIVITY == communityInfo.itemType) {
                //
                //                } else if (CommunityInfo.ITEM_TAG == communityInfo.itemType) {
                //                    // TODO: 2019/8/30 社区标签
                //
                //                    CommunityTagListActivity.StartActivity(mMainActivity, communityInfo.tagInfo.getTitle(), communityInfo.tagInfo.getId());
                //
                //                } else
                if (CommunityInfo.ITEM_CONTENT == communityInfo.itemType) {
                    CommunityDetailActivity.StartActivity(mMainActivity, getString(R.string.community_detail), communityInfo.topicId)
                }
            }


        }
        communityAdapter?.setOnLoadMoreListener({ this.getData() }, rv_hot_community)
        communityAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val communityInfo = communityAdapter?.getItem(position)

            if (communityInfo != null) {
                if (CommunityInfo.ITEM_CONTENT == communityInfo.itemType) {
                    when (view.id) {
                        R.id.iv_like, R.id.ll_like ->

                            if (communityInfo.is_dig == 0) {//未点赞

                                like(communityInfo, position)
                            }
                        R.id.iv_avator -> {
                            val communityFaceFragment = CommunityFaceFragment()
                            val bundle = Bundle()
                            bundle.putString("name", communityInfo.name)
                            bundle.putString("face", communityInfo.pic)
                            communityFaceFragment.arguments = bundle
                            communityFaceFragment.show(childFragmentManager, "")
                        }
                    }
                }

            }
        }

    }

    private fun like(communityInfo: CommunityInfo, position: Int) {

        mPresenter?.likeTopic(communityInfo, position)


    }

    override fun lazyLoad() {
        //        getTagInfo();
        initData()
        initListener()
    }

    private fun initData() {

        mPresenter?.getCommunityHotCache()
        getData()
    }


    fun getData() {

        mPresenter?.getCommunityHotList(page, PAGE_SIZE)

    }

    private fun createNewData(communityInfos: List<CommunityInfo>?) {
//

        if (page == 1) {
            communityAdapter?.setNewData(communityInfos)
        } else {
            communityInfos?.let {

                communityAdapter?.addData(communityInfos)
            }
        }

        if (communityInfos?.size == PAGE_SIZE) {
            communityAdapter?.loadMoreComplete()
            page++
        } else {
            communityAdapter?.loadMoreEnd()
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
    fun getNewDataByTag(communityTag: EventCommunityTag) {

        page = 1
        mPresenter?.getCommunityTagListInfo(communityTag.communityTagInfo.id, page, PAGE_SIZE)

    }


    override fun showLoadingDialog() {
        mMainActivity?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mMainActivity?.hideLoadingDialog()
    }


    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }


    override fun onError() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showCommunityHotList(list: List<CommunityInfo>) {
        createNewData(list)
    }

    override fun showLikeTopicSuccess(communityInfo: CommunityInfo, position: Int) {
        communityInfo.is_dig = 1
        val likeNum = communityInfo.like_num + 1
        communityInfo.like_num = likeNum
        communityAdapter?.notifyItemChanged(position)
    }

    override fun shoCommunityNewestInfos(datas: List<CommunityInfo>?) {
        createNewData(datas)
    }

    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        page = 1
    }

}
