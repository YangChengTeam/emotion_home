package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.presenter.EmotionTestPresenter
import com.yc.emotion.home.index.ui.fragment.EmotionTestFragment
import com.yc.emotion.home.index.view.EmotionTestView
import com.yc.emotion.home.model.bean.CourseInfo
import kotlinx.android.synthetic.main.activity_course_efficient.*
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
 * Created by suns  on 2019/10/11 08:52.
 */
class EmotionTestMainActivity : BaseSameActivity(), EmotionTestView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_course_efficient
    }

    override fun initViews() {

        mPresenter = EmotionTestPresenter(this, this)

        ll_viewpager_container.visibility = View.VISIBLE
        ll_fl_container.visibility = View.GONE
        tv_efficient.visibility = View.GONE
        if (ll_fl_container.visibility == View.VISIBLE) {
            initFragment()
        } else if (ll_viewpager_container.visibility == View.VISIBLE) {
            getTestCategory()
        }

//        tv_efficient.setOnClickListener(this)
    }

    private fun initFragment() {

        val bt = supportFragmentManager.beginTransaction()
        bt.replace(R.id.fl_container, EmotionTestFragment())
        bt.commit()

    }


    private fun netSwitchPagerData(list: ArrayList<CourseInfo>?) {


        val titleList = arrayListOf<String>()

        val fragments = arrayListOf<Fragment>()
        list?.let {
            list.forEach {
                titleList.add(it.cat_name)

                val emotionTestFragment = EmotionTestFragment()

                val bundle = Bundle()
                bundle.putString("cat_id", it.cat_id)


                emotionTestFragment.arguments = bundle

                fragments.add(emotionTestFragment)
            }
        }



        initNavigator(titleList)


        val commonMainPageAdapter = CommonMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragments)
        viewPager_efficient.adapter = commonMainPageAdapter
//        mViewPager.setOffscreenPageLimit(2)
        viewPager_efficient.currentItem = 0
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
                simplePagerTitleView.normalColor = resources.getColor(R.color.black)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.app_color)
                simplePagerTitleView.setOnClickListener { v -> viewPager_efficient.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context,30.0).toFloat()
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


    override fun isSupportSwipeBack(): Boolean {
        return false
    }


    private fun getTestCategory() {
        (mPresenter as? EmotionTestPresenter)?.getTestCategoryInfos()
    }

    override fun showEmotionCategorys(list: ArrayList<CourseInfo>?) {
        netSwitchPagerData(list)
    }


    override fun offerActivityTitle(): String {
        return "情感测试"
    }
}