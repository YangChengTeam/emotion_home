package com.yc.emotion.home.index.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.index.adapter.TutorListAdapter
import com.yc.emotion.home.index.ui.activity.TutorDetailActivity
import com.yc.emotion.home.index.ui.activity.TutorListActivity
import com.yc.emotion.home.model.bean.TutorInfo
import com.yc.emotion.home.base.domain.engine.LoveEngine
import com.yc.emotion.home.base.domain.model.IModel
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.widget.LoadDialog
import com.yc.emotion.home.base.view.IView
import com.yc.emotion.home.index.presenter.TutorPresenter
import com.yc.emotion.home.index.view.TutorView
import kotlinx.android.synthetic.main.fragment_collect_view.*
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/9 11:33.
 */
class TutorListFragment : BaseLazyFragment<TutorPresenter>(), TutorView {


    private var tutorListAdapter: TutorListAdapter? = null

    private var page = 1
    private var pageSize = 10
    private var catId: String? = null
    private var postion: Int? = 0


    companion object {
        fun newInstance(catId: String?, postion: Int): TutorListFragment {
            val tutorListFragment = TutorListFragment()

            val bundle = Bundle()
            bundle.putString("cat_id", catId)
            bundle.putInt("position", postion)

            tutorListFragment.arguments = bundle

            return tutorListFragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {

        mPresenter = TutorPresenter(activity, this)

        catId = arguments?.getString("cat_id")

        postion = arguments?.getInt("position")


        val layoutManager = LinearLayoutManager(activity)
        tutorListAdapter = TutorListAdapter(null)

        fragment_collect_love_healing_rv.layoutManager = layoutManager
        fragment_collect_love_healing_rv.adapter = tutorListAdapter
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
        tutorListAdapter?.setOnItemClickListener { adapter, view, position ->
            val tutorInfo = tutorListAdapter?.getItem(position)
            tutorInfo?.let {
                activity?.let {
                    TutorDetailActivity.startActivity(activity, tutorInfo.tutorId)
                }
            }

        }
        tutorListAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val tutorInfo = tutorListAdapter?.getItem(position)
            tutorInfo?.let {
                (activity as TutorListActivity).showToWxServiceDialog(tutorId = tutorInfo.tutorId)
            }
        }

        tutorListAdapter?.setOnLoadMoreListener({ getData() }, fragment_collect_love_healing_rv)

    }

    override fun lazyLoad() {
        getData()

    }


    fun getData() {

        mPresenter.getTutorListInfo(catId, page, pageSize)

    }

    private fun createNewData(tutorList: List<TutorInfo>?) {
        if (page == 1) tutorListAdapter?.setNewData(tutorList)
        else {
            tutorList?.let { tutorListAdapter?.addData(it) }
        }
        if (tutorList?.size == pageSize) {
            tutorListAdapter?.loadMoreComplete()
            page++
        } else {
            tutorListAdapter?.loadMoreEnd()
        }
    }

    override fun showTutorListInfos(data: List<TutorInfo>?) {
        createNewData(data)
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onNoData() {
        if (page == 1) {
            top_empty_view.visibility = View.VISIBLE
        } else {
            top_empty_view.visibility = View.GONE
        }
    }

    override fun showLoadingDialog() {
        (activity as BaseActivity).showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        (activity as BaseActivity).hideLoadingDialog()
    }
}