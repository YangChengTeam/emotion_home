package com.yc.emotion.home.index.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean

/**
 * Created by wanglin  on 2019/7/8 14:43.
 */
class CollectLoveHealDetailAdapter(data: List<LoveHealDetDetailsBean?>?) : CommonMoreAdapter<LoveHealDetDetailsBean?, BaseViewHolder?>(R.layout.recycler_view_item_love_healing, data) {
    override fun convert(helper: BaseViewHolder?, item: LoveHealDetDetailsBean?) {
        helper?.let {
            item?.let {
                helper.setText(R.id.item_love_healing_tv_name, item.content)
                        .setText(R.id.tv_tag, item.title).setVisible(R.id.tv_tag, true)
                val tv = helper.getView<TextView>(R.id.item_love_healing_tv_name)
                tv.setCompoundDrawables(null, null, null, null)
            }
        }

    }
}