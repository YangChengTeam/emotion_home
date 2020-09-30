package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.model.bean.LoveHealingDetailBean


class LoveUpDownPhotoAdapter(data: List<LoveHealingDetailBean>?) : BaseMultiItemQuickAdapter<LoveHealingDetailBean, BaseViewHolder?>(data) {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    init {
        addItemType(LoveHealingDetailBean.VIEW_ITEM_MEN, R.layout.recycler_view_item_up_down_men)
        addItemType(LoveHealingDetailBean.VIEW_ITEM_WOMEN, R.layout.recycler_view_item_up_down_women)
        addItemType(LoveHealingDetailBean.VIEW_TITLE, R.layout.recycler_view_item_up_down_title)
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder, item: LoveHealingDetailBean) {
        when (item.type) {
            LoveHealingDetailBean.VIEW_ITEM_MEN, LoveHealingDetailBean.VIEW_ITEM_WOMEN -> helper.setText(R.id.item_up_down_women_tv_name, item.content)
            LoveHealingDetailBean.VIEW_TITLE -> {
            }
        }
    }
}