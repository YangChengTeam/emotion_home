package com.yc.emotion.home.index.ui.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.base.constant.Constant
import kotlinx.android.synthetic.main.fragment_emotion_search_result_fragment.*
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
 * Created by suns  on 2019/10/28 11:23.
 */
class EmotionSearchResultFragment : BaseEmotionSearchFragment() {


    companion object {
        fun newInstance(keyword: String): EmotionSearchResultFragment {
            val fragment = EmotionSearchResultFragment()


            fragment.fragmentTag = Constant.SEARCH_RESULT

            val bundle = Bundle()

            bundle.putString("keyword", keyword)


            fragment.arguments = bundle


            return fragment

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_emotion_search_result_fragment
    }


    override fun initViews() {
        val keyword = arguments?.getString("keyword")
        netSwitchPagerData(keyword)
    }

    override fun lazyLoad() {

    }

    private fun netSwitchPagerData(keyword: String?) {

        val arrays = resources.getStringArray(R.array.emotion_search_array)


        val titleList = listOf(*arrays)

        initNavigator(titleList)

        val fragments = arrayListOf<Fragment>()
        repeat(titleList.size) {

            if (it == 0) {
                val emotionSearchContentFragment = EmotionSearchContentFragment()
                val bundle = Bundle()
                bundle.putString("keyword", keyword)

                emotionSearchContentFragment.arguments = bundle
                fragments.add(emotionSearchContentFragment)
            } else if (it == 1) {
                val emotionSearchTutorFragment = EmotionSearchTutorFragment()

                val bundle = Bundle()
                bundle.putString("keyword", keyword)
                emotionSearchTutorFragment.arguments = bundle

                fragments.add(emotionSearchTutorFragment)

            }
        }

        val efficientCourseMainAdapter = CommonMainPageAdapter(childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragments)
        viewpager_emotion_search.adapter = efficientCourseMainAdapter
//        mViewPager.setOffscreenPageLimit(2)
        viewpager_emotion_search.currentItem = 0
    }

    private fun initNavigator(titleList: List<String>) {
        val commonNavigator = CommonNavigator(activity)
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
                simplePagerTitleView.setOnClickListener { v -> viewpager_emotion_search.currentItem = index }
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
        magicIndicator_emotion_search.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator_emotion_search, viewpager_emotion_search)

        viewpager_emotion_search.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
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

}