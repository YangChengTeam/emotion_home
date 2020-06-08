package com.yc.emotion.home.index.ui.fragment

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.adapter.EmotionSearchContentAdapter
import com.yc.emotion.home.index.presenter.EmotionSearchPresenter
import com.yc.emotion.home.index.ui.activity.*
import com.yc.emotion.home.index.view.EmotionSearchView
import com.yc.emotion.home.model.bean.IndexSearchInfo
import com.yc.emotion.home.model.bean.SearchContentInfo
import com.yc.emotion.home.model.bean.event.EventSearch
import kotlinx.android.synthetic.main.fragment_collect_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * Created by suns  on 2019/10/15 10:55.
 */
class EmotionSearchContentFragment : BaseFragment<EmotionSearchPresenter>(), EmotionSearchView {


    private var emotionSearchContentAdapter: EmotionSearchContentAdapter? = null

    private var searchContentInfos: MutableList<SearchContentInfo>? = null

    private var keyWord: String? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_collect_view
    }


    override fun initViews() {


        mPresenter = EmotionSearchPresenter(activity, this)

        keyWord = arguments?.getString("keyword")


        searchContentInfos = mutableListOf()
//        rv_efficient_course.setPadding(0, 0, 0, 0)

        val layoutManager = GridLayoutManager(activity, 2)

        fragment_collect_love_healing_rv.layoutManager = layoutManager

        emotionSearchContentAdapter = EmotionSearchContentAdapter(null)
        fragment_collect_love_healing_rv.adapter = emotionSearchContentAdapter
        initListener()
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
        searchKeyword()
    }


    fun getData() {
        searchKeyword()
    }

    private fun initListener() {

        activity?.let {
            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.app_color))
            swipeRefreshLayout.setOnRefreshListener {
                //                getData()
                swipeRefreshLayout.isRefreshing = false
            }

        }

        emotionSearchContentAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val searchContentInfo = emotionSearchContentAdapter?.getItem(position)
            searchContentInfo?.let {
                when (searchContentInfo.type) {
                    SearchContentInfo.ITEM_TYPE_ARTICLE -> startActivity(Intent(activity, MoreArticleActivity::class.java))
                    SearchContentInfo.ITEM_TYPE_COURSE -> startActivity(Intent(activity, EfficientCourseActivity::class.java))
                }
            }
        }

        emotionSearchContentAdapter?.setOnItemClickListener { adapter, view, position ->
            val searchContentInfo = emotionSearchContentAdapter?.getItem(position)
            searchContentInfo?.let {
                when (searchContentInfo.type) {
                    SearchContentInfo.ITEM_TYPE_ARTICLE -> {
                        activity?.let {
                            ArticleDetailActivity.startExampleDetailActivity(it, searchContentInfo.articleDetailInfo?.id, searchContentInfo.articleDetailInfo?.post_title)
                        }
                    }
                    SearchContentInfo.ITEM_TYPE_COURSE -> {
                        val courseInfo = searchContentInfo.courseInfo
                        courseInfo?.let {
                            activity?.let {
                                TutorCourseDetailActivity.startActivity(it, courseInfo.id)
                            }

                        }
                    }
                    SearchContentInfo.ITEM_TYPE_EMOTION_TEST -> {
                        val testInfo = searchContentInfo.emotionTestInfo
                        testInfo?.let {
                            activity?.let {
                                EmotionTestDescActivity.startActivity(it, testInfo.id)
                            }
                        }
                    }
                    else -> ""

                }
            }

        }
    }

    override fun lazyLoad() {
        getData()
    }


    private fun searchKeyword() {
        mPresenter?.searchIndexInfo(keyWord, 1)

    }

    private fun createNewData(data: IndexSearchInfo?) {
        data?.let {
            searchContentInfos?.clear()
            val article = data.article
            article?.let {
                for ((index, value) in article.withIndex()) {
                    val searchContentInfo = SearchContentInfo()
                    searchContentInfo.articleDetailInfo = value
                    searchContentInfo.type = SearchContentInfo.ITEM_TYPE_ARTICLE
                    if (index == 0) {
                        searchContentInfo.isShow = true
                    }
                    searchContentInfos?.add(searchContentInfo)
                }

                if (article.isNotEmpty()) {
                    val searchContentInfo = SearchContentInfo()
                    searchContentInfo.type = SearchContentInfo.ITEM_TYPE_DIVIDER
                    searchContentInfos?.add(searchContentInfo)
                }
            }

            val lesson = data.lesson_chapter
            lesson?.let {

                for ((key, value) in lesson.withIndex()) {


                    val searchContentInfo = SearchContentInfo()
                    searchContentInfo.courseInfo = value
                    searchContentInfo.type = SearchContentInfo.ITEM_TYPE_COURSE

                    if (key == 0) {
                        searchContentInfo.isShow = true
                        searchContentInfo.isExtra = false
                    } else if (key == 1) {
                        searchContentInfo.isShow = false
                        searchContentInfo.isExtra = true
                    }
                    searchContentInfos?.add(searchContentInfo)
                }

                if (lesson.isNotEmpty()) {
                    val searchContentInfo = SearchContentInfo()
                    searchContentInfo.type = SearchContentInfo.ITEM_TYPE_DIVIDER
                    searchContentInfos?.add(searchContentInfo)
                }
            }

            val test = data.psych_test
            test?.let {
                for ((index, value) in test.withIndex()) {
                    val searchContentInfo = SearchContentInfo()
                    searchContentInfo.emotionTestInfo = value
                    searchContentInfo.type = SearchContentInfo.ITEM_TYPE_EMOTION_TEST
                    if (index == 0) {
                        searchContentInfo.isShow = true
                    }
                    searchContentInfos?.add(searchContentInfo)
                }
            }

            searchContentInfos?.let {
                if (it.isNotEmpty()) {
                    emotionSearchContentAdapter?.setNewData(searchContentInfos)
                    top_empty_view.visibility = View.GONE
                } else {
                    top_empty_view.visibility = View.VISIBLE
                }
            }


        }
    }

    override fun showEmotionSearchResult(data: IndexSearchInfo?) {
        createNewData(data)
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