package com.yc.emotion.home.community.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.community.presenter.CommunityPresenter
import com.yc.emotion.home.community.ui.activity.CommunityNoticeDetailActivity
import com.yc.emotion.home.community.ui.activity.CommunityPublishActivity
import com.yc.emotion.home.community.ui.widget.CommunityTagPopWindow
import com.yc.emotion.home.community.view.CommunityView
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.model.bean.CommunityTagInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.fragment_main_community.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import yc.com.rthttplibrary.util.ScreenUtil
import java.util.*

/**
 * Created by suns  on 2019/8/29 09:51.
 */
class CommunityMainFragment : BaseFragment<CommunityPresenter>(), View.OnClickListener, CommunityView {
    override fun shoCommunityNewestCacheInfos(datas: List<CommunityInfo>?) {

    }


    private var tagPos by Preference(ConstantKey.TAG_POSTION, 0)

    var mMainActivity: MainActivity? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_community
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }

    override fun initViews() {
        mPresenter = CommunityPresenter(activity, this)

        val layoutParams = main_community_tab.layoutParams as RelativeLayout.LayoutParams

        layoutParams.width = ScreenUtil.getWidth(mMainActivity) * 2 / 3
        main_community_tab.layoutParams = layoutParams

        ll_top_notice.visibility = View.VISIBLE
        iv_add_community.visibility = View.VISIBLE


        getData()
        initListener()

    }



    private fun initListener() {
        ll_top_notice.setOnClickListener(this)
        iv_add_community.setOnClickListener(this)
        tv_community_tag.setOnClickListener(this)
    }

    override fun lazyLoad() {
        netSwitchPagerData()
    }

    private fun netSwitchPagerData() {

        val arrays = resources.getStringArray(R.array.community_array)

        val titleList = listOf(*arrays)

        val fragmentList = arrayListOf<Fragment>()

        fragmentList.add(CommunityFragment())
        fragmentList.add(CommunityHotFragment())
        fragmentList.add(CommunityMyFragment())

        initNavigator(titleList)


        val communityMainAdapter = CommonMainPageAdapter(childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, titleList, fragmentList)
        main_community_view_pager.adapter = communityMainAdapter
        main_community_view_pager.offscreenPageLimit = 2
        //        mViewPager.setCurrentItem(1);
    }

    private fun initNavigator(titleList: List<String>?) {
        val commonNavigator = CommonNavigator(mMainActivity)
        //        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.isAdjustMode = true
        val navigatorAdapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return titleList?.size ?: 0
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //                Log.e(TAG, "getTitleView: " + index);

                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = titleList?.get(index)
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = resources.getColor(R.color.black)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.app_color)
                simplePagerTitleView.setOnClickListener { v -> main_community_view_pager.currentItem = index }
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
        main_community_tab.navigator = commonNavigator
        ViewPagerHelper.bind(main_community_tab, main_community_view_pager)

        main_community_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
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
            R.id.ll_top_notice -> startActivity(Intent(mMainActivity, CommunityNoticeDetailActivity::class.java))
            R.id.iv_add_community -> {
                mMainActivity?.let {
                    if (!UserInfoHelper.instance.goToLogin(mMainActivity))
                        startActivity(Intent(mMainActivity, CommunityPublishActivity::class.java))
                }

            }
            R.id.tv_community_tag -> {
                setTagArrow(true)
                val communityTagPopwindow = CommunityTagPopWindow(mMainActivity)
                communityTagPopwindow.createNewData(tagList)//
                communityTagPopwindow.showAsDropDown(view_divider)
                communityTagPopwindow.setOnDismissListener { setTagArrow(false) }
                communityTagPopwindow.setOnTagSelectListener(object : CommunityTagPopWindow.OnTagSelectListener {
                    override fun onTagSelect(communityTagInfo: CommunityTagInfo) {
                        tv_community_tag.text = communityTagInfo.title
                    }
                })
            }
        }
    }

    private fun setTagArrow(isExtend: Boolean) {
        val drawable = if (isExtend) {
            resources.getDrawable(R.mipmap.community_icon_arrow_up)
        } else {
            resources.getDrawable(R.mipmap.community_icon_arrow_down)
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)

        tv_community_tag.setCompoundDrawables(null, null, drawable, null)
    }


    private var tagList: List<CommunityTagInfo>? = null

    private fun getData() {

        mPresenter?.getCommunityTagInfos()

    }

    override fun showCommunityTagInfos(list: List<CommunityTagInfo>) {
        tagList = list
        tagList?.let {
            tv_community_tag.text = tagList?.get(tagPos)?.title
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

}
