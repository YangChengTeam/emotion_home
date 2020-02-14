package com.yc.emotion.home.index.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.alibaba.fastjson.JSON
import com.music.player.lib.util.ToastUtils
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.activity.BaseActivity
import com.yc.emotion.home.base.ui.fragment.BaseFragment
import com.yc.emotion.home.base.ui.fragment.BaseLazyFragment
import com.yc.emotion.home.index.ui.fragment.EmotionSearchHistoryFragment
import com.yc.emotion.home.index.ui.fragment.EmotionSearchResultFragment
import com.yc.emotion.home.model.bean.event.EventSearch
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import kotlinx.android.synthetic.main.activity_emotion_search.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * Created by suns  on 2019/10/15 08:50.
 */
class EmotionSearchActivity : BaseActivity() {


    private var keyword = ""

    private var historyListStr by Preference(ConstantKey.SEARCH_HISTORY_LIST, "")

    private var historyList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_emotion_search)

        initViews()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_emotion_search
    }

    override fun initViews() {
        invadeStatusBar() //侵入状态栏
        setAndroidNativeLightStatusBar() //状态栏字体颜色改变


        historyList = JSON.parseArray(historyListStr, String::class.java)
//        initSearchView()

        val bt = supportFragmentManager.beginTransaction()
        bt.add(R.id.fl_search_container, EmotionSearchHistoryFragment.newInstance(), EmotionSearchHistoryFragment.newInstance().fragmentTag)

        bt.commit()

        initListener()
    }


    private fun switchFragment(fragment: BaseFragment, tag: String) {
        val bt = supportFragmentManager.beginTransaction()

        bt.add(R.id.fl_search_container, fragment, fragment.fragmentTag)
        if (!fragment.isVisible) {
            bt.addToBackStack(null)
        }
        supportFragmentManager.findFragmentByTag(tag)?.let {
            bt.hide(it)
        }

        bt.commit()
    }

    private fun initListener() {
        activity_base_same_iv_back.setOnClickListener {
            popBackStack()
        }
        et_emotion_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyword = s.toString()

                Log.e("TAG", keyword)

                if (!TextUtils.isEmpty(keyword)) {
                    iv_delete.visibility = View.VISIBLE
                    iv_search.setImageResource(R.mipmap.icon_search_sel)

                } else {
                    iv_delete.visibility = View.GONE
                    iv_search.setImageResource(R.mipmap.index_search_icon)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        iv_delete.setOnClickListener {
            et_emotion_search.setText("")
            iv_delete.visibility = View.GONE
        }
        tv_emotion_search.setOnClickListener {
            if (TextUtils.isEmpty(keyword)) {
                ToastUtils.showCenterToast("请输入关键字搜索")
                return@setOnClickListener
            }


            searchKeyword(keyword)
            saveHistoryList()
        }


    }

    var searchResultFragment: EmotionSearchResultFragment? = null
    fun searchKeyword(keyword: String) {
//        if (searchResultFragment == null) {
//            searchResultFragment = EmotionSearchResultFragment.newInstance(keyword)
//            searchResultFragment?.let {
//
//            }
//        } else {
//            EventBus.getDefault().post(EventSearch(keyword))
//        }
        switchFragment(EmotionSearchResultFragment.newInstance(keyword), EmotionSearchHistoryFragment.newInstance().fragmentTag)

        hindKeyboard(et_emotion_search)

    }

    fun setKeyWord(keyword: String) {
        et_emotion_search.setText(keyword)
        et_emotion_search.setSelection(keyword.length)
    }


    private fun saveHistoryList() {
        if (historyList == null) {
            historyList = mutableListOf()
        }
        historyList?.let {

            if (!it.contains(keyword)) {
                historyList?.add(0, keyword)
                historyListStr = JSON.toJSONString(historyList)

            }

        }

    }


    private fun popBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        popBackStack()
    }


}