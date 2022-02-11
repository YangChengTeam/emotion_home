package com.yc.emotion.home.index.ui.fragment


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.MainActivity
import com.yc.emotion.home.base.ui.adapter.CommonMainPageAdapter
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.widget.ColorFlipPagerTitleView
import com.yc.emotion.home.index.adapter.SearchTintAdapter
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.ui.activity.SearchActivity
import com.yc.emotion.home.index.ui.activity.SmartChatVerbalActivity
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.IndexHotInfo
import com.yc.emotion.home.model.bean.SearchDialogueBean
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivityNew
import com.yc.emotion.home.utils.ToastUtils
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_index_verbal.*
import kotlinx.android.synthetic.main.verbal_search_top.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import kotlin.math.abs

/**
 *
 * Created by suns  on 2019/9/19 14:44.
 */
class IndexVerbalMainFragment : BaseFragment<IndexVerbalPresenter>(), IndexVerbalView {


    private var keyword = ""
    private var isVisable = false


    private var tintAdapter: SearchTintAdapter? = null

    private var mHandler: Handler? = null

    private lateinit var mMainActivity: MainActivity


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
//        mMainActivity.showToWxServiceDialog(position = "homewx", listener = object : BaseActivity.OnWxListener {
//            override fun onWx(wx: String) {
//                showService(wx)
//            }
//        })
    }


    private fun showService(wx: String) {
        val indexActivityFragment = IndexActivityFragment()
        val bundle = Bundle()
        bundle.putString("wx", wx)
        indexActivityFragment.arguments = bundle

        indexActivityFragment.show(childFragmentManager, "")
        indexActivityFragment.setListener(object : IndexActivityFragment.onToWxListener {
            override fun onToWx() {
                MobclickAgent.onEvent(mMainActivity, "index_dialog_add_wx", "首页弹窗添加微信")
                mMainActivity.openWeiXin()
                Toast.makeText(mMainActivity, "导师微信复制成功，请在微信中添加", Toast.LENGTH_SHORT).show()
            }


        })
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


    private fun initSearchView() {
        val searchView: SearchView = rootView.findViewById(R.id.verbal_search_view)
        val ivIconShare: ImageView = rootView.findViewById(R.id.search_iv_icon_share)
        try { //--拿到字节码
            val argClass: Class<*> = searchView.javaClass
            //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
            val ownField = argClass.getDeclaredField("mSearchPlate")
            //--暴力反射,只有暴力反射才能拿到私有属性
            ownField.isAccessible = true
            val mView = ownField[searchView] as View
            //--设置背景
            mView.setBackgroundResource(R.drawable.search_view_bg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchView.post {
            searchIcon.setImageDrawable(null)
            searchIcon.visibility = View.GONE
        }
        //修改键入的文字字体大小、颜色和hint的字体颜色
        val editText = searchView.findViewById<EditText>(R.id.search_src_text)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources
                .getDimension(R.dimen.sp_14))
        //        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        val mCloseButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val textView = searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        if (mCloseButton.isClickable) {
            mCloseButton.setOnClickListener { view: View? ->
                //清除搜索框并加载默认数据
                //                    hindShareItemShowInfo();
                textView.text = null
            }
        }
        editText.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            isVisable = !isVisable
            search_down_rv.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }
        editText.clickWithTrigger { v: View? ->
            isVisable = !isVisable
            search_down_rv.visibility = if (isVisable) View.VISIBLE else View.GONE
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { //搜索按钮回调
                if (TextUtils.isEmpty(query)) {
                    ToastUtils.showCenterToast("请输入搜索关键字")
                    return false
                }
                //                netIsVipData(query);
                searchWord(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { //输入变化回调
//                SearchActivity.this.shareTextString = newText;
                if (!TextUtils.isEmpty(newText) && !TextUtils.isEmpty(newText.trim { it <= ' ' })) {
                    getRandomWords(newText.trim { it <= ' ' })
                    search_down_rv.visibility = View.VISIBLE
                } else {
                    search_down_rv.visibility = View.GONE
                }
                return false
            }
        })
        ivIconShare.clickWithTrigger { v: View? ->
            val keyWord = searchView.query.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(keyWord)) {
                ToastUtils.showCenterToast("请输入搜索关键字")
                return@clickWithTrigger
            }
            //                netIsVipData(keyWord);
            searchWord(keyWord)
        }
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
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
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


    fun initListener() {


        tintAdapter?.setOnItemClickListener { adapter, view, position ->
            val tint = tintAdapter?.getItem(position)
            tint?.let {
                searchWord(tint.search)
            }
            isVisable = false
            search_down_rv.visibility = if (isVisable) View.VISIBLE else View.GONE

        }

        tv_verbal_search.clickWithTrigger {
            if (TextUtils.isEmpty(keyword)) {
                ToastUtils.showCenterToast("请输入关键字搜索")
                return@clickWithTrigger
            }
            searchWord(keyword)
        }

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, verticalOffset ->
            //verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
            val offset = abs(verticalOffset) //目的是将负数转换为绝对正数；
            //标题栏的渐变
            mMainActivity.changeAlpha(ContextCompat.getColor(mMainActivity, R.color.white), abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange).let { ll_top_container.setBackgroundColor(it) }

            /**
             * 当前最大高度偏移值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */
            if (offset <= appBarLayout.totalScrollRange / 2) {
                toolbar.title = ""
                ll_top_container.alpha = (appBarLayout.totalScrollRange / 2 - offset * 1.0f) / (appBarLayout.totalScrollRange / 2)
//                toolbar.visibility = View.GONE
                rl_search_container.visibility = View.GONE
                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (offset > appBarLayout.totalScrollRange / 2) {
                val floate = (offset - appBarLayout.totalScrollRange / 2) * 1.0f / (appBarLayout.totalScrollRange / 2)
                ll_top_container.alpha = floate

                rl_search_container.visibility = View.VISIBLE
            }
        })
        et_verbal_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = s.toString().trim()
                if (!TextUtils.isEmpty(content) && content.isNotEmpty()) {
                    tv_verbal_search.visibility = View.VISIBLE
                    iv_delete.visibility = View.VISIBLE
                } else {
                    tv_verbal_search.visibility = View.GONE
                    iv_delete.visibility = View.GONE
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        tv_verbal_search.clickWithTrigger {
            val content = et_verbal_search.text.toString().trim()
            searchWord(content)
        }
        iv_delete.clickWithTrigger {
            et_verbal_search.setText("")
        }

        iv_top_bg.clickWithTrigger {
            SmartChatVerbalActivity.startActivity(mMainActivity, "")
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


//        SearchActivity.startSearchActivity(activity, keyword)
//        if (!UserInfoHelper.instance.goToLogin(activity)) {
//            mPresenter?.searchVerbalTalk(keyword, page, PAGE_SIZE)
        activity?.let { SmartChatVerbalActivity.startActivity(it, keyword) }
        searchCount(keyword)
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


    override fun showLoading() {
        mMainActivity.showLoading()
    }

    override fun hideLoading() {
        mMainActivity.hideLoading()
    }

    override fun showSearchResult(searchDialogueBean: SearchDialogueBean?, keyword: String?) {
        searchDialogueBean?.let {
            val searchBuyVip = searchDialogueBean.search_buy_vip
            if (1 == searchBuyVip) { //1 弹窗付费 0不弹
                startActivity(Intent(activity, BecomeVipActivityNew::class.java))
            } else {
                SearchActivity.startSearchActivity(activity, keyword)
            }

        }

    }

}