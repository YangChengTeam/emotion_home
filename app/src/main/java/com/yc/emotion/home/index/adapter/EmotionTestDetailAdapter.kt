package com.yc.emotion.home.index.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.QuestionInfo
import com.yc.emotion.home.model.bean.event.EventBusEmotionTest
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by suns  on 2019/10/11 15:11.
 */
class EmotionTestDetailAdapter(private val mContext: Context, private val mEmotionTestTopicInfos: List<QuestionInfo>) : PagerAdapter() {
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val questionInfo = mEmotionTestTopicInfos[position]
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_emotion_test_detail_item, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView_emotion_test_detail)
        val newTestTopicInfos: MutableList<QuestionInfo> = ArrayList()
        questionInfo.type = QuestionInfo.ITEM_TYPE_TOPIC
        newTestTopicInfos.add(questionInfo)
        val options = questionInfo.options
        if (options != null && options.size > 0) {
            for (option in options) {
                option.question_id = questionInfo.question_id
                option.type = QuestionInfo.ITEM_TYPE_ANSWER
                newTestTopicInfos.add(option)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val emotionTestDetailAdapter = EmotionTestDetailAnswerAdapter(newTestTopicInfos)
        recyclerView.adapter = emotionTestDetailAdapter
        emotionTestDetailAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view1: View?, position1: Int ->
            val item = emotionTestDetailAdapter.getItem(position1)
            if (item != null) {
                if (item.type == QuestionInfo.ITEM_TYPE_ANSWER) {
                    val itemView = emotionTestDetailAdapter.getItemView(position1)
                    if (itemView != null) itemView.isSelected = true
                    EventBus.getDefault().post(EventBusEmotionTest(item))
                }
            }
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return mEmotionTestTopicInfos.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

}