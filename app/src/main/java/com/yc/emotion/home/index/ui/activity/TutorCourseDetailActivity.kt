package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.danikula.videocache.HttpProxyCacheServer
import com.umeng.analytics.MobclickAgent
import com.video.player.lib.constants.VideoConstants
import com.video.player.lib.manager.VideoPlayerManager
import com.video.player.lib.manager.VideoWindowManager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.EmApplication
import com.yc.emotion.home.base.ui.activity.PayActivity
import com.yc.emotion.home.index.adapter.TutorCourseDetailAdapter
import com.yc.emotion.home.index.presenter.TutorCoursePresenter
import com.yc.emotion.home.index.ui.fragment.PaySuccWxFragment
import com.yc.emotion.home.index.ui.fragment.VipPayWayFragment
import com.yc.emotion.home.index.ui.widget.TutorCoursePopwindow
import com.yc.emotion.home.index.view.TutorCourseView
import com.yc.emotion.home.message.ui.fragment.CoursePayFragment
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.model.bean.event.EventBusWxPayResult
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_tutor_course_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import yc.com.rthttplibrary.util.LogUtil


/**
 *
 * Created by suns  on 2019/10/9 16:35.
 * 导师课程详情
 */
class TutorCourseDetailActivity : PayActivity(), TutorCourseView {


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
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_tutor_course_detail
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
//                                                    startActivity(Intent(this@TutorCourseDetailActivity, VipActivity::class.java))
                                                    showPay(lessonInfo)
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
            override fun onItemPlay(lessonInfo: LessonInfo?) {
                startPlayer(lessonInfo)
            }

            override fun onItemPay(lessonInfo: LessonInfo?) {
                showPay(lessonInfo)
            }
        })

        iv_tutor_back.clickWithTrigger { finish() }
        ll_tutor_buy.clickWithTrigger {

            MobclickAgent.onEvent(this, "course_detail_pay", "课程详情页购买")

            val coursePayFragment = CoursePayFragment.newInstance(mLessons as ArrayList<LessonInfo>?)
            coursePayFragment.setOnLessonItemClickListener(object : CoursePayFragment.OnLessonItemClickListener {
                override fun onLessonClick(lessonInfo: LessonInfo, position: Int) {
                    if (lessonInfo.need_pay == 0) {
                        startPlayer(lessonInfo)
                    } else {
                        if (!UserInfoHelper.instance.goToLogin(this@TutorCourseDetailActivity)) {
                            showPay(lessonInfo)
                        }
                    }
                }
            })
            coursePayFragment.show(supportFragmentManager, "")
        }

        iv_course_share.clickWithTrigger {
            //todo 分享
        }

        iv_course_collect.clickWithTrigger {
            //收藏
            collectCourse(chapterId)
        }
        ll_wx_advise.clickWithTrigger {
            //添加微信
            showToWxServiceDialog(tutorId = "$tutorId")
            MobclickAgent.onEvent(this, "course_detail_add_wx", "课程详情页添加微信")
        }
    }

    private fun showPay(lessonInfo: LessonInfo?) {
        val vipPayWayFragment = VipPayWayFragment()
        vipPayWayFragment.show(supportFragmentManager, "")
        vipPayWayFragment.setOnPayWaySelectListener(object : VipPayWayFragment.OnPayWaySelectListener {
            override fun onPayWaySelect(payway: String) {
                //                    nextOrders(payway, indexDoodsBean)

                val goodsInfo = GoodsInfo()

                lessonInfo?.let {
                    goodsInfo.m_price = it.m_price
                    goodsInfo.name = it.lesson_title
                    goodsInfo.id = it.good_id
                    nextOrders(payway, goodsInfo)
                }
//                courseInfo?.let {
//                    goodsInfo.m_price = it.price
//                    goodsInfo.name = it.title
//                    goodsInfo.id = it.goods_id
//                    nextOrders(payway, goodsInfo)
//                }
            }
        })
    }


    private fun nextOrders(payWayName: String, indexDoodsBean: GoodsInfo?) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        if (indexDoodsBean == null) return

        if (!UserInfoHelper.instance.goToLogin(this)) {
            (mPresenter as? TutorCoursePresenter)?.initOrders(payWayName, indexDoodsBean.m_price, indexDoodsBean.name, "${indexDoodsBean.id}")
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
    private var courseInfo: CourseInfo? = null

    private fun createData(data: TutorCourseDetailInfo) {
        tutorId = data.chapter?.tutor_id
        courseInfo = data.chapter
        tv_money.text = "¥${courseInfo?.price}"
        val hasPay = courseInfo?.has_buy
        if (hasPay == 0) {
            ll_tutor_buy.visibility = View.VISIBLE
        } else {
            ll_tutor_buy.visibility = View.GONE
        }

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

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun showOrderInfo(data: OrdersInitBean?, payWayName: String) {
        data?.let {
            val paramsBean = data.params
            Log.d("mylog", "onNetNext: payType == 0  Zfb   payType $payWayName")
            if (payWayName == "alipay") {
                //                    String info="alipay_sdk=alipay-sdk-php-20180705&app_id=2019051564672294&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A0.01%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22201905161657594587%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flove.bshu.com%2Fnotify%2Falipay%2Fdefault&sign_type=RSA2&timestamp=2019-05-16+16%3A57%3A59&version=1.0&sign=BRj%2FY6Bk319dZwNoHwWbYIKYZFJahg1TRgvhFf7ubJzFKZEIESnattbFnaGJ6wq6%2BmauaKZcGv83ianrZfw0R%2BMQ9OmbTPXjKYGZUMzdPNDV3NygmVMgM68vs6oeHyQOxsbx16L4ltGi%2BdEjPDsLWqlw8E1INukZMxV4EDbFl8ZlyzKYerY9YZR1dRtxscFXgG7npmyPp3mO%2BA%2BywZABb5sANxqBShG%2FgeGbE%2BG1hpkZUE4KYGV7rCC80dcBjODWPgj%2FKQtFUXnx5NzCfWIeUMcyc8UaeK%2FsxqyrMJmsFPQgCBYGR5HH1llIfQ8NJuitwhDnJTKMhqCgh03UG9j%2B%2BQ%3D%3D";
                //                    toZfbPay(info);
                toZfbPay(paramsBean.info)
            } else {
                toWxPay(paramsBean)
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventBusWxPayResult) {
        when (event.code) {
            0//支付成功
            ->
                //  微信支付成功
                showPaySuccessDialog(true, event.mess)
            -1//错误
            -> showPaySuccessDialog(false, event.mess)
            -2//用户取消
            -> showPaySuccessDialog(false, event.mess)
            else -> {
            }
        }
    }


    override fun onZfbPauResult(result: Boolean, des: String?) {
        showPaySuccessDialog(result, des)
    }


    private fun showPaySuccessDialog(result: Boolean, des: String?) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setCancelable(false)
        alertDialog.setTitle("提示")
        if (result) {

            val paySuccessFragment = PaySuccWxFragment()
            paySuccessFragment.show(supportFragmentManager, "")

            getCourseData(chapterId)
        }
        alertDialog.setMessage(des)

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定") { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
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
        //尝试返回
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

        var proxyUrl = lessonInfo?.lesson_url

        val proxy: HttpProxyCacheServer? = EmApplication.instance.getProxy()
        proxy?.let {
            proxyUrl = proxy.getProxyUrl(proxyUrl)
        }

        Glide.with(this).load(lessonInfo?.lesson_image).apply(RequestOptions().error(R.mipmap.efficient_course_example_pic)).into(videoPlayer.coverController.mVideoCover)
        videoPlayer.setVideoDisplayType(VideoConstants.VIDEO_DISPLAY_TYPE_ZOOM)
        videoPlayer.startPlayVideo(proxyUrl, lessonInfo?.lesson_title)
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