package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter

class TutorTagAdapter(mDatas: List<String>?) : CommonMoreAdapter<String, BaseViewHolder>(R.layout.layout_tutor_item_tag, mDatas) {
    override fun convert(helper: BaseViewHolder?, item: String?) {

        helper?.let {
            item?.let {
                helper.setText(R.id.tv_tutor_tag, item)
            }
        }
    }

}
