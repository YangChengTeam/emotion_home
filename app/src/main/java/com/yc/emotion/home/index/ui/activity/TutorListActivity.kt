package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.index.ui.fragment.TutorListFragment
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.presenter.TutorPresenter
import com.yc.emotion.home.index.view.TutorView
import com.yc.emotion.home.utils.CommonInfoHelper
import kotlinx.android.synthetic.main.activity_course_efficient.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import rx.Subscriber

/**
 *
 * Created by suns  on 2019/10/10 17:50.
 */
class TutorListActivity : BaseSameActivity(), TutorView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_course_efficient
    }


    override fun initViews() {
        mPresenter = TutorPresenter(this, this)
        tv_efficient.visibility = View.GONE

        getTutorCategory()

    }


    private fun netSwitchPagerData(datas: List<CourseInfo>) {

        val titleList = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()

        datas.let {

            for ((index, value) in datas.withIndex()) {
                titleList.add(value.cat_name)

                val tutorListFragment = TutorListFragment.newInstance(value.cat_id, index)
                fragments.add(tutorListFragment)
            }

        }

        initNavigator(titleList)

        val efficientCourseMainAdapter = CommonMainPageAdapter(supportFragmentManager, titleList, fragments)
        viewPager_efficient.adapter = efficientCourseMainAdapter

//        viewPager_efficient.setOffscreenPageLimit(2)
        viewPager_efficient.currentItem = 0
    }

    private fun initNavigator(titleList: List<String>) {
        val commonNavigator = CommonNavigator(this)
        //        commonNavigator.setScrollPivotX(0.65f);
//        commonNavigator.isAdjustMode = true
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
                simplePagerTitleView.setOnClickListener { v -> viewPager_efficient.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 40.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                //                indicator.setYOffset(-5f);
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }


        }
        commonNavigator.adapter = navigatorAdapter
        magicIndicator_efficient.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator_efficient, viewPager_efficient)

        viewPager_efficient.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(i: Int) {
                resetNavigator(commonNavigator)

                val pagerTitleView = commonNavigator.getPagerTitleView(i) as SimplePagerTitleView
                //                pagerTitleView.setTextSize(12);
                pagerTitleView.typeface = Typeface.DEFAULT_BOLD
            }


        })

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


    private fun getTutorCategory() {

        (mPresenter as? TutorPresenter)?.getTutorCategory()

    }

    override fun showTutorCategory(data: List<CourseInfo>?) {
        data?.let {
            netSwitchPagerData(data)
        }

    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return "明星导师"
    }
}