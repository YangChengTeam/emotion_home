package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.ui.fragment.LoveArticleListFragment
import com.yc.emotion.home.model.bean.CategoryArticleChildrenBean
import kotlinx.android.synthetic.main.activity_love_by_stages.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import java.util.*

/**
 * 首页提升--》不同恋爱阶段
 */

class LoveByStagesActivity : BaseSameActivity() {



    private var mActivityTitle: String? = null
    private var mCategoryArticleChildrenBeans: List<CategoryArticleChildrenBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_love_by_stages
    }

    override fun initViews() {

        netSwitchPagerData()
    }

    private fun netSwitchPagerData() {
        val titleLists = ArrayList<String>()
        val idLists = ArrayList<Int>()

        val fragmentList = arrayListOf<Fragment>()



        mCategoryArticleChildrenBeans?.forEach {

            titleLists.add(it.name)
            idLists.add(it.id)

            val fragment= LoveArticleListFragment.newInstance(it.id)
            fragmentList.add(fragment)
            Log.d("mylog", "netSwitchPagerData: categoryArticleChildrenBean.name " + it.name
                    + " categoryArticleChildrenBean.id " + it.id)
        }


        initNavigator(titleLists)


        val commonMainPageAdapter = CommonMainPageAdapter(supportFragmentManager, titleLists, fragmentList)


        love_by_stages_view_pager.adapter = commonMainPageAdapter
        if (mCategoryArticleChildrenBeans != null && mCategoryArticleChildrenBeans!!.isNotEmpty())
            love_by_stages_view_pager.offscreenPageLimit = mCategoryArticleChildrenBeans!!.size - 1
    }

    private fun initNavigator(titleList: List<String>) {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.65f
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
                simplePagerTitleView.setOnClickListener { v -> love_by_stages_view_pager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.red_crimson))
                return indicator
            }
        }
        commonNavigator.adapter = navigatorAdapter
        love_by_stages_pager_tabs.navigator = commonNavigator
        ViewPagerHelper.bind(love_by_stages_pager_tabs, love_by_stages_view_pager)
    }

    override fun offerActivityTitle(): String? {
        return mActivityTitle
    }

    override fun initIntentData() {
        val intent = intent
        mActivityTitle = intent.getStringExtra("title")
        mCategoryArticleChildrenBeans = intent.getParcelableArrayListExtra("CategoryArticleChildrenBeans")
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    companion object {

        fun startLoveByStagesActivity(context: Context, title: String, children: ArrayList<CategoryArticleChildrenBean>?) {
            val intent = Intent(context, LoveByStagesActivity::class.java)
            intent.putExtra("title", title)
            intent.putParcelableArrayListExtra("CategoryArticleChildrenBeans", children)
            context.startActivity(intent)
        }
    }
}
