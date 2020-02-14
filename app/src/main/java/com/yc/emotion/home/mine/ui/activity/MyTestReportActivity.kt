package com.yc.emotion.home.mine.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.ui.activity.TestRecordDetailActivity
import com.yc.emotion.home.mine.adapter.EmotionTestReportAdapter
import com.yc.emotion.home.model.bean.EmotionTestInfo
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.presenter.EmotionTestPresenter
import com.yc.emotion.home.index.view.EmotionTestView
import com.yc.emotion.home.utils.CommonInfoHelper
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_collect_view.*

import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/17 09:39.
 */
class MyTestReportActivity : BaseSameActivity(), EmotionTestView {


    private var emotionTestAdapter: EmotionTestReportAdapter? = null

    private var page = 1
    private val PAGE_SIZE = 10

    private var userId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }

    override fun initViews() {

        mPresenter = EmotionTestPresenter(this, this)

        if (mLoadingDialog == null) mLoadingDialog = LoadDialog(this)

//        rv_efficient_course.setPadding(0, 0, 0, 0)
        val layoutManager = LinearLayoutManager(this)
        emotionTestAdapter = EmotionTestReportAdapter(null)

        fragment_collect_love_healing_rv.layoutManager = layoutManager
        fragment_collect_love_healing_rv.adapter = emotionTestAdapter
        initData()
        initListener()

        userId = UserInfoHelper.instance.getUid() as Int


    }


    fun initListener() {


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }

        emotionTestAdapter?.setOnItemClickListener { adapter, view, position ->
            val emotionTestInfo = emotionTestAdapter?.getItem(position)
            TestRecordDetailActivity.startActivity(this, emotionTestInfo?.record_id)
        }
        emotionTestAdapter?.setOnLoadMoreListener({ getData() }, fragment_collect_love_healing_rv)

    }

    private fun initData() {
        if (page == 1) {

            (mPresenter as? EmotionTestPresenter)?.getTestRecordsCache()
        }

        getData()
    }


    fun getData() {

        (mPresenter as? EmotionTestPresenter)?.getTestRecords(page, PAGE_SIZE)

    }

    private fun createNewData(data: List<EmotionTestInfo>?) {

        if (page == 1) {
            emotionTestAdapter?.setNewData(data)

        } else {
            data?.let {
                emotionTestAdapter?.addData(data)
            }
        }
        if (data?.size == PAGE_SIZE) {
            emotionTestAdapter?.loadMoreComplete()
            page++
        } else {
            emotionTestAdapter?.loadMoreEnd()
        }

    }

    override fun showTestRecords(data: List<EmotionTestInfo>?) {
        createNewData(data)

    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onNoData() {

        top_empty_view.visibility = View.VISIBLE
    }

    override fun onError() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun offerActivityTitle(): String {
        return "测试报告"
    }
}