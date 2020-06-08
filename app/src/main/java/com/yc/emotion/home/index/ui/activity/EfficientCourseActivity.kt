package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.index.ui.fragment.EfficientCourseFragment
import com.yc.emotion.home.index.ui.widget.EfficientCoursePopWindow
import com.yc.emotion.home.model.bean.CourseInfo
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.presenter.TutorCoursePresenter
import com.yc.emotion.home.index.view.TutorCourseView
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
 * Created by suns  on 2019/10/9 11:12.
 * 高效课程
 */
class EfficientCourseActivity : BaseSameActivity(), TutorCourseView {


    var categoryList: List<CourseInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_course_efficient
    }

    override fun initViews() {

        mPresenter = TutorCoursePresenter(this, this)

        intent?.let {
            categoryList = intent.getParcelableArrayListExtra("category_list")

        }


        if (categoryList != null && categoryList!!.isNotEmpty()) {
            netSwitchPagerData(categoryList)
        } else {
            getCourseCategory()
        }



        tv_efficient.setOnClickListener(this)
    }

    override fun offerActivityTitle(): String {
        return "高效课程"
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    private var titleList = arrayListOf<String>()
    private fun netSwitchPagerData(list: List<CourseInfo>?) {

        titleList.clear()

        val fragments = arrayListOf<Fragment>()
        list?.let {
            list.forEach {
                titleList.add(it.cat_name)
                val efficientCourseFragment = EfficientCourseFragment()

                val bundle = Bundle()

                bundle.putString("cat_id", it.cat_id)

                efficientCourseFragment.arguments = bundle

                fragments.add(efficientCourseFragment)
            }
        }



        initNavigator(titleList)


        val efficientCourseMainAdapter = CommonMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragments)
        viewPager_efficient.adapter = efficientCourseMainAdapter
//        mViewPager.setOffscreenPageLimit(2)
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

    override fun onClick(v: View) {
        when (v.id) {

            R.id.tv_efficient -> {
                setTagArrow(true)
                categoryList?.let {
                    val efficientCoursePopwindow = EfficientCoursePopWindow(this)
                    efficientCoursePopwindow.setData(titleList)
                    efficientCoursePopwindow.showAsDropDown(view_divider)
//                efficientCoursePopwindow.show(window.decorView.rootView,Gravity.TOP)
                    efficientCoursePopwindow.setOnDismissListener { setTagArrow(false) }
                    efficientCoursePopwindow.setOnTagSelectListener(object : EfficientCoursePopWindow.OnTagSelectListener {
                        override fun onTagSelect(position: Int) {
                            viewPager_efficient.currentItem = position
                        }

                    })
                }

            }
        }
    }

    private fun setTagArrow(isExtend: Boolean) {
        val drawable: Drawable = if (isExtend) {
            resources.getDrawable(R.mipmap.community_icon_arrow_up)
        } else {
            resources.getDrawable(R.mipmap.community_icon_arrow_down)
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)

        tv_efficient.setCompoundDrawables(null, null, drawable, null)
    }


    private fun getCourseCategory() {

        (mPresenter as? TutorCoursePresenter)?.getCourseCategory()

    }

    override fun showCourseCategory(data: ArrayList<CourseInfo>?) {
        categoryList = data
        netSwitchPagerData(data)
    }
}