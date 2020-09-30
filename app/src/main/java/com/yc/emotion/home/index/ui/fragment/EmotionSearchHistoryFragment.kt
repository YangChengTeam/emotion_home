package com.yc.emotion.home.index.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.yc.emotion.home.R
import com.yc.emotion.home.base.constant.Constant
import com.yc.emotion.home.index.adapter.EmotionSearchHistoryAdapter
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference
import com.yc.emotion.home.utils.clickWithTrigger
import kotlinx.android.synthetic.main.fragment_emotion_search_history.*

/**
 *
 * Created by suns  on 2019/10/28 11:17.
 */
class EmotionSearchHistoryFragment : BaseEmotionSearchFragment() {


    private var emotionSearchHistoryAdapter: EmotionSearchHistoryAdapter? = null

    private var historyListStr by Preference(ConstantKey.SEARCH_HISTORY_LIST, "")


    private var historyList: MutableList<String>? = null


    companion object {
        fun newInstance(): EmotionSearchHistoryFragment {
            val fragment = EmotionSearchHistoryFragment()
            fragment.fragmentTag = Constant.SEARCH_HISTORY


            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_emotion_search_history
    }



    override fun initViews() {
        historyList = JSON.parseArray(historyListStr, String::class.java)


        rcv_search_history.layoutManager = LinearLayoutManager(activity)
        emotionSearchHistoryAdapter = EmotionSearchHistoryAdapter(historyList)
        rcv_search_history.adapter = emotionSearchHistoryAdapter
        initListener()
    }

    private fun initListener() {
        tv_clear_history.clickWithTrigger {
            historyList?.clear()
            historyListStr = ""
            emotionSearchHistoryAdapter?.notifyDataSetChanged()
        }



        emotionSearchHistoryAdapter?.setOnItemChildClickListener { adapter, view, position ->


            if (view.id == R.id.iv_history_delete) {
                historyList?.removeAt(position)
                historyListStr = JSON.toJSONString(historyList)
                emotionSearchHistoryAdapter?.notifyItemRemoved(position)
            }

        }
        emotionSearchHistoryAdapter?.setOnItemClickListener { adapter, view, position ->
            val item = emotionSearchHistoryAdapter?.getItem(position)
            item?.let {
                mMainActivity?.searchKeyword(item)
                mMainActivity?.setKeyWord(it)

            }

        }
    }

    override fun lazyLoad() {

    }
}