package com.yc.emotion.home.skill.ui.fragment


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.presenter.BasePresenter
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.message.ui.fragment.ExpressFragment
import kotlinx.android.synthetic.main.fragment_main_skill.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * Created by mayn on 2019/6/17.
 */

class SkillMainFragment : BaseFragment<BasePresenter<*, *>>() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_main_skill
    }

    override fun initViews() {


    }

    private fun netSwitchPagerData() {

        val arrays = resources.getStringArray(R.array.love_practice)

        val titleList = listOf(*arrays)

        initNavigator(titleList)
        val fragmentList = arrayListOf<Fragment>()

        fragmentList.add(PracticeTeachFragment())
        fragmentList.add(ExpressFragment())


        val commonMainPageAdapter = CommonMainPageAdapter(childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragmentList)

//        val mainT2NewPagerAdapter = MainT2PagerAdapter(childFragmentManager, titleList)
        skill_view_pager.adapter = commonMainPageAdapter
    }

    private fun initNavigator(titleList: List<String>?) {
        val commonNavigator = CommonNavigator(activity)
        //        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.isAdjustMode = true
        val navigatorAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titleList?.size ?: 0
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = titleList?.get(index)
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = Color.BLACK
                simplePagerTitleView.selectedColor = resources.getColor(R.color.app_color)
                simplePagerTitleView.setOnClickListener { v -> skill_view_pager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }

            override fun getTitleWeight(context: Context?, index: Int): Float {
                return when (index) {
                    0 -> 1.8f
                    1 -> 1.8f
                    else -> 1.0f
                }
            }

        }
        skill_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                resetNavigator(commonNavigator)
                //
                val pagerTitleView = commonNavigator.getPagerTitleView(position) as SimplePagerTitleView
                //                pagerTitleView.setTextSize(12);
                pagerTitleView.typeface = Typeface.DEFAULT_BOLD
            }
        })
        commonNavigator.adapter = navigatorAdapter
        skill_pager_tabs.navigator = commonNavigator
        ViewPagerHelper.bind(skill_pager_tabs, skill_view_pager)
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

    override fun lazyLoad() {
        netSwitchPagerData()
    }


}
