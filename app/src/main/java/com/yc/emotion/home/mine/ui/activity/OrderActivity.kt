package com.yc.emotion.home.mine.ui.activity

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.mine.ui.fragment.CourseOrderFragment
import com.yc.emotion.home.mine.ui.fragment.ServiceOrderFragment
import kotlinx.android.synthetic.main.activity_order.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.util.*

/**
 *
 * Created by suns  on 2019/10/17 09:34.
 */
class OrderActivity : BaseSameActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_order
    }

    override fun initViews() {
        netSwitchPagerData()
    }


    private fun netSwitchPagerData() {

        val arrays = resources.getStringArray(R.array.order_array)


        val titleList = listOf(*arrays)

        initNavigator(titleList)

        val fragments = arrayListOf<Fragment>()
        repeat(titleList.size) {
            //            Log.e("TAG", it)
            if (it == 0) fragments.add(CourseOrderFragment())
            else if (it == 1)
                fragments.add(ServiceOrderFragment())
        }

        val commonMainPageAdapter = CommonMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragments)
        viewPager_order.adapter = commonMainPageAdapter
//        mViewPager.setOffscreenPageLimit(2)
        viewPager_order.currentItem = 0
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
                simplePagerTitleView.setOnClickListener { v -> viewPager_order.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 25.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                //                indicator.setYOffset(-5f);
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }


        }
        commonNavigator.adapter = navigatorAdapter
        magicIndicator_order.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator_order, viewPager_order)

        viewPager_order.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
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


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return "我的订单"
    }
}