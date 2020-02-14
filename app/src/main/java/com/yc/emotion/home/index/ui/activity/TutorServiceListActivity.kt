package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.index.adapter.TutorServiceListAdapter
import com.yc.emotion.home.model.bean.TutorServiceInfo
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.index.presenter.TutorPresenter
import com.yc.emotion.home.index.view.TutorView
import kotlinx.android.synthetic.main.fragment_collect_view.*

import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/12 16:49.
 */
class TutorServiceListActivity : BaseSameActivity(), TutorView {

    private var tutorServiceListAdapter: TutorServiceListAdapter? = null
    private var tutorId: String? = null

    private var page = 1
    private val PAGE_SIZE = 10

    companion object {
        fun startActivity(context: Context, tutor_id: String?) {
            val intent = Intent(context, TutorServiceListActivity::class.java)
            intent.putExtra("tutor_id", tutor_id)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {

        mPresenter = TutorPresenter(this, this)

        tutorId = intent?.getStringExtra("tutor_id")


//        rv_efficient_course.setPadding(0, 0, 0, 0)
        fragment_collect_love_healing_rv.layoutManager = LinearLayoutManager(this)

        tutorServiceListAdapter = TutorServiceListAdapter(null)

        fragment_collect_love_healing_rv.adapter = tutorServiceListAdapter
        initData()
        initListener()
    }

    fun initListener() {


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_color))
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }

        tutorServiceListAdapter?.setOnItemClickListener { adapter, view, position ->
            val tutorServiceInfo = tutorServiceListAdapter?.getItem(position)
            TutorServiceDetailActivity.startActivity(this, tutorServiceInfo?.id, tutorId)
        }

        tutorServiceListAdapter?.setOnLoadMoreListener({ getData() }, fragment_collect_love_healing_rv)

    }

    private fun initData() {

        getData()
    }


    private fun getData() {

        (mPresenter as? TutorPresenter)?.getTutorServices(tutorId, page, PAGE_SIZE)

    }

    private fun createNewData(tutorServices: List<TutorServiceInfo>?) {
        if (page == 1) {
            tutorServiceListAdapter?.setNewData(tutorServices)
        } else {
            tutorServices?.let {
                tutorServiceListAdapter?.addData(tutorServices)
            }
        }
        if (tutorServices?.size == PAGE_SIZE) {
            tutorServiceListAdapter?.loadMoreComplete()
            page++
        } else {
            tutorServiceListAdapter?.loadMoreEnd()
        }
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showTutorServiceInfos(data: List<TutorServiceInfo>?) {
        top_empty_view.visibility = View.GONE
        createNewData(data)
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }

    override fun offerActivityTitle(): String {

        return "导师服务"
    }
}