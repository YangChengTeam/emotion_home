package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter

/**
 *
 * Created by suns  on 2019/10/15 16:52.
 */
class EmotionSearchHistoryAdapter(mDatas: List<String>?) : CommonMoreAdapter<String, BaseViewHolder>(R.layout.search_history_item, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: String?) {

        helper?.let {
            item?.let {
                helper.setText(R.id.tv_history_title, item)
                        .addOnClickListener(R.id.iv_history_delete)
            }
        }
    }
}