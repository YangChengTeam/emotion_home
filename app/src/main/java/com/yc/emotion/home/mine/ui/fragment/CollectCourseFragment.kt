package com.yc.emotion.home.mine.ui.fragment

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.EfficientCourseAdapter
import com.yc.emotion.home.index.ui.activity.TutorCourseDetailActivity
import com.yc.emotion.home.mine.presenter.CollectPresenter
import com.yc.emotion.home.mine.ui.activity.CollectActivity
import com.yc.emotion.home.mine.view.CollectView
import com.yc.emotion.home.model.bean.CourseInfo
import kotlinx.android.synthetic.main.fragment_collect_view.*


/**
 *
 * Created by suns  on 2019/10/17 15:32.
 */
class CollectCourseFragment : BaseFragment<CollectPresenter>(), CollectView {



    private var efficientCourseAdapter: EfficientCourseAdapter? = null


    private var page = 1
    private val PAGE_SIZE = 10

    private var mCollectActivity: CollectActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CollectActivity) {
            mCollectActivity = context
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }

    override fun initViews() {
        mPresenter = CollectPresenter(activity, this)

        fragment_collect_love_healing_rv.layoutManager = LinearLayoutManager(activity)

        efficientCourseAdapter = EfficientCourseAdapter(null)

        fragment_collect_love_healing_rv.adapter = efficientCourseAdapter

        initListener()
    }

    fun initListener() {

        mCollectActivity?.let {

            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.app_color))
        }

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }


        efficientCourseAdapter?.setOnItemClickListener { adapter, view, position ->
            val courseInfo = efficientCourseAdapter?.getItem(position)

            TutorCourseDetailActivity.startActivity(mCollectActivity, courseInfo?.id)
        }

        efficientCourseAdapter?.setOnLoadMoreListener({ getData() }, fragment_collect_love_healing_rv)
    }


    private fun getData() {

        mPresenter?.getCourseCollectList(page, PAGE_SIZE)

    }

    private fun createNewData(data: List<CourseInfo>?) {
        if (page == 1) efficientCourseAdapter?.setNewData(data)
        else {
            data?.let {
                efficientCourseAdapter?.addData(data)
            }
        }
        if (data?.size == PAGE_SIZE) {
            efficientCourseAdapter?.loadMoreComplete()
            page++
        } else {
            efficientCourseAdapter?.loadMoreEnd()
        }

    }

    override fun lazyLoad() {
        getData()
    }


    override fun showCollectCourseList(data: List<CourseInfo>?) {
        createNewData(data)
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showLoadingDialog() {
        mCollectActivity?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mCollectActivity?.hideLoadingDialog()
    }
}