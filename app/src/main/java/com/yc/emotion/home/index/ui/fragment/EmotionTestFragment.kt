package com.yc.emotion.home.index.ui.fragment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.index.adapter.EmotionTestAdapter
import com.yc.emotion.home.index.presenter.EmotionTestPresenter
import com.yc.emotion.home.index.ui.activity.EmotionTestDescActivity
import com.yc.emotion.home.index.view.EmotionTestView
import com.yc.emotion.home.model.bean.EmotionTestInfo
import kotlinx.android.synthetic.main.fragment_collect_view.*

/**
 *
 * Created by suns  on 2019/10/11 08:59.
 */
class EmotionTestFragment : BaseLazyFragment<EmotionTestPresenter>(), EmotionTestView {



    private var emotionTestAdapter: EmotionTestAdapter? = null


    private var page = 1
    private val PAGE_SIZE = 10

    private var cat_id: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {

        mPresenter = EmotionTestPresenter(activity, this)

        val mArg = arguments

        mArg?.let {
            cat_id = mArg.getString("cat_id")
        }




//        rv_efficient_course.setPadding(0, 0, 0, 0)
        val layoutManager = LinearLayoutManager(activity)
        emotionTestAdapter = EmotionTestAdapter(null)

        fragment_collect_love_healing_rv.layoutManager = layoutManager
        fragment_collect_love_healing_rv.adapter = emotionTestAdapter

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
        emotionTestAdapter?.setOnItemClickListener { adapter, view, position ->
            val emotionTestInfo = emotionTestAdapter?.getItem(position)
            emotionTestInfo?.let {
                activity?.let {
                    EmotionTestDescActivity.startActivity(activity, emotionTestInfo.id)
                }
            }
        }
        emotionTestAdapter?.setOnLoadMoreListener({ getData() }, fragment_collect_love_healing_rv)

    }

    override fun lazyLoad() {
        getData()
    }


    fun getData() {

        mPresenter.getEmotionTestInfos(cat_id, page, PAGE_SIZE)
    }

    private fun createData(emotionTestInfos: List<EmotionTestInfo>?) {
        if (page == 1) {
            emotionTestAdapter?.setNewData(emotionTestInfos)
        } else {
            emotionTestInfos?.let {

                emotionTestAdapter?.addData(emotionTestInfos)
            }
        }
        if (emotionTestInfos?.size == PAGE_SIZE) {
            emotionTestAdapter?.loadMoreComplete()
            page++
        } else {
            emotionTestAdapter?.loadMoreEnd()
        }


    }

    override fun showEmotionTestListInfo(data: List<EmotionTestInfo>?) {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        createData(data)
    }

    override fun showLoadingDialog() {
        (activity as BaseActivity).showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        (activity as BaseActivity).hideLoadingDialog()
    }


    override fun onError() {
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun onEnd() {
        top_empty_view.visibility = View.VISIBLE

    }
}