package com.yc.emotion.home.index.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.adapter.LoveByStagesAdapter
import com.yc.emotion.home.index.presenter.SkillPresenter
import com.yc.emotion.home.index.ui.activity.LoveArticleDetailsActivity
import com.yc.emotion.home.index.ui.activity.LoveByStagesActivity
import com.yc.emotion.home.index.view.SkillView
import com.yc.emotion.home.model.bean.LoveByStagesBean
import kotlinx.android.synthetic.main.fragment_love_by_stages.*

/**
 * Created by mayn on 2019/5/5.
 * 恋爱文章列表页
 */

class LoveArticleListFragment : BaseFragment<SkillPresenter>(), SkillView {


    private var mCategoryId: Int = 0

    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1
    private var mLoadingDialog: LoadDialog? = null


    private var mAdapter: LoveByStagesAdapter? = null
    private var mLoveByStagesActivity: LoveByStagesActivity? = null

    companion object {
        fun newInstance(categoryId: Int): LoveArticleListFragment {
            val fragment = LoveArticleListFragment()
            val args = Bundle()
            args.putInt("category_id", categoryId)
//            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoveByStagesActivity) {
            mLoveByStagesActivity = context
        }
    }

    override fun initBundle() {
        val arguments = arguments
        if (arguments != null) {
//            val position = arguments.getInt("position")
            mCategoryId = arguments.getInt("category_id", -1)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_love_by_stages
    }

    override fun initViews() {
        mPresenter = SkillPresenter(activity, this)

        mLoadingDialog = LoadDialog(activity)
        initRecyclerView()
        initListener()
    }


    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(mLoveByStagesActivity)
        fragment_love_by_stages_rv.layoutManager = layoutManager
        mAdapter = LoveByStagesAdapter(null)
        fragment_love_by_stages_rv.adapter = mAdapter

        fragment_love_by_stages_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        fragment_love_by_stages_swipe_refresh.setOnRefreshListener {
            PAGE_NUM = 1
            netData()
        }
    }

    override fun lazyLoad() {
        netData()
    }


    private fun netData() {

        mPresenter?.listsArticle("$mCategoryId", page = PAGE_NUM, pageSize = PAGE_SIZE)

    }

    private fun createNewData(loveByStagesBeans: List<LoveByStagesBean>) {
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(loveByStagesBeans)
        } else {
            mAdapter?.addData(loveByStagesBeans)
        }
        if (PAGE_SIZE == loveByStagesBeans.size) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }
        if (fragment_love_by_stages_swipe_refresh.isRefreshing) fragment_love_by_stages_swipe_refresh.isRefreshing = false

    }

    private fun initListener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            //                mLoveByStagesActivity.startActivity(new Intent(mLoveByStagesActivity, TestWebViewActivity.class));
            val loveByStagesBean = mAdapter?.getItem(position)
            if (loveByStagesBean != null)
                LoveArticleDetailsActivity.startLoveByStagesDetailsActivity(mLoveByStagesActivity, loveByStagesBean.id, loveByStagesBean.post_title)
        }
        mAdapter?.setOnLoadMoreListener({ this.netData() }, fragment_love_by_stages_rv)
    }




    override fun showLoading() {
        mLoveByStagesActivity?.showLoading()
    }

    override fun hideLoading() {
        mLoveByStagesActivity?.hideLoading()
    }

    override fun showSkillArticleList(data: List<LoveByStagesBean>?) {
        data?.let {
            createNewData(data)
        }
    }

    override fun onComplete() {
        if (fragment_love_by_stages_swipe_refresh.isRefreshing) fragment_love_by_stages_swipe_refresh.isRefreshing = false
    }


}
