package com.yc.emotion.home.index.ui.fragment

import android.content.Intent
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.index.adapter.TutorDetailCommentAdapter
import com.yc.emotion.home.index.ui.activity.TutorCourseDetailActivity
import com.yc.emotion.home.model.bean.CommunityInfo
import kotlinx.android.synthetic.main.fragment_efficient_course.*

/**
 *
 * Created by suns  on 2019/10/12 14:29.
 */
class TutorDetailCommentFragment : BaseFragment<BasePresenter<IModel, IView>>() {


    private var loveEngine: LoveEngine? = null
    private var tutorDetailCommentAdapter: TutorDetailCommentAdapter? = null
    var loadDialog: LoadDialog? = null
    private var mHandler: Handler? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_efficient_course
    }


    override fun initViews() {
        loveEngine = LoveEngine(activity)
        loadDialog = LoadDialog(activity)
        mHandler = Handler()
        val layoutManager = LinearLayoutManager(activity)
        tutorDetailCommentAdapter = TutorDetailCommentAdapter(null)
        rv_efficient_course.layoutManager = layoutManager
        rv_efficient_course.adapter = tutorDetailCommentAdapter
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
        tutorDetailCommentAdapter?.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(activity, TutorCourseDetailActivity::class.java))
        }

    }

    override fun lazyLoad() {
        getData()
    }


    fun getData() {

        loadDialog?.showLoadingDialog()
        mHandler?.postDelayed({
            val commentInfos = arrayListOf<CommunityInfo>()

            for (i in 0..5) {
                commentInfos.add(CommunityInfo())
            }
            tutorDetailCommentAdapter?.setNewData(commentInfos)
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
            loadDialog?.dismissLoadingDialog()

        }, 500)

    }


}