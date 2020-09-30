package com.yc.emotion.home.index.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseSameActivity
import com.yc.emotion.home.index.presenter.IndexVerbalPresenter
import com.yc.emotion.home.index.ui.fragment.SearchFragment
import com.yc.emotion.home.index.view.IndexVerbalView
import com.yc.emotion.home.model.util.SPUtils
import com.yc.emotion.home.model.util.TimeUtils
import com.yc.emotion.home.pay.ui.activity.BecomeVipActivity
import com.yc.emotion.home.utils.UserInfoHelper
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : BaseSameActivity(), IndexVerbalView {


    var shareTextString: String? = null


    private var mKeyword: String? = null

    override fun initIntentData() {
        mKeyword = intent?.getStringExtra("keyword")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initViews()

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }


    override fun initViews() {

        mPresenter = IndexVerbalPresenter(this, this)


        val sDay = TimeUtils.dateToStamp(Date(System.currentTimeMillis())).toString()
        share_tv_today_add.text = "今日新增${sDay.substring(5, 8).replace("0", "1")}条话术" //.contains("条话术")


        changHistoryFluidLayout("")  //初始化搜索记录


        share_tv_next.setOnClickListener(this)
        share_tv_delete.setOnClickListener(this)
        share_iv_to_vip.setOnClickListener(this)
        share_tv_to_help.setOnClickListener(this)

        //        initSwitchPagerData();

        //修改键入的文字字体大小、颜色和hint的字体颜色
        val editText = share_searview.findViewById<EditText>(R.id.search_src_text)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources
                .getDimension(R.dimen.size_16))
        //        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        val mCloseButton = share_searview.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val textView = share_searview.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        if (mCloseButton.isClickable) {
            mCloseButton.setOnClickListener { view ->
                //清除搜索框并加载默认数据
                //                    hindShareItemShowInfo();
                textView.text = null
            }
        }
        share_searview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { //搜索按钮回调
                startSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean { //输入变化回调
                this@SearchActivity.shareTextString = newText
                return false
            }
        })
        replaceFragment(mKeyword)


        val userInfo = UserInfoHelper.instance.getUserInfo()
        userInfo?.let {
            val vip = it.is_vip
            if (vip > 0) {
                Log.d("mylog", "initViews:  已经购买了vip")
                share_iv_to_vip.visibility = View.GONE
            } else {
                Log.d("mylog", "initViews:  未购买了vip")
            }
        }


        hindKeyboard(share_searview)

        textView.text = mKeyword
        mKeyword?.let { editText.setSelection(it.length) }


    }


    private fun replaceFragment(keyword: String?) {

        //获取管理者
        val supportFragmentManager = supportFragmentManager//开启事务
        val fragmentTransaction = supportFragmentManager.beginTransaction()//碎片
        //提交事务
        val mSearchFragment = SearchFragment()
        val bundle = Bundle()
        bundle.putString("keyword", keyword)
        mSearchFragment.arguments = bundle
        fragmentTransaction.replace(R.id.share_frame_layout, mSearchFragment).commit()


        searchCount(keyword)
    }


    private fun startSearch(query: String?) {
        var query = query
        if (TextUtils.isEmpty(query)) {
            return
        }

//        if (!UserInfoHelper.instance.goToLogin(this)) {
            query = query?.trim { it <= ' ' }
            if (TextUtils.isEmpty(query) || share_view_pager == null) {
                return
            }

            changHistoryFluidLayout(query)

            hindKeyboard(share_view_pager)

            //        netPagerOneData(query); //为了解决Fragment切换白屏的问题，第一页数据在Activity中请求

            replaceFragment(query)
//        }


    }


    /**
     * 增加搜索历史
     *
     * @param query 最新的一条历史
     */
    private fun changHistoryFluidLayout(query: String?) {
        val shareHistory = SPUtils.get(this, SPUtils.SHARE_HISTORY, "") as String
        var historyList: MutableList<String> = ArrayList()
        val stringBuffer = StringBuffer()
        if (!TextUtils.isEmpty(shareHistory)) {
            val split = shareHistory.split("__".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (s in split) {
                historyList.add(s)
            }
            if (historyList.contains(query)) {
                historyList.remove(query)
            }
        }
        query?.let {
            if (!TextUtils.isEmpty(query)) {
                historyList.add(query)
                if (historyList.size >= 20) {
                    historyList = historyList.subList(1, historyList.size)
                }
                for (i in historyList.indices) {
                    stringBuffer.append(historyList[i]).append("__")
                }
                SPUtils.put(this, SPUtils.SHARE_HISTORY, stringBuffer.toString())
            }
        }

        share_fluid_layout.removeAllViews()
        for (i in historyList.indices) {
            val textView = TextView(this)
            textView.text = historyList[i]
            textView.setPadding(0, 12, 38, 12)
            textView.isClickable = true
            textView.setTextColor(resources.getColor(R.color.select_color_text_gray_dark))
            textView.setOnClickListener { v -> share_searview.setQuery(textView.text, true) }
            share_fluid_layout.addView(textView)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.share_tv_next -> startSearch(shareTextString)
            R.id.share_tv_delete -> {
                SPUtils.put(this, SPUtils.SHARE_HISTORY, "")
                changHistoryFluidLayout("")
            }
            R.id.share_iv_to_vip ->
                //TODO 购买VIP刷新数据
                startActivity(Intent(this@SearchActivity, BecomeVipActivity::class.java))
            R.id.share_tv_to_help -> {
            }
        }
    }


    private fun searchCount(keyword: String?) {

        (mPresenter as? IndexVerbalPresenter)?.searchCount(keyword)

    }

    override fun offerActivityTitle(): String {
        return "搜索"
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }


    public override fun childDisposeOnBack(): Boolean {
        finish()
        return true
    }

    companion object {

        fun startSearchActivity(context: Context?, keyword: String?) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("keyword", keyword)
            context?.startActivity(intent)
        }
    }
}
