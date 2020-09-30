package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.umeng.analytics.MobclickAgent
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.adapter.LoveHealDetailsAdapter
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.bean.LoveHealDetBean
import com.yc.emotion.home.model.bean.SearchDialogueBean
import com.yc.emotion.home.model.bean.event.EventPayVipSuccess
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivity
import com.yc.emotion.home.utils.UserInfoHelper
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.activity_love_heal_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoveHealDetailsActivity : BaseSameActivity(), IndexVerbalView {


    private var mTitle: String? = null
    private var mCategoryId: String? = null

    private var mAdapter: LoveHealDetailsAdapter? = null

    private val PAGE_SIZE = 8
    private var PAGE_NUM = 1
    private var mLoveHealDetBeans: List<LoveHealDetBean>? = null
    private var shareTextString: String? = null
    private var isSearch: Boolean = false

    override fun initIntentData() {
        mTitle = intent?.getStringExtra("title")
        mCategoryId = intent?.getStringExtra("category_id")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()

    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun initData() {
        netData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_love_heal_details
    }


    override fun initViews() {

        mPresenter = IndexVerbalPresenter(this, this)
        initData()

        initRecyclerView()
        initSearchView()
        initListener()
    }


    private fun initSearchView() {
        //修改键入的文字字体大小、颜色和hint的字体颜色
        val editText = share_searchView.findViewById<EditText>(R.id.search_src_text)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources
                .getDimension(R.dimen.size_14))
        //        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        val mCloseButton = share_searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val textView = share_searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        if (mCloseButton.isClickable) {
            mCloseButton.setOnClickListener { view ->
                //清除搜索框并加载默认数据
                textView.text = null
            }
        }
        share_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { //搜索按钮回调
                PAGE_NUM = 1

                searchKeyWord(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { //输入变化回调
                this@LoveHealDetailsActivity.shareTextString = newText
                return false
            }
        })
    }

    private fun searchKeyWord(keyword: String?) {
        (mPresenter as IndexVerbalPresenter).searchVerbalTalk(keyword, PAGE_NUM, PAGE_SIZE)
    }

    private fun initListener() {
        love_heal_details_iv_to_wx.clickWithTrigger {
            showToWxServiceDialog()
        }
        love_heal_details_swipe_refresh.setColorSchemeResources(R.color.red_crimson)
        love_heal_details_swipe_refresh.setOnRefreshListener {

            PAGE_NUM = 1
            if (isSearch) {
                searchKeyWord(this.shareTextString)
            } else {
                netData()
            }
        }
        mAdapter?.setOnLoadMoreListener({ this.netData() }, love_heal_details_rl)

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val loveHealDetBean = mAdapter?.getItem(position)
            if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                if (!UserInfoHelper.instance.goToLogin(this@LoveHealDetailsActivity))
                //TODO 购买VIP刷新数据
                    startActivity(Intent(this@LoveHealDetailsActivity, BecomeVipActivity::class.java))
        }
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val loveHealDetBean = mAdapter?.getItem(position)
            if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                if (!UserInfoHelper.instance.goToLogin(this@LoveHealDetailsActivity))
                //TODO 购买VIP刷新数据
                    startActivity(Intent(this@LoveHealDetailsActivity, BecomeVipActivity::class.java))
        }

        tv_search_btn.clickWithTrigger {
            MobclickAgent.onEvent(this, "verbal_detail_search", "话术详情搜索")
            if (!UserInfoHelper.instance.goToLogin(this)) {
                PAGE_NUM = 1
                searchKeyWord(shareTextString)
            }
        }

    }


    private fun initRecyclerView() {


        val layoutManager = LinearLayoutManager(this)
        love_heal_details_rl.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //设置增加或删除条目的动画
        love_heal_details_rl.itemAnimator = DefaultItemAnimator()
        mAdapter = LoveHealDetailsAdapter(mLoveHealDetBeans, mTitle)
        love_heal_details_rl.adapter = mAdapter


    }

    private fun netData() {

        (mPresenter as? IndexVerbalPresenter)?.loveListCategory(mCategoryId, page = PAGE_NUM, page_size = PAGE_SIZE)

    }

    private fun createNewData(loveHealDetBeans: List<LoveHealDetBean>?) {

        if (loveHealDetBeans != null && loveHealDetBeans.isNotEmpty()) {
            for (loveHealDetBean in loveHealDetBeans) {
                if (loveHealDetBean.is_vip == 0) {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_ITEM
                } else {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_VIP
                }

            }
        }
        mLoveHealDetBeans = loveHealDetBeans
        if (PAGE_NUM == 1) {
            mAdapter?.setNewData(mLoveHealDetBeans)
        } else {
            mLoveHealDetBeans?.let {

                mAdapter?.addData(it)
            }
        }
        if (loveHealDetBeans != null && loveHealDetBeans.size == PAGE_SIZE) {
            mAdapter?.loadMoreComplete()
            PAGE_NUM++
        } else {
            mAdapter?.loadMoreEnd()
        }


    }


    override fun onComplete() {
        if (love_heal_details_swipe_refresh.isRefreshing)
            love_heal_details_swipe_refresh.isRefreshing = false

    }

    override fun showVerbalDetailInfos(data: List<LoveHealDetBean>?) {
        createNewData(data)
    }

    override fun showSearchResult(searchDialogueBean: SearchDialogueBean?, keyword: String?) {
        super.showSearchResult(searchDialogueBean, keyword)

        val searchVip = searchDialogueBean?.search_buy_vip
        if (searchVip == 0) {//直接看
            isSearch = true
            val list = searchDialogueBean.list
            createNewData(list)
        } else {
            startActivity(Intent(this, BecomeVipActivity::class.java))
        }
    }


    override fun offerActivityTitle(): String? {
        return mTitle
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(eventPayVipSuccess: EventPayVipSuccess) {
        netData()
    }

    companion object {

        fun startLoveHealDetailsActivity(context: Context, title: String, categoryId: String) {
            val intent = Intent(context, LoveHealDetailsActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("category_id", categoryId)
            context.startActivity(intent)
        }
    }


}
