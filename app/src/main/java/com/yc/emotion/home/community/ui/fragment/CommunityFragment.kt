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
import kotlinx.android.synthetic.main.fragment_community.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by suns  on 2019/8/28 09:17.
 * 最新动态
 */
class CommunityFragment : BaseFragment<CommunityPresenter>(), View.OnClickListener, CommunityView {


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
        return R.layout.fragment_community
    }

    override fun initViews() {

        mPresenter = CommunityPresenter(activity, this)


        initRecyclerView()
    }


    private fun initRecyclerView() {
        rv_community.layoutManager = LinearLayoutManager(mMainActivity)
        communityAdapter = CommunityAdapter(null, false)
        rv_community.adapter = communityAdapter
        rv_community.addItemDecoration(ItemDecorationHelper(mMainActivity, 10))

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
            if (null != communityInfo)
                CommunityDetailActivity.StartActivity(mMainActivity, getString(R.string.community_detail), communityInfo.topicId)
        }

        communityAdapter?.setOnLoadMoreListener({ this.getData() }, rv_community)

        communityAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val communityInfo = communityAdapter?.getItem(position)
            if (communityInfo != null) {

                when (view.id) {
                    R.id.iv_like, R.id.ll_like ->
                        //                        ImageView iv = view.findViewById(R.id.iv_like);
                        if (communityInfo.is_dig == 0) {//未点赞
                            //                            TextView textView = communityAdapter.getView(position);
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

    private fun initData() {


        getData()
    }

    override fun lazyLoad() {
        initData()
        initListener()
    }

    fun getData() {

        mPresenter?.getCommunityNewstInfos(page, PAGE_SIZE)

    }

    private fun createNewData(list: List<CommunityInfo>?) {

        if (page == 1) {
//            CommonInfoHelper.setO<List<CommunityInfo>>(mMainActivity, list, "emotion_community_newst_data")
            communityAdapter?.setNewData(list)
        } else {
            list?.let {
                communityAdapter?.addData(list)
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
//        val id = UserInfoHelper.instance.getUid() as Int

        page = 1

        mPresenter?.getCommunityTagListInfo(communityTag.communityTagInfo.id, page, PAGE_SIZE)

    }

    override fun onClick(v: View) = Unit


    override fun showLoading() {
        mMainActivity?.showLoading()
    }

    override fun hideLoading() {
        mMainActivity?.hideLoading()
    }

    private var isFromNet = false//是否从网络中获取了数据
    override fun shoCommunityNewestInfos(datas: List<CommunityInfo>?) {
        isFromNet = true
        createNewData(datas)
    }

    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {
        if (!isFromNet) createNewData(datas)
    }

    override fun onComplete() {
        swipeRefreshLayout?.let {
            if (it.isRefreshing) it.isRefreshing = false
        }

    }

    override fun onError() {
        swipeRefreshLayout?.let {
            if (it.isRefreshing) it.isRefreshing = false
        }

    }

    override fun showLikeTopicSuccess(communityInfo: CommunityInfo, position: Int) {
        communityInfo.is_dig = 1
        communityInfo.like_num = communityInfo.like_num + 1
        communityAdapter?.notifyItemChanged(position)
    }


    override fun onDestroy() {
        super.onDestroy()
        page = 1
    }
}
