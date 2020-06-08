package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.model.bean.LoveHealingBean

/**
 * Created by Administrator on 2017/9/12.
 */
class LoveHealingAdapter(data: List<LoveHealingBean?>?) : BaseMultiItemQuickAdapter<LoveHealingBean?, BaseViewHolder?>(data) {
    override fun convert(helper: BaseViewHolder?, item: LoveHealingBean?) {

        helper?.let {
            item?.let {
                when (item.type) {
                    LoveHealingBean.VIEW_TITLE -> {
                    }
                    LoveHealingBean.VIEW_ITEM_ITEM -> helper.setText(R.id.item_love_healing_item_title_tv_name, item.chat_name)
                    LoveHealingBean.VIEW_ITEM -> helper.setText(R.id.item_love_healing_tv_name, item.chat_name)
                    else -> {
                    }
                }
            }

        }
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    init {
        addItemType(LoveHealingBean.VIEW_TITLE, R.layout.recycler_view_item_title_love_healing)
        addItemType(LoveHealingBean.VIEW_ITEM_ITEM, R.layout.recycler_view_item_love_healing_item_title)
        addItemType(LoveHealingBean.VIEW_ITEM, R.layout.recycler_view_item_love_healing)
        setLoadMoreView(CustomLoadMoreView())
    }
}