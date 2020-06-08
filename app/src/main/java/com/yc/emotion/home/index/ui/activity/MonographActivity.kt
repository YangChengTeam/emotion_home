package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.IndexChoicenessAdapter
import com.yc.emotion.home.index.presenter.MonagraphPresenter
import com.yc.emotion.home.index.view.MonagraphView
import com.yc.emotion.home.model.bean.ArticleDetailInfo
import kotlinx.android.synthetic.main.activity_monograph.*

/**
 *
 * Created by suns  on 2020/4/29 10:29.
 * 专题文章 击退小三 挽救婚姻
 */
class MonographActivity : BaseSameActivity(), MonagraphView {

    companion object {
        fun startActivity(context: Context?, title: String, series: String) {
            val intent = Intent(context, MonographActivity::class.java)
            intent.putExtra("series", series)
            intent.putExtra("title", title)
            context?.startActivity(intent)

        }
    }


    private lateinit var indexChoicenessAdapter: IndexChoicenessAdapter
    var series: String? = "jituixiaosan"
    var title: String? = "击退小三"
    var page = 1
    var pageSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        series = intent?.getStringExtra("series")
        title = intent?.getStringExtra("title")

        setContentView(getLayoutId())
        initViews()
    }

    override fun offerActivityTitle(): String? {
        return title
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_monograph
    }

    override fun initViews() {
        super.initViews()


        mPresenter = MonagraphPresenter(this, this)
        recyclerView_monograph.layoutManager = LinearLayoutManager(this)
        indexChoicenessAdapter = IndexChoicenessAdapter(null, false, isMore = true)
        recyclerView_monograph.adapter = indexChoicenessAdapter
        getData()
        initListener()
    }

    fun getData() {
        (mPresenter as MonagraphPresenter).getMonographArticles(series, page, pageSize)
    }

    private fun initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }
        indexChoicenessAdapter.setOnItemClickListener { adapter, view, position ->
            val articleDetailInfo = indexChoicenessAdapter.getItem(position)

            articleDetailInfo?.let {

                ArticleDetailActivity.startExampleDetailActivity(this, articleDetailInfo.id, articleDetailInfo.post_title, false)
            }
        }

        indexChoicenessAdapter.setOnLoadMoreListener({ getData() }, recyclerView_monograph)
    }

    override fun showMonagraphInfos(data: List<ArticleDetailInfo>?) {
        setNewData(data)
    }


    fun setNewData(data: List<ArticleDetailInfo>?) {
        if (page == 1) {
            indexChoicenessAdapter.setNewData(data)
        } else {
            data?.let {
                indexChoicenessAdapter.addData(data)
            }
        }

        if (data?.size == pageSize) {
            indexChoicenessAdapter.loadMoreComplete()
            page++
        } else {
            indexChoicenessAdapter.loadMoreEnd()
        }

        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }


    override fun onComplete() {
        super.onComplete()
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onEnd() {
        super.onEnd()
        indexChoicenessAdapter.loadMoreEnd()
    }

}