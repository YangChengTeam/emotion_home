package com.yc.emotion.home.index.adapter

import android.util.SparseArray
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.model.bean.EmotionTestTopicInfo
import com.yc.emotion.home.model.bean.QuestionInfo

/**
 *
 * Created by suns  on 2019/10/11 15:40.
 */
class EmotionTestDetailAnswerAdapter(mDatas: List<QuestionInfo>?) : BaseMultiItemQuickAdapter<QuestionInfo, BaseViewHolder>(mDatas) {

    private var viewArray: SparseArray<View>? = null

    init {
        addItemType(QuestionInfo.ITEM_TYPE_TOPIC, R.layout.layout_emotion_test_detail_topic_item)
        addItemType(QuestionInfo.ITEM_TYPE_ANSWER, R.layout.layout_emotion_test_detail_answer_item)
        viewArray = SparseArray()
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder, item: QuestionInfo?) {

            item?.let {
                when (item.itemType) {
                    QuestionInfo.ITEM_TYPE_TOPIC -> {
                        helper.setText(R.id.tv_emotion_test_topic, item.question)
                    }
                    QuestionInfo.ITEM_TYPE_ANSWER -> {
                        helper.setText(R.id.tv_emotion_test_answer_title, item.option_text)
                        viewArray?.put(helper.adapterPosition, helper.itemView)
                    }
                    else -> {
                    }
                }
            }

    }


    fun resetState() {
        viewArray?.let {
            for (i in 0..it.size()) {
                viewArray?.get(i)?.isSelected = false
            }
        }
    }

    fun getItemView(pos: Int): View? {
        resetState()
        return viewArray?.get(pos)
    }
}