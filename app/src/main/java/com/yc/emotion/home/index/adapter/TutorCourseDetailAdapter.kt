package com.yc.emotion.home.index.adapter

import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.ScreenUtil
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.model.bean.LessonInfo
import com.yc.emotion.home.model.bean.TutorCourseDetailInfo
import com.yc.emotion.home.utils.UserInfoHelper

/**
 * Created by suns  on 2019/10/10 09:00.
 * /**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
**/
 */
class TutorCourseDetailAdapter(data: List<TutorCourseDetailInfo>?) : BaseMultiItemQuickAdapter<TutorCourseDetailInfo, BaseViewHolder>(data) {

    private var userId: Int = 0


    var listener: OnItemPlayerListener? = null

    init {
        addItemType(TutorCourseDetailInfo.ITEM_TYPE_ONE, R.layout.layout_tutor_course_top)
        addItemType(TutorCourseDetailInfo.ITEM_TYPE_SECOND, R.layout.layout_tutor_course_second)
        addItemType(TutorCourseDetailInfo.ITEM_TYPE_THIRD, R.layout.layout_tutor_course_third)
        addItemType(TutorCourseDetailInfo.ITEM_TYPE_FOUR, R.layout.layout_tutor_course_four)
        userId = UserInfoHelper.instance.getUid()
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder?, item: TutorCourseDetailInfo?) {

        helper?.let {
            item?.let {
                when (item.itemType) {
                    TutorCourseDetailInfo.ITEM_TYPE_ONE -> {
                        val llContainer = helper.getView<LinearLayout>(R.id.ll_scroll_container)
                        initTutorData(llContainer, item.lessons)
                        helper.addOnClickListener(R.id.tv_tutor_course_total)
                                .setText(R.id.tv_tutor_course_total, "共" + item.lessons.size + "课")
                    }
                    TutorCourseDetailInfo.ITEM_TYPE_SECOND -> {
                        val chapter = item.chapter
                        val tutors = item.tutors
                        helper.setText(R.id.tv_tutor_course_title, chapter?.title)
                                .setText(R.id.tv_tutor_course_name, "主讲导师：" + tutors?.name)
                                .setText(R.id.tv_tutor_course_people, "${chapter?.count}" + "人已学")

                        initWebview(helper.getView(R.id.webview_course_desc), chapter?.chapter_content)

                        helper.addOnClickListener(R.id.tv_tutor_extend)
                                .addOnClickListener(R.id.tv_tutor_close)
                    }
                    TutorCourseDetailInfo.ITEM_TYPE_THIRD -> {
                        val tutorInfo = item.tutors
                        Glide.with(mContext).load(tutorInfo?.img).apply(RequestOptions().circleCrop().error(R.mipmap.service_tutor_head)).into(helper.getView<View>(R.id.iv_tutor_face) as ImageView)
                        helper.setText(R.id.tv_tutor_profession, tutorInfo?.profession)
                                .addOnClickListener(R.id.tv_tutor_main_page)
                    }
                    TutorCourseDetailInfo.ITEM_TYPE_FOUR -> {
                        val position = helper.adapterPosition
                        if (position == 3) {
                            helper.setGone(R.id.tv_comment_tip, true)
                        } else {
                            helper.setGone(R.id.tv_comment_tip, false)
                        }

                        helper.setText(R.id.tv_tutor_comment_content, item.communityInfo?.content)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun initWebview(webView: WebView, data: String?) {


        val settings = webView.settings

        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//支持内容重新布局

        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null)

    }


    private fun initTutorData(linearLayout: LinearLayout, lessons: List<LessonInfo>?) {

        linearLayout.removeAllViews()

        lessons?.let {


            for (i in lessons.indices) {

                val lessonInfo = lessons[i]
                val childView = LayoutInflater.from(mContext).inflate(R.layout.layout_tutor_course_item, null)
                val view = childView.findViewById<RoundCornerImg>(R.id.screenshot_image)
                val tvTutorCourse = childView.findViewById<TextView>(R.id.tv_tutor_course)
                val rootView = childView.findViewById<LinearLayout>(R.id.rootView)
                val ivCourseLock = childView.findViewById<ImageView>(R.id.iv_tutor_course_lock)

                if (i == 0 || userId > 0) {
                    ivCourseLock.visibility = View.GONE
                } else {
                    ivCourseLock.visibility = View.VISIBLE
                }

                val layoutParams = rootView.layoutParams as LinearLayout.LayoutParams

                layoutParams.width = ScreenUtil.getWidth(mContext) / 2
                rootView.layoutParams = layoutParams


                Glide.with(mContext).load(lessonInfo.lesson_image).apply(RequestOptions().error(R.mipmap.index_tutor_example)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)).into(view)
                tvTutorCourse.text = lessonInfo.lesson_title

                childView.setOnClickListener { v ->

                    if (listener != null) {

                        if (i == 0 || !UserInfoHelper.instance.goToLogin(mContext)) {
                            listener?.onItemPlay(lessonInfo)
                            MobclickAgent.onEvent(mContext, "video_player_click", "课程视频播放点击")
                        }

                    }

                }

                linearLayout.addView(childView)
            }
        }
    }

    fun setOnItemPlayerListener(listener: OnItemPlayerListener) {
        this.listener = listener
    }

    interface OnItemPlayerListener {
        fun onItemPlay(lessonInfo: LessonInfo)
    }

}
