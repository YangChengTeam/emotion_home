package com.yc.emotion.home.mine.ui.fragment

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.IndexChoicenessAdapter
import com.yc.emotion.home.index.ui.activity.ArticleDetailActivity
import com.yc.emotion.home.mine.presenter.CollectPresenter
import com.yc.emotion.home.mine.ui.activity.CollectActivity
import com.yc.emotion.home.mine.view.CollectView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import kotlinx.android.synthetic.main.fragment_collect_view.*

/**
 * 收藏 实例（文章）
 * Created by mayn on 2019/5/5.
 */

class CollectArticleFragment : BaseFragment<CollectPresenter>(), CollectView {



    private val PAGE_SIZE = 10
    private var PAGE_NUM = 1


    private var mAdapter: IndexChoicenessAdapter? = null


    private var mCollectActivity: CollectActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CollectActivity) {
            mCollectActivity = context
        }
    }

    override fun initBundle() {
        val arguments = arguments
        if (arguments != null) {
            val position = arguments.getInt("position")
            //            mCategoryId = arguments.getInt("category_id", -1);
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {

        mPresenter = CollectPresenter(activity, this)

        initRecyclerView()
        initListener()
    }



    private fun initListener() {

        mCollectActivity?.let {

            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.app_color))
        }

        swipeRefreshLayout.setOnRefreshListener {
            PAGE_NUM = 1
            netData()
        }


        mAdapter?.setOnLoadMoreListener({ netData() }, fragment_collect_love_healing_rv)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val exampListsBean = mAdapter?.getItem(position)
            if (exampListsBean != null)
                ArticleDetailActivity.startExampleDetailActivity(mCollectActivity, exampListsBean.id, exampListsBean.post_title)
        }
    }


    private fun initRecyclerView() {

        val layoutManager = LinearLayoutManager(mCollectActivity)
        fragment_collect_love_healing_rv.layoutManager = layoutManager
        mAdapter = IndexChoicenessAdapter(null, isIndex = false, isMore = true)
        fragment_collect_love_healing_rv.adapter = mAdapter
    }

    override fun lazyLoad() {
        netData()
    }


    private fun netData() {

        mPresenter?.getArticleCollectList(PAGE_NUM,PAGE_SIZE)

    }

    private fun initRecyclerViewData(mExampListsBeans: List<ArticleDetailInfo>?) {

        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mExampListsBeans)
        } else {
            mExampListsBeans?.let {
                mAdapter?.addData(mExampListsBeans)

            }
        }
        if (mExampListsBeans?.size == PAGE_SIZE) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }

    }

    override fun showCollectArticleList(mExampListsBeans: List<ArticleDetailInfo>?) {
        initRecyclerViewData(mExampListsBeans)
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }


    override fun showLoading() {
        mCollectActivity?.showLoading()
    }

    override fun hideLoading() {
        mCollectActivity?.hideLoading()
    }
}
