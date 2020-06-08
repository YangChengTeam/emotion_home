package com.yc.emotion.home.mine.ui.activity

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.mine.ui.fragment.CollectArticleFragment
import com.yc.emotion.home.mine.ui.fragment.CollectCourseFragment
import com.yc.emotion.home.mine.ui.fragment.CollectVerbalTalkFragment
import kotlinx.android.synthetic.main.activity_collect.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.*

class CollectActivity : BaseSameActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_collect
    }

    override fun initViews() {


        netSwitchPagerData()
    }


    private fun netSwitchPagerData() {


        val array = resources.getStringArray(R.array.collect_array)

        val titleLists = Arrays.asList(*array)
        //        titleLists.add("问答");

        initNavigator(titleLists)

        val fragmentList = arrayListOf<Fragment>()

        repeat(titleLists.size) {
            fragmentList.add(CollectCourseFragment())
//            fragmentList.add(CollectServiceFragment())

            fragmentList.add(CollectVerbalTalkFragment())
            fragmentList.add(CollectArticleFragment())
        }

        val commonMainPageAdapter = CommonMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleLists, fragmentList)

//        val collectPagerAdapter = CollectPagerAdapter(supportFragmentManager, titleLists, fragmentList)
        collect_view_pager.adapter = commonMainPageAdapter
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
                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = titleList[index]
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = Color.BLACK
                simplePagerTitleView.selectedColor = resources.getColor(R.color.red_crimson)
                simplePagerTitleView.setOnClickListener { v -> collect_view_pager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }


        }
        commonNavigator.adapter = navigatorAdapter
        collect_pager_tabs.navigator = commonNavigator
        ViewPagerHelper.bind(collect_pager_tabs, collect_view_pager)
        collect_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                resetNavigator(commonNavigator)
                //
                val pagerTitleView = commonNavigator.getPagerTitleView(position) as SimplePagerTitleView
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

    override fun offerActivityTitle(): String {
        return "我的收藏"
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

}
