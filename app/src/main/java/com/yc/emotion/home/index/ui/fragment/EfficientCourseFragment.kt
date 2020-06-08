package com.yc.emotion.home.index.ui.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.EfficientCourseAdapter
import com.yc.emotion.home.index.presenter.TutorCoursePresenter
import com.yc.emotion.home.index.ui.activity.TutorCourseDetailActivity
import com.yc.emotion.home.index.view.TutorCourseView
import com.yc.emotion.home.model.bean.CourseInfo
import kotlinx.android.synthetic.main.fragment_collect_view.*

/**
 *
 * Created by suns  on 2019/10/9 11:33.
 */
class EfficientCourseFragment : BaseFragment<TutorCoursePresenter>(), TutorCourseView {



    var efficientCourseAdapter: EfficientCourseAdapter? = null


    var catId: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }

    override fun initViews() {

        mPresenter = TutorCoursePresenter(activity, this)

        val intent = arguments

        intent?.let {
            catId = intent.getString("cat_id")
        }




//        rv_efficient_course.setPadding(0, 0, 0, 0)
        val layoutManager = LinearLayoutManager(activity)
        efficientCourseAdapter = EfficientCourseAdapter(null)
        fragment_collect_love_healing_rv.layoutManager = layoutManager
        fragment_collect_love_healing_rv.adapter = efficientCourseAdapter
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
        efficientCourseAdapter?.setOnItemClickListener { adapter, view, position ->
            val courseInfo = efficientCourseAdapter?.getItem(position)
            courseInfo?.let {
                activity?.let {

                    TutorCourseDetailActivity.startActivity(activity, courseInfo.id)
                }
            }

        }

    }

    override fun lazyLoad() {
        getData()
    }


    fun getData() {

        mPresenter?.let {
            (mPresenter as TutorCoursePresenter).getCourseList(catId)
        }

    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }

    override fun showCourseListInfo(data: List<CourseInfo>?) {
        efficientCourseAdapter?.setNewData(data)
        top_empty_view.visibility = View.GONE
    }


    override fun showLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).showLoadingDialog()
        }
    }

    override fun hideLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).hideLoadingDialog()
        }
    }
}