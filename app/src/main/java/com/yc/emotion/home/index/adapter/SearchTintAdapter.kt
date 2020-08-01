package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.IndexHotInfo

/**
 * Created by wanglin  on 2019/7/10 15:06.
 */
class SearchTintAdapter(data: List<IndexHotInfo>?) : CommonMoreAdapter<IndexHotInfo, BaseViewHolder>(R.layout.search_tint_item, data) {

    override fun convert(helper: BaseViewHolder?, item: IndexHotInfo?) {
        helper?.let {
            item?.let {
                helper.setText(R.id.tv_title, item.search)
                val position = helper.adapterPosition
            }
        }


        //        if (position == mData.size() - 1) {
        //            helper.setGone(R.id.divider, false);
        //        }
    }
}
