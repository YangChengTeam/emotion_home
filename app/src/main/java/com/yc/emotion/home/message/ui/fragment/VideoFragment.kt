package com.yc.emotion.home.message.ui.fragment

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.ui.activity.EfficientCourseActivity
import com.yc.emotion.home.index.ui.activity.TutorCourseDetailActivity
import com.yc.emotion.home.message.adapter.VideoItemNewAdapter
import com.yc.emotion.home.message.domain.bean.VideoItemInfo
import com.yc.emotion.home.message.presenter.VideoPresenter
import com.yc.emotion.home.message.ui.activity.VideoDetailActivity
import com.yc.emotion.home.message.view.VideoView
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.model.bean.VideoItem
import com.yc.emotion.home.utils.ItemDecorationHelper
import kotlinx.android.synthetic.main.fragment_main_video_new.*


/**
 * Created by suns  on 2020/8/3 17:09.
 */
class VideoFragmentNew : BaseFragment<VideoPresenter>(), VideoView {

    private var page = 1
    private val pageSize = 10
    private lateinit var videoItemNewAdapter: VideoItemNewAdapter
    override fun lazyLoad() {}


    override fun getLayoutId(): Int {
        return R.layout.fragment_main_video_new
    }

    override fun initViews() {
        mPresenter = VideoPresenter(activity, this)

        //生命为瀑布流的布局方式，3列，布局方向为垂直
        val manager = GridLayoutManager(activity, 2)
        manager.isAutoMeasureEnabled = true
        //解决item跳动
        //manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        video_recyclerview.layoutManager = manager


        videoItemNewAdapter = VideoItemNewAdapter(null)

        video_recyclerview.adapter = videoItemNewAdapter

        initListener()

//        val adapter = VideoItemAdapter(initData())
//        video_recyclerview.adapter = adapter
//        video_recyclerview.addItemDecoration(ItemDecorationHelper(activity, 15, 10))
//        adapter.setOnItemClickListener { adapter, view, position ->
//            val intent = Intent(activity, VideoDetailActivity::class.java)
//            startActivity(intent)
//        }
//        val imageList = mutableListOf<Int>()
//        imageList.add(R.mipmap.course_bg)
//
//        initBanner(imageList)
        loadData()
    }


    fun loadData() {
        mPresenter?.getVideoItemInfos(page, pageSize)
    }

    override fun showLoadingDialog() {
        activity?.let {
            (activity as BaseActivity).showLoadingDialog()
        }

    }

    override fun hideLoadingDialog() {
        activity?.let {
            (it as BaseActivity).hideLoadingDialog()
        }
    }

    override fun showVideoInfoList(videoItemInfoList: MutableList<VideoItemInfo>) {
        if (page == 1) {
            videoItemNewAdapter.setNewData(videoItemInfoList)
        } else {
            videoItemNewAdapter.addData(videoItemInfoList)
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onComplete() {
        super.onComplete()
        page++
        videoItemNewAdapter.loadMoreComplete()
    }

    override fun onEnd() {
        super.onEnd()
        videoItemNewAdapter.loadMoreEnd()
    }

    override fun onError() {
        super.onError()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun initListener() {
        videoItemNewAdapter.setOnItemChildClickListener { adapter, view, position ->
            val videoItemInfo = videoItemNewAdapter.getItem(position)
            when (videoItemInfo?.itemType) {
                VideoItemInfo.ITEM_TITLE -> {
                    if (view.id == R.id.tv_more_course) {
                        //更多课程
                        startActivity(Intent(activity, EfficientCourseActivity::class.java))
//                        val intent =Intent(activity,cours)
                    }
                }
            }

        }
        videoItemNewAdapter.setOnItemClickListener { adapter, view, position ->
            val videoItemInfo = videoItemNewAdapter.getItem(position)
            when (videoItemInfo?.itemType) {
                VideoItemInfo.ITEM_COURSE -> {

                    val courseInfo = videoItemInfo.courseInfo
                    courseInfo?.let {
                        TutorCourseDetailActivity.startActivity(activity, courseInfo.id)
                    }

                }
                VideoItemInfo.ITEM_VIDEO -> {
                    VideoDetailActivity.startActivity(activity, videoItemInfo.videoItem)
                    mPresenter?.statisticsVideo(videoItemInfo.videoItem.id)
                }
            }
        }

        activity?.let { ContextCompat.getColor(it, R.color.app_color) }?.let { swipeRefreshLayout.setColorSchemeColors(it) }

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            loadData()
        }
    }

}