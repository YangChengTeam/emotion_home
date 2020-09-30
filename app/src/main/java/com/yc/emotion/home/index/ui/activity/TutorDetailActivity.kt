package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.adapter.TutorTagAdapter
import com.yc.emotion.home.index.presenter.TutorPresenter
import com.yc.emotion.home.index.ui.fragment.TutorCourseFragment
import com.yc.emotion.home.index.ui.fragment.TutorDetailCommentFragment
import com.yc.emotion.home.index.ui.fragment.TutorDetailDescFragment
import com.yc.emotion.home.index.view.TutorView
import com.yc.emotion.home.mine.ui.activity.ShareActivity
import com.yc.emotion.home.model.bean.TutorDetailInfo
import com.yc.emotion.home.model.bean.TutorInfo
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_tutor_detail.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 *
 * Created by suns  on 2019/10/9 16:35.
 * 导师详情
 */
class TutorDetailActivity : BaseSameActivity(), TutorView {


    private var tutorId: String? = null
    private var tutorTagAdapter: TutorTagAdapter? = null

    companion object {
        fun startActivity(context: Context, tutor_id: String) {
            val intent = Intent(context, TutorDetailActivity::class.java)
            intent.putExtra("tutor_id", tutor_id)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_tutor_detail
    }


    override fun initViews() {

        mPresenter = TutorPresenter(this, this)
        tutorId = intent?.getStringExtra("tutor_id")


        val layoutManager = FlexboxLayoutManager(this)

        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP

        rcv_tutor_detail.layoutManager = layoutManager

        tutorTagAdapter = TutorTagAdapter(null)
        rcv_tutor_detail.adapter = tutorTagAdapter



        getData()
        initListener()
    }

    private fun initListener() {


        iv_tutor_back.clickWithTrigger { finish() }
//        ll_tutor_buy.setOnClickListener { ToastUtils.showCenterToast("购买课程") }
        rl_free_consult.clickWithTrigger { showToWxServiceDialog(tutorId = tutorId) }
        rl_buy_service.clickWithTrigger {
            MobclickAgent.onEvent(this, "purchase_service_id", "导师页面购买服务")
            TutorServiceListActivity.startActivity(this, tutorId)
        }
        ll_tutor_detail_aptitude.clickWithTrigger {
            val intent = Intent(this, TutorAptitudeActivity::class.java)
            intent.putExtra("tutor_id", tutorId)
            startActivity(intent)
        }
        iv_tutor_share.clickWithTrigger {
            startActivity(Intent(this, ShareActivity::class.java))
        }

    }

    private fun netSwitchPagerData(tutor: TutorInfo?) {

        val arrays = resources.getStringArray(R.array.tutor_detail_array)


        val titleList = listOf(*arrays)

        initNavigator(titleList)

        val fragments = arrayListOf<Fragment>()


        repeat(titleList.size) {

            when (it) {
                0 -> {
                    val tutorDetailDescFragment = TutorDetailDescFragment()
                    val bundle = Bundle()
                    bundle.putString("content", tutor?.content)

                    tutorDetailDescFragment.arguments = bundle
                    fragments.add(tutorDetailDescFragment)
                }
                1 -> {
                    val courseFragment = TutorCourseFragment()
                    val bundle = Bundle()
                    bundle.putString("tutor_id", tutorId)

                    courseFragment.arguments = bundle

                    fragments.add(courseFragment)
                }
                2 -> fragments.add(TutorDetailCommentFragment())
            }
        }

        val efficientCourseMainAdapter = CommonMainPageAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragments)
        viewPager_tutor_detail.adapter = efficientCourseMainAdapter
//        mViewPager.setOffscreenPageLimit(2)
        viewPager_tutor_detail.currentItem = 0
    }

    private fun initNavigator(titleList: List<String>) {
        val commonNavigator = CommonNavigator(this)
        //        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.isAdjustMode = true
        val navigatorAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titleList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //                Log.e(TAG, "getTitleView: " + index);

                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = titleList[index]
                if (index == 0) simplePagerTitleView.typeface = Typeface.DEFAULT_BOLD
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = resources.getColor(R.color.gray_999)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.gray_222222)
                simplePagerTitleView.setOnClickListener { v -> viewPager_tutor_detail.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 20.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                //                indicator.setYOffset(-5f);
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }


        }
        commonNavigator.adapter = navigatorAdapter
        magicIndicator_tutor_detail.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator_tutor_detail, viewPager_tutor_detail)

        viewPager_tutor_detail.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(i: Int) {
                resetNavigator(commonNavigator)

                val pagerTitleView = commonNavigator.getPagerTitleView(i) as SimplePagerTitleView
                //                pagerTitleView.setTextSize(12);
                pagerTitleView.typeface = Typeface.DEFAULT_BOLD
            }


        })

    }

    private fun initData(tutorInfo: TutorInfo?) {
        Glide.with(this).load(tutorInfo?.bg_img).apply(RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA).centerInside()).thumbnail(0.1f).into(iv_tutor_detail)
        tv_tutor_detail_name.text = tutorInfo?.name

        val profession = tutorInfo?.profession
        var professions = profession?.split("，")
        if (professions?.size == 1) professions = profession?.split("；")
        tutorTagAdapter?.setNewData(professions)
        val star = tutorInfo?.star
        if (star == 0) {
            tv_tutor_star.visibility = View.GONE
        } else {
            tv_tutor_star.visibility = View.VISIBLE
            setStar(star)
        }

    }

    private fun setStar(star: Int?) {
        when (star) {
            1 -> tv_tutor_star.setImageResource(R.mipmap.star_one)
            2 -> tv_tutor_star.setImageResource(R.mipmap.star_two)
            3 -> tv_tutor_star.setImageResource(R.mipmap.star_three)
            4 -> tv_tutor_star.setImageResource(R.mipmap.star_four)
            5 -> tv_tutor_star.setImageResource(R.mipmap.star_five)
        }
    }


    private fun resetNavigator(commonNavigator: CommonNavigator) {
        val titleContainer = commonNavigator.titleContainer
        val childCount = titleContainer.childCount
        for (i in 0 until childCount) {
            val pagerTitleView = titleContainer.getChildAt(i) as SimplePagerTitleView
            pagerTitleView.typeface = Typeface.DEFAULT

            //            pagerTitleView.setTextSize(20);
        }

    }


    override fun isSupportSwipeBack(): Boolean {
        return false
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

    fun getData() {

        (mPresenter as? TutorPresenter)?.getTutorDetailInfo(tutorId)

    }

    override fun showTutorDetailInfo(data: TutorDetailInfo?) {
        data?.let {
            netSwitchPagerData(data.tutor)
            initData(data.tutor)
        }
    }
}