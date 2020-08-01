package com.yc.emotion.home.index.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.kk.utils.ScreenUtil
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.YcApplication
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.fragment.common.SuccessFragment
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.index.adapter.IndexChoicenessAdapter
import com.yc.emotion.home.index.adapter.IndexCourseAdapter
import com.yc.emotion.home.index.adapter.IndexLiveAdapter
import com.yc.emotion.home.index.adapter.IndexTestAdapter
import com.yc.emotion.home.index.presenter.IndexPresenter
import com.yc.emotion.home.index.ui.activity.*
import com.yc.emotion.home.index.view.IndexView
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.model.bean.event.IndexRefreshEvent
import com.yc.emotion.home.model.bean.event.NetWorkChangT1Bean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.pay.ui.fragment.PaySuccessFragment
import com.yc.emotion.home.utils.*
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_main_index.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs

/**
 * Created by mayn on 2019/4/23.
 */

class IndexFragment : BaseFragment<IndexPresenter>(), IndexView {


    private var indexChoicenessAdapter: IndexChoicenessAdapter? = null


    private var indexCourseAdapter: IndexCourseAdapter? = null

    private var indexLiveAdapter: IndexLiveAdapter? = null

    private var sex by Preference(ConstantKey.SEX, 1)

    private val handler: Handler = Handler()


    private lateinit var mMainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_main_index
    }


    override fun initViews() {


        mPresenter = IndexPresenter(activity, this)
        tv_live_title.text = HtmlCompat.fromHtml("热门直播 <font color='#fa4a65'>LIVE</font>", FROM_HTML_MODE_LEGACY)

        val linearLayoutManager = LinearLayoutManager(mMainActivity)
        rcv_choiceness.layoutManager = linearLayoutManager
        rcv_choiceness.itemAnimator = DefaultItemAnimator()

        indexChoicenessAdapter = IndexChoicenessAdapter(null, true, isMore = false)

        rcv_choiceness.adapter = indexChoicenessAdapter


        index_course_recyclerView.layoutManager = GridLayoutManager(mMainActivity, 1)
        indexCourseAdapter = IndexCourseAdapter(null)
        index_course_recyclerView.adapter = indexCourseAdapter
        index_course_recyclerView.addItemDecoration(ItemDecorationHelper(mMainActivity, 15, 0))

        recyclerView_live.layoutManager = LinearLayoutManager(mMainActivity)
        indexLiveAdapter = IndexLiveAdapter(null)
        recyclerView_live.adapter = indexLiveAdapter
        recyclerView_live.addItemDecoration(ItemDecorationHelper(mMainActivity, 10))

//        indexCourseAdapter.setLoadMoreView()
//        indexCourseAdapter.setAutoLoadMoreSize()


        initListener()
//        showGuide()
        initToolbar()
        initMarqueeView()


    }


    private fun initMarqueeView() {
        UIUtils.setMarqueList(marqueeView)

    }


    private fun initListener() {


        mMainActivity.let { ContextCompat.getColor(it, R.color.app_color) }.let { swipeRefreshLayout.setColorSchemeColors(it) }
        swipeRefreshLayout.setOnRefreshListener {
            getIndexData()
        }

        ll_advise.clickWithTrigger {
            MobclickAgent.onEvent(mMainActivity, "personal_tailor_id", "私人订制")
            startActivity(Intent(mMainActivity, ConsultAppointActivity::class.java))
        }


        indexChoicenessAdapter?.setOnItemClickListener { adapter, view, position ->
            val articleDetailInfo = indexChoicenessAdapter?.getItem(position)
            articleDetailInfo?.let {
                mMainActivity.let { ArticleDetailActivity.startExampleDetailActivity(it, articleDetailInfo.id, articleDetailInfo.post_title, false) }
            }
        }

        tv_more_tutor.clickWithTrigger { startActivity(Intent(mMainActivity, TutorListActivity::class.java)) }
        tv_more_test.clickWithTrigger { startActivity(Intent(mMainActivity, EmotionTestMainActivity::class.java)) }
        tv_more_course.clickWithTrigger { startActivity(Intent(mMainActivity, EfficientCourseActivity::class.java)) }
        ll_index_search.clickWithTrigger { switchSearch() }
        iv_vip.clickWithTrigger {
            MobclickAgent.onEvent(mMainActivity, "home_vip_id", "首页vip")
            startActivity(Intent(mMainActivity, VipActivity::class.java))
        }
        tv_more_article.clickWithTrigger { startActivity(Intent(mMainActivity, MoreArticleActivity::class.java)) }
        iv_index_search.clickWithTrigger { startActivity(Intent(mMainActivity, EmotionSearchActivity::class.java)) }
        iv_index_vip.clickWithTrigger { startActivity(Intent(mMainActivity, VipActivity::class.java)) }
        iv_repel.clickWithTrigger {
            MonographActivity.startActivity(mMainActivity, "恋爱脱单", "41")

        }
        iv_save.clickWithTrigger {
            MonographActivity.startActivity(mMainActivity, title = "狙击挽回", series = "43")
        }

        indexCourseAdapter?.setOnItemClickListener { adapter, view, position ->
            val courseInfo = indexCourseAdapter?.getItem(position)
            courseInfo?.let {
                mMainActivity.let { it1 -> TutorCourseDetailActivity.startActivity(it1, courseInfo.id) }
            }

        }
        marqueeView.setOnItemClickListener { position, textView ->
            switchSearch()
        }

        indexLiveAdapter?.setOnItemClickListener { adapter, view, position ->


            val liveInfo = indexLiveAdapter?.getItem(position)
            liveInfo?.let {
                if (!TextUtils.isEmpty(liveInfo.record_url)) {
                    LiveVideoActivity.startActivity(mMainActivity, liveInfo)

                } else {
//                    mPresenter?.getOnlineLiveList()
//                    when (it.status) {
//                        2 -> {

//                    handler.postDelayed({
//                        mLiveInfo?.let { myit ->
                    if (it.status == 1) {
                        LiveLookActivity.startActivity(mMainActivity, it.roomId, it.start_time, it.end_time)
                    } else if (it.status == 2) {
                        LiveNoticeActivity.startActivity(mMainActivity, it)
                    }
                    //                            }
                }
//                    }, 1000)
//                        }
//                        1 -> {
//                            LiveLookActivity.startActivity(mMainActivity, it.roomId, it.start_time, it.end_time)
//                        }
//                        else -> {
//                        }
//                    }


//                }

            }


        }

    }

    private fun switchSearch() {
        MobclickAgent.onEvent(mMainActivity, "home_search_id", "首页搜索")
        startActivity(Intent(mMainActivity, EmotionSearchActivity::class.java))
    }


    private fun initToolbar() {

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, verticalOffset ->
            //verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
            val offset = abs(verticalOffset) //目的是将负数转换为绝对正数；
            //标题栏的渐变
            mMainActivity.changeAlpha(resources.getColor(R.color.white)
                    , abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange).let { rl_index_toolbar_container.setBackgroundColor(it) }

//            Log.e("tag", "offset=$offset")
//            Log.e("tag", "totalScrollRange=${appBarLayout.totalScrollRange / 2}")
            /**
             * 当前最大高度偏移值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */
            if (offset <= appBarLayout.totalScrollRange / 2) {
                toolbar.title = ""
                toolbar.alpha = (appBarLayout.totalScrollRange / 2 - offset * 1.0f) / (appBarLayout.totalScrollRange / 2)
//                toolbar.visibility = View.GONE
//                toolbar1.visibility = View.VISIBLE

                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (offset > appBarLayout.totalScrollRange / 2) {
                val floate = (offset - appBarLayout.totalScrollRange / 2) * 1.0f / (appBarLayout.totalScrollRange / 2)
                toolbar.alpha = floate
//                toolbar.visibility = View.VISIBLE
//                toolbar1.visibility = View.GONE
            }


        })
    }


    private fun initBanner(banners: List<BannerInfo>?) {


        banners?.let {
            val paths = arrayListOf<String>()
            banners.forEach {
                paths.add(it.img)
            }

            index_banner?.let {
                //设置banner样式
                it.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                //设置图片加载器
                it.setImageLoader(GlideImageLoader())
                //设置图片集合
                it.setImages(paths)
                //设置banner动画效果
                it.setBannerAnimation(Transformer.Default)

                //设置自动轮播，默认为true
                it.isAutoPlay(true)
                //设置轮播时间
                it.setDelayTime(2500)
                //设置指示器位置（当banner模式中有指示器时）
                it.setIndicatorGravity(BannerConfig.CENTER)
                //banner设置方法全部调用完毕时最后调用
                it.setOnBannerListener {
                    //todo banner点击事件

                }
                it.start()
            }

        }

    }


    private fun initTutorData(tutors: List<TutorInfo>?) {

        tutors?.let {
            ll_scroll_container?.removeAllViews()
            tutors.forEach { item ->
                val childView = LayoutInflater.from(mMainActivity).inflate(R.layout.index_tutor_item, null)
                val ivTutorPic = childView.findViewById<RoundCornerImg>(R.id.screenshot_image)
                val tvTutorName = childView.findViewById<TextView>(R.id.tv_tutor_name)
                val tvTutorJob = childView.findViewById<TextView>(R.id.tv_tutor_job)
                val rootView = childView.findViewById<LinearLayout>(R.id.rootView)

                val layoutParams = rootView.layoutParams

                layoutParams.width = ScreenUtil.getWidth(mMainActivity) / 3
                rootView.layoutParams = layoutParams

                mMainActivity.let {
                    if (!it.isDestroyed)

                        Glide.with(it).load(item.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).into(ivTutorPic)
                }
                tvTutorName.text = item.name
                tvTutorJob.text = item.profession


                childView.clickWithTrigger {
                    mMainActivity.let { it1 -> TutorDetailActivity.startActivity(it1, item.tutorId) }
                }

                ll_scroll_container?.addView(childView)
            }

        }

    }


    private fun initCourseData(lessonChapter: List<CourseInfo>?) {
        lessonChapter?.let {
            var newLesson = lessonChapter
            if (lessonChapter.size > 1) {
                newLesson = lessonChapter.subList(0, 1)
            }
            indexCourseAdapter?.setNewData(newLesson)
        }

    }

    private fun initArticleData(article: List<ArticleDetailInfo>?) {
        indexChoicenessAdapter?.setNewData(article)
    }


    private fun initViewPager(psychtTest: List<EmotionTestInfo>?) {

        index_ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        psychtTest?.let {
            val adapter = IndexTestAdapter(mMainActivity, psychtTest)
            index_ultraViewPager.adapter = adapter
            index_ultraViewPager.setMultiScreen(0.45f)
//        index_ultraViewPager.setItemRatio(1.0)
//        index_ultraViewPager.setRatio(2.0f)
//        index_ultraViewPager.setMaxHeight(800)
            index_ultraViewPager.setAutoMeasureHeight(true)

            index_ultraViewPager.setPageTransformer(false, UltraScaleTransformer())

            index_ultraViewPager.currentItem = 1
        }


    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        marqueeView.startFlipping()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        marqueeView.stopFlipping()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(netWorkChangBean: NetWorkChangT1Bean) {  //无网状态
        val connectionTypeList = netWorkChangBean.connectionTypeList
        if (connectionTypeList == null || connectionTypeList.size == 0) {
            if (main_t1_not_net.visibility != View.VISIBLE) {
                main_t1_not_net.visibility = View.VISIBLE
            }
        } else {
            if (main_t1_not_net.visibility != View.GONE) {
                main_t1_not_net.visibility = View.GONE
                lazyLoad()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLiveEnd(event: IndexRefreshEvent) {
        val msg = event.message
//        Log.e("tag", msg)
        mPresenter?.getOnlineLiveList()

    }


    override fun onResume() {
        super.onResume()
        if (YcApplication.isBackToForeground) {
            YcApplication.isBackToForeground = false
//            Log.e("TAG", "onResume")
            mPresenter?.getOnlineLiveList()
        }

    }

    override fun lazyLoad() {
//        showGuide2()
    }


    private fun getIndexData() {
        mPresenter?.getIndexData()

        mPresenter?.getOnlineLiveList()
    }


    private var mPsychtTest: List<EmotionTestInfo>? = null
    private fun setData(indexInfo: IndexInfo) {
        initTutorData(indexInfo.tutors)//初始化导师数据
        initArticleData(indexInfo.article)//初始化文章数据
        initBanner(indexInfo.banners)//初始化banner数据
        val psychtTest = indexInfo.psych_test
        if (mPsychtTest == null) {
            mPsychtTest = psychtTest
            initViewPager(psychtTest)//初始化测试数据
        }


    }


    private fun showGuide() {
        NewbieGuide.with(activity)
                .setLabel("guide1")
                .alwaysShow(false)
                .addGuidePage(GuidePage.newInstance()
                        .setEverywhereCancelable(false)
                        .setLayoutRes(R.layout.layout_community_guide)
                        .setOnLayoutInflatedListener { view, controller ->
                            view.findViewById<ImageView>(R.id.iv_know).setOnClickListener {
                                controller.remove()
                                showGuide2()
                            }
                        })
                .show()
    }

    private fun showGuide2() {
        NewbieGuide.with(activity)
                .setLabel("guide2")
                .alwaysShow(false)
                .addGuidePage(GuidePage.newInstance()
                        .setEverywhereCancelable(false)
                        .setLayoutRes(R.layout.layout_index_guide)
                        .setOnLayoutInflatedListener { view, controller ->
                            view.findViewById<ImageView>(R.id.iv_know).setOnClickListener { controller.remove() }
                        })
                .show()
    }

    override fun showIndexInfo(indexInfo: IndexInfo) {
        setData(indexInfo)

        initCourseData(indexInfo.lesson_chapter)//初始化课程数据
    }

    override fun onComplete() {
        swipeRefreshLayout?.let {
            if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
        }
    }


    override fun showIndexCaches(detailInfos: IndexInfo) {
        setData(detailInfos)
    }

    private var mLiveInfo: LiveInfo? = null
    override fun showIndexLiveInfos(data: List<LiveInfo>) {
        var liveInfos = data
        if (data.size > 2) {
            liveInfos = data.subList(0, 2)
        }
        this.mLiveInfo = liveInfos[0]
        indexLiveAdapter?.setNewData(liveInfos)
    }


}
