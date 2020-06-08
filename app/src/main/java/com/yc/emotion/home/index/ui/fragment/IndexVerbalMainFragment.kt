package com.yc.emotion.home.index.ui.fragment


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.music.player.lib.util.ToastUtils
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.adapter.SearchTintAdapter
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.ui.activity.SearchActivity
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.IndexHotInfo
import com.yc.emotion.home.model.bean.SearchDialogueBean
import com.yc.emotion.home.pay.ui.activity.VipActivity
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_index_verbal.*
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
 * Created by suns  on 2019/9/19 14:44.
 */
class IndexVerbalMainFragment : BaseFragment<IndexVerbalPresenter>(), IndexVerbalView {


    private var keyword = ""
    private var isVisable = false

    private val page = 1
    private val PAGE_SIZE = 1

    private var tintAdapter: SearchTintAdapter? = null

    private var mHandler: Handler? = null

    private var mMainActivity: MainActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mMainActivity = context
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_index_verbal
    }


    override fun lazyLoad() {
        getData()
    }

    override fun initViews() {

        mPresenter = IndexVerbalPresenter(mMainActivity, this)

        if (mHandler == null) mHandler = Handler()

        val mTitles = resources.getStringArray(R.array.index_verbal).toList()
        initNavigator(mTitles)

        val fragmentList = arrayListOf<Fragment>()

        fragmentList.add(IndexVerbalFragment.newInstance(""))
        fragmentList.add(IndexVerbalFragment.newInstance("2"))


        val commonMainPageAdapter = CommonMainPageAdapter(childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTitles, fragmentList)

        collect_view_pager.adapter = commonMainPageAdapter


        search_down_rv.layoutManager = LinearLayoutManager(mMainActivity)

        search_down_rv.itemAnimator = DefaultItemAnimator()
        tintAdapter = SearchTintAdapter(null)
        search_down_rv.adapter = tintAdapter



        initSearchView()
        initListener()
    }


    private fun initNavigator(titleList: List<String>) {
        val commonNavigator = CommonNavigator(mMainActivity)
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
                simplePagerTitleView.selectedColor = resources.getColor(R.color.app_color)
                simplePagerTitleView.setOnClickListener { v -> collect_view_pager.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 50.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(resources.getColor(R.color.app_color))
                return indicator
            }


        }
        commonNavigator.adapter = navigatorAdapter
        collect_pager_tabs.navigator = commonNavigator
        ViewPagerHelper.bind(collect_pager_tabs, collect_view_pager)

        collect_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
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


    private fun initSearchView() {
        et_verbal_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyword = s.toString()
                if (!TextUtils.isEmpty(keyword)) {
                    iv_delete.visibility = View.VISIBLE
                    iv_search.setImageResource(R.mipmap.icon_search_sel)
                    getRandomWords(keyword)
                    search_down_rv.visibility = View.VISIBLE
                    tv_verbal_search.visibility = View.VISIBLE
                    mHandler?.postDelayed(myRunnable, 2000)
                } else {
                    iv_delete.visibility = View.GONE
                    iv_search.setImageResource(R.mipmap.index_search_icon)
                    search_down_rv.visibility = View.GONE
                    tv_verbal_search.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        iv_delete.setOnClickListener {
            et_verbal_search.setText("")
            iv_delete.visibility = View.GONE
        }


        et_verbal_search.setOnClickListener {
            isVisable = !isVisable
            search_down_rv.visibility = if (isVisable) View.VISIBLE else View.GONE
            tv_verbal_search.visibility = if (isVisable) View.VISIBLE else View.GONE
        }
        et_verbal_search.setOnFocusChangeListener { v, hasFocus ->
            isVisable = !isVisable
            search_down_rv.visibility = if (isVisable) View.VISIBLE else View.GONE
            tv_verbal_search.visibility = if (isVisable) View.VISIBLE else View.GONE

        }

    }

    fun initListener() {
//        collect_pager_iv_back.setOnClickListener { finish() }

        tintAdapter?.setOnItemClickListener { adapter, view, position ->
            val tint = tintAdapter?.getItem(position)
            tint?.let {
                searchWord(tint.search)
            }
            isVisable = false
            search_down_rv.visibility = if (isVisable) View.VISIBLE else View.GONE

        }

        tv_verbal_search.setOnClickListener {
            if (TextUtils.isEmpty(keyword)) {
                ToastUtils.showCenterToast("请输入关键字搜索")
                return@setOnClickListener
            }
            searchWord(keyword)
        }


    }

    /**
     * 模拟服务器获取随机顺序的热词
     *
     * @return
     */
    private fun getRandomWords(keyWord: String) {

        mPresenter?.getIndexDropInfos(keyWord)

    }

    private fun searchWord(keyword: String) {

        MobclickAgent.onEvent(activity, "search_dialogue_id", "搜索话术框")

//        mPresenter?.searchVerbalTalk(keyword, page, PAGE_SIZE)
//        searchCount(keyword)
        SearchActivity.startSearchActivity(activity, keyword)
//        if (!UserInfoHelper.instance.goToLogin(activity)) {
//
//
//        }
    }

    private fun searchCount(keyword: String) {

        mPresenter?.searchCount(keyword)

    }

    private fun getData() {
        getRandomWords("")
    }


    private val myRunnable = object : Runnable {
        override fun run() {
            search_down_rv.visibility = View.GONE
            mHandler?.postDelayed(this, 10000)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != mHandler)
            mHandler?.removeCallbacks(myRunnable)
    }


    override fun showDropKeyWords(list: List<IndexHotInfo>) {
        tintAdapter?.setNewData(list)
    }

    override fun showLoadingDialog() {
        mMainActivity?.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mMainActivity?.hideLoadingDialog()
    }


    override fun showSearchResult(searchDialogueBean: SearchDialogueBean?, keyword: String?) {
        searchDialogueBean?.let {
            val searchBuyVip = searchDialogueBean.search_buy_vip
            if (1 == searchBuyVip) { //1 弹窗付费 0不弹
                startActivity(Intent(activity, VipActivity::class.java))
            } else {
                SearchActivity.startSearchActivity(activity, keyword)
            }
        }

    }

}