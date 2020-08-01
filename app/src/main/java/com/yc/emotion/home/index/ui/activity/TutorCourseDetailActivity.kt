package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.umeng.analytics.MobclickAgent
import com.video.player.lib.constants.VideoConstants
import com.video.player.lib.manager.VideoPlayerManager
import com.video.player.lib.manager.VideoWindowManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.TutorCourseDetailAdapter
import com.yc.emotion.home.index.presenter.TutorCoursePresenter
import com.yc.emotion.home.index.ui.widget.TutorCoursePopwindow
import com.yc.emotion.home.index.view.TutorCourseView
import com.yc.emotion.home.model.bean.LessonInfo
import com.yc.emotion.home.model.bean.TutorCommentInfo
import com.yc.emotion.home.model.bean.TutorCourseDetailInfo
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_tutor_course_detail.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.ParsePosition


/**
 *
 * Created by suns  on 2019/10/9 16:35.
 * 导师课程详情
 */
class TutorCourseDetailActivity : BaseSameActivity(), TutorCourseView {


    private var tutorCourseDetailAdapter: TutorCourseDetailAdapter? = null

    private var page = 1
    private val PAGE_SIZE = 10
    private var tutorCourseDetailInfoList: ArrayList<TutorCourseDetailInfo>? = null


    private var chapterId: String? = null

    companion object {
        fun startActivity(context: Context?, chapter_id: String?) {
            val intent = Intent(context, TutorCourseDetailActivity::class.java)
            intent.putExtra("chapter_id", chapter_id)

            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_tutor_course_detail
    }


    override fun offerActivityTitle(): String {

        return ""
    }

    override fun hindActivityBar(): Boolean {
        return true
    }

    override fun hindActivityTitle(): Boolean {
        return true
    }

    override fun initViews() {

        mPresenter = TutorCoursePresenter(this, this)
        intent?.let {
            if (intent.hasExtra("chapter_id"))
                chapterId = intent.getStringExtra("chapter_id")
        }

        val layoutManager = LinearLayoutManager(this)
        tutorCourseDetailAdapter = TutorCourseDetailAdapter(null)
        rcv_tutor_course.layoutManager = layoutManager
        rcv_tutor_course.adapter = tutorCourseDetailAdapter
        getCourseData(chapterId)
        initListener()
    }

    private fun initListener() {
        tutorCourseDetailAdapter?.setOnLoadMoreListener({
            getCommentData("$tutorId")
        }, rcv_tutor_course)

        tutorCourseDetailAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val tutorCourseDetailInfo = tutorCourseDetailAdapter?.getItem(position)

            val llTutorContainer = tutorCourseDetailAdapter?.getViewByPosition(position, R.id.ll_tutor_container)

            val tvTutorExtend = tutorCourseDetailAdapter?.getViewByPosition(position, R.id.tv_tutor_extend)



            tutorCourseDetailInfo?.let {
                when (tutorCourseDetailInfo.itemType) {
                    TutorCourseDetailInfo.ITEM_TYPE_ONE -> {
                        val courseRecyclerView = tutorCourseDetailAdapter?.getViewByPosition(position, R.id.recyclerview_course) as RecyclerView
                        if (view.id == R.id.tv_tutor_course_total) {
                            mLessons?.let {
                                if (it.isNotEmpty()) {
                                    view_decoder.visibility = View.VISIBLE
                                    val tutorCoursePopWindow = TutorCoursePopwindow(this@TutorCourseDetailActivity)
                                    tutorCoursePopWindow.setCourseData(it)
                                    tutorCoursePopWindow.setOnTagSelectListener(object : TutorCoursePopwindow.OnTagSelectListener {
                                        override fun onTagSelect(lessonInfo: LessonInfo?, pos: Int) {
                                            MobclickAgent.onEvent(this@TutorCourseDetailActivity, "video_player_click", "课程视频播放点击")
                                            if (pos == 0 || lessonInfo?.need_pay == 0) {
                                                startPlayer(lessonInfo)
                                            } else {
                                                if (!UserInfoHelper.instance.goToLogin(this@TutorCourseDetailActivity)) {
                                                    startActivity(Intent(this@TutorCourseDetailActivity, VipActivity::class.java))
                                                }
                                            }
                                            courseRecyclerView.scrollToPosition(pos)

                                        }
                                    })

                                    tutorCoursePopWindow.showUp(ll_tutor_buy)
                                    tutorCoursePopWindow.setOnDismissListener { view_decoder.visibility = View.GONE }
                                }
                            }

                        }
                    }
                    TutorCourseDetailInfo.ITEM_TYPE_SECOND -> {

                        if (view.id == R.id.tv_tutor_extend) {
                            llTutorContainer?.visibility = View.VISIBLE
                            tvTutorExtend?.visibility = View.GONE
                        } else if (view.id == R.id.tv_tutor_close) {
                            llTutorContainer?.visibility = View.GONE
                            tvTutorExtend?.visibility = View.VISIBLE
                        }
                    }
                    TutorCourseDetailInfo.ITEM_TYPE_THIRD -> {
                        if (view.id == R.id.tv_tutor_main_page) {

                            TutorDetailActivity.startActivity(this, tutorCourseDetailInfo.tutors.tutorId)
                        }
                    }
                    TutorCourseDetailInfo.ITEM_TYPE_FOUR -> {//todo 评论
                    }

                    else -> {

                    }
                }
            }

        }

        tutorCourseDetailAdapter?.setOnItemPlayerListener(object : TutorCourseDetailAdapter.OnItemPlayerListener {
            override fun onItemPlay(lessonInfo: LessonInfo) {
                startPlayer(lessonInfo)
            }
        })

        iv_tutor_back.setOnClickListener { finish() }
        ll_tutor_buy.setOnClickListener { ToastUtils.showCenterToast("购买课程") }

        iv_course_share.setOnClickListener {
            //todo 分享
        }

        iv_course_collect.setOnClickListener {
            //收藏
            collectCourse(chapterId)
        }
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }


    private fun getCommentData(tutor_id: String?) {

        (mPresenter as? TutorCoursePresenter)?.getTutorCommentInfos(tutor_id, page, PAGE_SIZE)

    }

    private var mLessons: List<LessonInfo>? = null
    private var isCollect: Int = 0

    private fun getCourseData(chapter_id: String?) {
        (mPresenter as? TutorCoursePresenter)?.getCourseInfo(chapter_id)

    }

    private var tutorId: Int? = null
    private fun createData(data: TutorCourseDetailInfo) {
        tutorId = data.chapter?.tutor_id
        val courseInfo = data.chapter
        tv_money.text = "¥${courseInfo.price}"
        tutorCourseDetailInfoList = arrayListOf()
        if (page == 1) {
            data.let {
                val lesson = data.lessons

                val tutorCourseDetailInfo1 = TutorCourseDetailInfo(TutorCourseDetailInfo.ITEM_TYPE_ONE)
                tutorCourseDetailInfo1.lessons = lesson

                val tutorCourseDetailInfo2 = TutorCourseDetailInfo(TutorCourseDetailInfo.ITEM_TYPE_SECOND)
                tutorCourseDetailInfo2.chapter = data.chapter
                tutorCourseDetailInfo2.tutors = data.tutors

                val tutorCourseDetailInfo3 = TutorCourseDetailInfo(TutorCourseDetailInfo.ITEM_TYPE_THIRD)
                tutorCourseDetailInfo3.tutors = data.tutors

                tutorCourseDetailInfoList?.add(tutorCourseDetailInfo1)
                tutorCourseDetailInfoList?.add(tutorCourseDetailInfo2)
                tutorCourseDetailInfoList?.add(tutorCourseDetailInfo3)
            }
        }

        getCommentData("$tutorId")
    }


    override fun setCollectState(collect: Int) {
        isCollect = collect
        if (collect == 0)//未收藏
        {
            iv_course_collect.setImageResource(R.mipmap.icon_course_collection)
        } else if (collect == 1) {
            iv_course_collect.setImageResource(R.mipmap.icon_collection_sel)
        }
    }

    private fun createNewData(communityInfos: List<TutorCommentInfo>?) {


        communityInfos?.let {
            communityInfos.forEach {
                val tutorCourseDetailInfo = TutorCourseDetailInfo(TutorCourseDetailInfo.ITEM_TYPE_FOUR)
                tutorCourseDetailInfo.communityInfo = it
                tutorCourseDetailInfoList?.add(tutorCourseDetailInfo)
            }
        }

        if (page == 1) {
            tutorCourseDetailAdapter?.setNewData(tutorCourseDetailInfoList)
        } else {
            tutorCourseDetailInfoList?.let {
                tutorCourseDetailAdapter?.addData(it)
            }
        }
        communityInfos?.let {
            if (communityInfos.size == PAGE_SIZE) {
                tutorCourseDetailAdapter?.loadMoreComplete()
                page += 1
            } else {
                tutorCourseDetailAdapter?.loadMoreEnd()
            }
        }


    }


    private fun collectCourse(chapter_id: String?) {

        if (!UserInfoHelper.instance.goToLogin(this)) {
            (mPresenter as TutorCoursePresenter).collectCourse(chapter_id, isCollect)
        }
    }


    override fun onResume() {
        super.onResume()
        VideoPlayerManager.getInstance().onResume()
    }

    override fun onPause() {
        super.onPause()
        VideoPlayerManager.getInstance().onPause()
    }

    override fun onBackPressed() {
        //尝试弹射返回
        if (VideoPlayerManager.getInstance().isBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VideoPlayerManager.getInstance().onDestroy()
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy()
    }


    private fun startPlayer(lessonInfo: LessonInfo?) {

        Glide.with(this).load(lessonInfo?.lesson_image).apply(RequestOptions().error(R.mipmap.efficient_course_example_pic)).into(videoPlayer.coverController.mVideoCover)
        videoPlayer.setVideoDisplayType(VideoConstants.VIDEO_DISPLAY_TYPE_ZOOM)
        videoPlayer.startPlayVideo(lessonInfo?.lesson_url, lessonInfo?.lesson_title)
    }

    override fun showCourseDetailInfo(data: TutorCourseDetailInfo?) {
        data?.let {
            val lessons = data.lessons
            mLessons = lessons
            lessons?.let {
                if (lessons.isNotEmpty()) {
                    val lesson = lessons[0]
                    startPlayer(lesson)
                }

            }
            data.chapter?.let {
                isCollect = data.chapter.is_collect
            }
            setCollectState(isCollect)

            createData(data)
        }
    }

    override fun showTutorCommentInfos(comment_list: List<TutorCommentInfo>?) {
        createNewData(comment_list)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        getCourseData(chapterId)
    }
}