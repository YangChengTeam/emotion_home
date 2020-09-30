package com.yc.emotion.home.message.ui.fragment

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.index.ui.activity.EfficientCourseActivity
import com.yc.emotion.home.index.ui.activity.TutorCourseDetailActivity
import com.yc.emotion.home.message.adapter.VideoItemAdapter
import com.yc.emotion.home.message.domain.bean.VideoItemInfo
import com.yc.emotion.home.message.presenter.VideoPresenter
import com.yc.emotion.home.message.ui.activity.VideoDetailActivity
import com.yc.emotion.home.message.view.VideoView
import kotlinx.android.synthetic.main.fragment_main_video.*


/**
 * Created by suns  on 2020/8/3 17:09.
 */
class VideoFragment : BaseFragment<VideoPresenter>(), VideoView {

    private var page = 1
    private val pageSize = 10
    private lateinit var videoItemNewAdapter: VideoItemAdapter
    override fun lazyLoad() {}


    override fun getLayoutId(): Int {
        return R.layout.fragment_main_video
    }

    override fun initViews() {
        mPresenter = VideoPresenter(activity, this)

        //生命为瀑布流的布局方式，3列，布局方向为垂直
        val manager = GridLayoutManager(activity, 2)
        manager.isAutoMeasureEnabled = true
        //解决item跳动
        //manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        video_recyclerview.layoutManager = manager


        videoItemNewAdapter = VideoItemAdapter(null,0)

        video_recyclerview.adapter = videoItemNewAdapter

        initListener()

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

    override fun showVideoInfoList(videoItemInfoList: MutableList<VideoItemInfo>, size: Int) {
        if (page == 1) {
            videoItemNewAdapter.setCount(size)
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
                        MobclickAgent.onEvent(activity,"video_course_click","视频页课程点击")
                    }

                }
                VideoItemInfo.ITEM_VIDEO -> {
                    VideoDetailActivity.startActivity(activity, videoItemInfo.videoItem)
                    activity?.overridePendingTransition(R.anim.ani_in,R.anim.ani_out)
                    mPresenter?.statisticsVideo(videoItemInfo.videoItem.id)
                    MobclickAgent.onEvent(activity,"small_video_click","小视频点击")
                }
            }
        }

        activity?.let { ContextCompat.getColor(it, R.color.app_color) }?.let { swipeRefreshLayout.setColorSchemeColors(it) }

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            loadData()
        }

        videoItemNewAdapter.setOnLoadMoreListener({
            loadData()
        },video_recyclerview)
    }


}