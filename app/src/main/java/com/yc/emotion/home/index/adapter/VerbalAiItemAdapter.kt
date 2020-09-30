package com.yc.emotion.home.index.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.index.domain.bean.AIItem

/**
 *
 * Created by suns  on 2020/9/1 11:47.
 */
class VerbalAiItemAdapter(datas: List<AIItem>?) : BaseQuickImproAdapter<AIItem, BaseViewHolder>(R.layout.verbal_ai_item_view, datas) {
    override fun convert(helper: BaseViewHolder, item: AIItem?) {
        item?.let {
            val position = helper.adapterPosition
            helper.setText(R.id.tv_ai_verbal, "${(position + 1)}、${it.content}")
                    .addOnClickListener(R.id.iv_praise)
                    .addOnClickListener(R.id.iv_collect)

            helper.getView<ImageView>(R.id.iv_praise).isSelected = it.is_favour == 1
            helper.getView<ImageView>(R.id.iv_collect).isSelected = it.is_collect == 1
        }
    }
}