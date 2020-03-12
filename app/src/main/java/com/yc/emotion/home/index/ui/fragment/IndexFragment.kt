package com.yc.emotion.home.index.ui.fragment

import android.content.Context
import android.content.Intent
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kk.utils.ScreenUtil
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.base.ui.widget.RoundCornerImg
import com.yc.emotion.home.factory.MainFragmentFactory
import com.yc.emotion.home.index.adapter.IndexChoicenessAdapter
import com.yc.emotion.home.index.adapter.IndexCourseAdapter
import com.yc.emotion.home.index.adapter.IndexLiveAdapter
import com.yc.emotion.home.index.adapter.IndexTestAdapter
import com.yc.emotion.home.index.domain.bean.SexInfo
import com.yc.emotion.home.index.presenter.IndexPresenter
import com.yc.emotion.home.index.ui.activity.*
import com.yc.emotion.home.index.view.IndexView
import com.yc.emotion.home.model.bean.*
import com.yc.emotion.home.model.bean.event.NetWorkChangT1Bean
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.utils.*
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_main_community.*
import kotlinx.android.synthetic.main.fragment_main_index.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by mayn on 2019/4/23.
 */

class IndexFragment : BaseLazyFragment<IndexPresenter>(), IndexView {



    private var indexChoicenessAdapter: IndexChoicenessAdapter? = null


    private var indexCourseAdapter: IndexCourseAdapter? = null

    private var indexLiveAdapter: IndexLiveAdapter? = null

    private var sex by Preference(ConstantKey.SEX, 1)


    var mMainActivity: MainActivity? = null

    override fun onAttach(context: Context?) {
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


        val linearLayoutManager = LinearLayoutManager(mMainActivity)
        rcv_choiceness.layoutManager = linearLayoutManager
        rcv_choiceness.itemAnimator = DefaultItemAnimator()

        indexChoicenessAdapter = IndexChoicenessAdapter(null, true, isMore = false)

        rcv_choiceness.adapter = indexChoicenessAdapter


        index_course_recyclerView.layoutManager = GridLayoutManager(mMainActivity, 2)
        indexCourseAdapter = IndexCourseAdapter(null)
        index_course_recyclerView.adapter = indexCourseAdapter
        index_course_recyclerView.addItemDecoration(ItemDecorationHelper(mMainActivity, 15, 0))

        recyclerView_live.layoutManager = GridLayoutManager(mMainActivity, 2)
        indexLiveAdapter = IndexLiveAdapter(null)
        recyclerView_live.adapter = indexLiveAdapter
        recyclerView_live.addItemDecoration(ItemDecorationHelper(mMainActivity, 15, 10))


        initData()
        initListener()
//        showGuide()
        initToolbar()
        initMarqueeView()
    }


    private fun initMarqueeView() {
        UIUtils.setMarqueList(marqueeView)

    }


    private fun initListener() {


        mMainActivity?.let { ContextCompat.getColor(it, R.color.app_color) }?.let { swipeRefreshLayout.setColorSchemeColors(it) }
        swipeRefreshLayout.setOnRefreshListener {
            getIndexData()
        }

        ll_advise.setOnClickListener {
            MobclickAgent.onEvent(mMainActivity, "personal_tailor_id", "私人订制")
            startActivity(Intent(mMainActivity, ConsultAppointActivity::class.java))
        }


        indexChoicenessAdapter?.setOnItemClickListener { adapter, view, position ->
            val articleDetailInfo = indexChoicenessAdapter?.getItem(position)
            articleDetailInfo?.let {
                mMainActivity?.let { ArticleDetailActivity.startExampleDetailActivity(it, articleDetailInfo.id, articleDetailInfo.post_title, false) }
            }
        }

        tv_more_tutor.setOnClickListener { startActivity(Intent(mMainActivity, TutorListActivity::class.java)) }
        tv_more_test.setOnClickListener { startActivity(Intent(mMainActivity, EmotionTestMainActivity::class.java)) }
        tv_more_course.setOnClickListener { startActivity(Intent(mMainActivity, EfficientCourseActivity::class.java)) }
        ll_index_search.setOnClickListener { switchSearch() }
        iv_vip.setOnClickListener {
            MobclickAgent.onEvent(mMainActivity, "home_vip_id", "首页vip")
            startActivity(Intent(mMainActivity, VipActivity::class.java))
        }
        tv_more_article.setOnClickListener { startActivity(Intent(mMainActivity, MoreArticleActivity::class.java)) }
        iv_index_search.setOnClickListener { startActivity(Intent(mMainActivity, EmotionSearchActivity::class.java)) }
        iv_index_vip.setOnClickListener { startActivity(Intent(mMainActivity, VipActivity::class.java)) }


        indexCourseAdapter?.setOnItemClickListener { adapter, view, position ->
            val courseInfo = indexCourseAdapter?.getItem(position)
            courseInfo?.let {
                mMainActivity?.let { it1 -> TutorCourseDetailActivity.startActivity(it1, courseInfo.id) }
            }

        }
        marqueeView.setOnItemClickListener { position, textView ->
            switchSearch()
        }

        indexLiveAdapter?.setOnItemClickListener { adapter, view, position ->
            val liveInfo = indexLiveAdapter?.getItem(position)
            liveInfo?.let {
                if (liveInfo.state == 1) {
                    LiveWebActivity.startActivity(mMainActivity,liveInfo.liveUrl)
//                    startActivity(Intent(mMainActivity, LiveWebActivity::class.java))
                }
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
            val offset = Math.abs(verticalOffset) //目的是将负数转换为绝对正数；
            //标题栏的渐变
            mMainActivity?.changeAlpha(resources.getColor(R.color.white)
                    , Math.abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange)?.let { toolbar.setBackgroundColor(it) }

            /**
             * 当前最大高度偏移值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */
            if (offset <= appBarLayout.totalScrollRange / 2) {
                toolbar.title = ""
                toolbar.alpha = (appBarLayout.totalScrollRange / 2 - offset * 1.0f) / (appBarLayout.totalScrollRange / 2)
                toolbar.visibility = View.GONE

                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (offset > appBarLayout.totalScrollRange / 2) {
                val floate = (offset - appBarLayout.totalScrollRange / 2) * 1.0f / (appBarLayout.totalScrollRange / 2)
                toolbar.alpha = floate
                toolbar.visibility = View.VISIBLE

            }


        })
    }


    private fun initData() {

//        mPresenter.getCache()
        getSexData()
//        initLiveData()

    }


    private fun initIcon(t: List<SexInfo>) {

        t.let {
            t.forEach { item ->

                val childView = LayoutInflater.from(mMainActivity).inflate(R.layout.index_content_icon, null)
                val ivTutorPic = childView.findViewById<ImageView>(R.id.iv_index_icon)


                val layoutParams = rootView.layoutParams

                layoutParams.width = ScreenUtil.getWidth(mMainActivity) / 3
                rootView.layoutParams = layoutParams
                ivTutorPic.setImageResource(item.imgId)

                childView.setOnClickListener {
                    if (item.aClass == MainActivity::class.java) {
                        mMainActivity?.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3)

                    } else {
                        val intent = Intent(mMainActivity, item.aClass)
                        startActivity(intent)
                    }

                    item.umId?.let {
                        MobclickAgent.onEvent(mMainActivity, item.umId, item.umDesc)
                    }

                }

                ll_top_scroll_container.addView(childView)
            }
        }


    }

    private fun initBanner(banners: List<BannerInfo>?) {


        banners?.let {
            val paths = arrayListOf<String>()
            banners.forEach {
                paths.add(it.img)
            }

            //设置banner样式
            index_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            //设置图片加载器
            index_banner.setImageLoader(GlideImageLoader())
            //设置图片集合
            index_banner.setImages(paths)
            //设置banner动画效果
            index_banner.setBannerAnimation(Transformer.Default)

            //设置自动轮播，默认为true
            index_banner.isAutoPlay(true)
            //设置轮播时间
            index_banner.setDelayTime(2500)
            //设置指示器位置（当banner模式中有指示器时）
            index_banner.setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            index_banner.setOnBannerListener {
                //todo banner点击事件

            }
            index_banner.start()
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

                mMainActivity?.let {
                    if (!it.isDestroyed)

                        Glide.with(it).load(item.img).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).into(ivTutorPic)
                }
                tvTutorName.text = item.name
                tvTutorJob.text = item.profession


                childView.setOnClickListener {
                    mMainActivity?.let { it1 -> TutorDetailActivity.startActivity(it1, item.tutorId) }
                }

                ll_scroll_container?.addView(childView)
            }

        }

    }


    private fun initCourseData(lessonChapter: List<CourseInfo>?) {
        lessonChapter?.let {
            var newLesson = lessonChapter
            if (lessonChapter.size > 4) {
                newLesson = lessonChapter.subList(0, 4)
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


    override fun lazyLoad() {
        showGuide2()
    }


    private fun getIndexData() {
        mPresenter.getIndexData()
        mPresenter.getIndexLiveList()

    }


    private fun getSexData() {
        mPresenter.getSexData(sex)
    }


    private fun setData(indexInfo: IndexInfo) {
        initTutorData(indexInfo.tutors)//初始化导师数据
        initArticleData(indexInfo.article)//初始化文章数据
        initBanner(indexInfo.banners)//初始化banner数据
        initViewPager(indexInfo.psych_test)//初始化测试数据

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


    override fun showIcon(t: List<SexInfo>) {
        initIcon(t)
    }


    override fun showIndexCaches(detailInfos: IndexInfo) {
        setData(detailInfos)
    }

    override fun showIndexLiveInfos(liveInfos: List<LiveInfo>) {
        indexLiveAdapter?.setNewData(liveInfos)
    }

    private fun initLiveData() {


        val json = AssetUtils.getAssetData(context)

        Log.e("TAG", json)
        val infoWrapper = JSON.parseObject(json, LiveInfoWrapper::class.java)
        val liveInfos = infoWrapper.data



    }
}
