package com.yc.emotion.home.index.ui.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.SearchTutorAdapter
import com.yc.emotion.home.index.presenter.EmotionSearchPresenter
import com.yc.emotion.home.index.ui.activity.TutorDetailActivity
import com.yc.emotion.home.index.ui.activity.TutorListActivity
import com.yc.emotion.home.index.view.EmotionSearchView
import com.yc.emotion.home.model.bean.IndexSearchInfo
import com.yc.emotion.home.model.bean.event.EventSearch
import kotlinx.android.synthetic.main.fragment_collect_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * Created by suns  on 2019/10/15 10:57.
 */
class EmotionSearchTutorFragment : BaseFragment<EmotionSearchPresenter>(), EmotionSearchView {



    var tutorListAdapter: SearchTutorAdapter? = null


    private var keyWord: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {
        mPresenter = EmotionSearchPresenter(activity, this)

        keyWord = arguments?.getString("keyword")


        val layoutManager = LinearLayoutManager(activity)
        tutorListAdapter = SearchTutorAdapter(null)

        fragment_collect_love_healing_rv.layoutManager = layoutManager
        fragment_collect_love_healing_rv.adapter = tutorListAdapter
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

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(eventSearch: EventSearch) {

        keyWord = eventSearch.keyword
        getData()
    }


    override fun lazyLoad() {
        getData()
    }


    fun getData() {

        mPresenter?.searchIndexInfo(keyWord,2)


    }

    override fun showEmotionSearchResult(data: IndexSearchInfo?) {
        data?.let {
            if (data.tutors != null && data.tutors!!.isNotEmpty()) {
                tutorListAdapter?.setNewData(data.tutors)
                top_empty_view.visibility = View.GONE
            } else {
                top_empty_view.visibility = View.VISIBLE
            }
        }
    }

    override fun onComplete() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onNoData() {
        top_empty_view.visibility = View.VISIBLE
    }

    override fun showLoadingDialog() {
        (activity as? BaseActivity)?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        (activity as? BaseActivity)?.hideLoadingDialog()
    }


}