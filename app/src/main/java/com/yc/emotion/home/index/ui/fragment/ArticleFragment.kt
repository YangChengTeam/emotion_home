package com.yc.emotion.home.index.ui.fragment

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.IndexChoicenessAdapter
import com.yc.emotion.home.index.presenter.ArticlePresenter
import com.yc.emotion.home.index.ui.activity.ArticleDetailActivity
import com.yc.emotion.home.index.view.ArticleView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import kotlinx.android.synthetic.main.fragment_efficient_course.*

/**
 *
 * Created by suns  on 2019/10/23 17:04.
 */
class ArticleFragment : BaseFragment<ArticlePresenter>(), ArticleView {


    var indexChoicenessAdapter: IndexChoicenessAdapter? = null

    var page = 1
    var pageSize = 10
    private var catId = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_efficient_course
    }


    override fun initViews() {

        mPresenter = ArticlePresenter(activity, this)


        val intent = arguments
        intent?.let {
            catId = intent.getInt("cat_id", 0)
        }
        
        rv_efficient_course.setPadding(0, 0, 0, 0)
        val layoutManager = LinearLayoutManager(activity)
        indexChoicenessAdapter = IndexChoicenessAdapter(null, false, isMore = true)

        rv_efficient_course.layoutManager = layoutManager
        rv_efficient_course.adapter = indexChoicenessAdapter
        initListener()

    }


    fun initListener() {
        val activity = activity
        activity?.let {

            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity, R.color.app_color))
            swipeRefreshLayout.setOnRefreshListener {
                getData()
            }
        }
        indexChoicenessAdapter?.setOnItemClickListener { adapter, view, position ->
            val articleDetailInfo = indexChoicenessAdapter?.getItem(position)

            articleDetailInfo?.let {
                activity?.let {
                    ArticleDetailActivity.startExampleDetailActivity(activity, articleDetailInfo.id, articleDetailInfo.post_title, false)
                }
            }
        }

        indexChoicenessAdapter?.setOnLoadMoreListener({ getData() }, rv_efficient_course)


    }

    override fun lazyLoad() {
        getData()
    }


    fun getData() {

        mPresenter?.getArticleInfoList(catId, page, pageSize)

    }

    private fun createNewData(list: List<ArticleDetailInfo>?) {
        if (page == 1) {
            indexChoicenessAdapter?.setNewData(list)
        } else {
            list?.let {
                indexChoicenessAdapter?.addData(list)
            }
        }

        if (list?.size == pageSize) {
            indexChoicenessAdapter?.loadMoreComplete()
            page++
        } else {
            indexChoicenessAdapter?.loadMoreEnd()
        }
    }



    override fun showArticleInfoList(data: List<ArticleDetailInfo>?) {
        createNewData(data)
    }

    override fun onEnd() {
        indexChoicenessAdapter?.loadMoreEnd()
    }

    override fun showLoading() {
        (activity as? BaseActivity)?.showLoading()
    }

    override fun hideLoading() {
        (activity as? BaseActivity)?.hideLoading()
    }

    override fun onComplete() {
        swipeRefreshLayout?.let {
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        }
    }
}