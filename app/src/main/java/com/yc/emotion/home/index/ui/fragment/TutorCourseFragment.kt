package com.yc.emotion.home.index.ui.fragment

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
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
class TutorCourseFragment : BaseFragment<TutorCoursePresenter>(), TutorCourseView {


    var loveEngine: LoveEngine? = null
    var efficientCourseAdapter: EfficientCourseAdapter? = null
    var loadDialog: LoadDialog? = null
    var mHandler: Handler? = null

    var tutorId: String? = null

    var page = 1
    val PAGE_SIZE = 10


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {
        mPresenter = TutorCoursePresenter(activity, this)

        val intent = arguments

        intent?.let {
            tutorId = intent.getString("tutor_id")
        }

        loveEngine = LoveEngine(activity)
        loadDialog = LoadDialog(activity)
        mHandler = Handler()
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
                page = 1
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

        efficientCourseAdapter?.setOnLoadMoreListener({ getData() }, fragment_collect_love_healing_rv)

    }

    override fun lazyLoad() {
        getData()
    }


    fun getData() {


        mPresenter?.getTutorCourseInfos(tutorId, page, PAGE_SIZE)

//        loadDialog?.showLoadingDialog()
//
//
//        loveEngine?.getTutorCourseInfos(tutorId, page, PAGE_SIZE)?.subscribe(object : Subscriber<ResultInfo<CourseInfoWrapper>>() {
//            override fun onNext(t: ResultInfo<CourseInfoWrapper>?) {
//                t?.let {
//                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.lessons != null && t.data.lessons!!.isNotEmpty()) {
//                        createNewData(t.data.lessons)
//                    } else {
//                        if (page == 1) top_empty_view.visibility = View.VISIBLE
//                    }
//                }
//            }
//
//            override fun onCompleted() {
//                if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
//                loadDialog?.dismissLoadingDialog()
//            }
//
//            override fun onError(e: Throwable?) {
//
//            }
//        })


    }

    private fun createNewData(lessons: List<CourseInfo>?) {
        if (page == 1) efficientCourseAdapter?.setNewData(lessons)
        else {
            lessons?.let {

                efficientCourseAdapter?.addData(lessons)
            }

        }
        if (lessons?.size == PAGE_SIZE) {
            efficientCourseAdapter?.loadMoreComplete()
            page++
        } else {
            efficientCourseAdapter?.loadMoreEnd()
        }
    }

    override fun showTutorCourseInfos(lessons: List<CourseInfo>?) {
        createNewData(lessons)
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showLoadingDialog() {
        (activity as? BaseActivity)?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        (activity as? BaseActivity)?.hideLoadingDialog()
    }
}