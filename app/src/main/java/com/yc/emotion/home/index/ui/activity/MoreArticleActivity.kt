package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
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
import com.yc.emotion.home.index.presenter.ArticlePresenter
import com.yc.emotion.home.index.ui.fragment.ArticleFragment
import com.yc.emotion.home.index.ui.widget.EfficientCoursePopWindow
import com.yc.emotion.home.index.view.ArticleView
import com.yc.emotion.home.model.bean.AticleTagInfo
import com.yc.emotion.home.utils.clickWithTrigger
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
 * Created by suns  on 2019/10/23 16:53.
 */
class MoreArticleActivity : BaseSameActivity(), ArticleView {


    override fun getLayoutId(): Int {
        return R.layout.activity_course_efficient
    }

    override fun initViews() {
        initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()

    }

    fun initView() {
        mPresenter = ArticlePresenter(this, this)
        (mPresenter as ArticlePresenter).getArticleTagInfos()
        initListener()

    }

    private fun initListener() {
        tv_efficient.clickWithTrigger {
            setTagArrow(true)
            titleList?.let {
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


    private fun netSwitchPagerData(catIds: ArrayList<Int>, titleList: ArrayList<String>) {


        initNavigator(titleList)

        val fragments = arrayListOf<Fragment>()


        catIds.forEach {
            val articleFragment = ArticleFragment()

            val bundle = Bundle()
            bundle.putInt("cat_id", it)


            articleFragment.arguments = bundle

            fragments.add(articleFragment)
        }


        val efficientCourseMainAdapter = CommonMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragments)
        viewPager_efficient.adapter = efficientCourseMainAdapter
//        viewPager_efficient.offscreenPageLimit = catIds.size - 1
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
                indicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
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

        }

    }


    private var titleList: ArrayList<String>? = null
    private fun createNewData(list: List<AticleTagInfo>?) {
        list?.let {

            val catIds = arrayListOf<Int>()
            titleList = arrayListOf()

            list.forEach {
                val children = it.children
                children?.let {
                    children.forEach { item ->
                        catIds.add(item.cat_id)
                        titleList?.add(item.cat_name)

                    }
                }
            }
            titleList?.let {

                netSwitchPagerData(catIds, it)
            }

        }
    }


    override fun showArticleCategory(data: List<AticleTagInfo>?) {
        createNewData(data)
    }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun offerActivityTitle(): String {
        return "更多文章"
    }
}