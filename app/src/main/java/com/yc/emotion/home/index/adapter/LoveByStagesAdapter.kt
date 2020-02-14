package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.LoveByStagesBean

/**
 * Created by wanglin  on 2019/6/29 11:32.
 */
class LoveByStagesAdapter(data: List<LoveByStagesBean>?) : BaseQuickAdapter<LoveByStagesBean, BaseViewHolder>(R.layout.recycler_view_item_love_by_stages, data) {

    override fun convert(helper: BaseViewHolder?, item: LoveByStagesBean?) {

        helper?.let {
            item?.let {
                helper.setText(R.id.item_love_by_stages_tv_name, item.post_title)
                        .setText(R.id.item_love_by_stages_tv_des, item.feeluseful.toString() + "人觉得有用")
            }
        }

    }
}
