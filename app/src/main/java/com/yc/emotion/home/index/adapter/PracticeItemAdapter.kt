package com.yc.emotion.home.index.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.model.bean.MainT2Bean

/**
 * Created by Administrator on 2017/9/12.
 * 实战教学
 */
class PracticeItemAdapter(data: List<MainT2Bean?>?) : BaseMultiItemQuickAdapter<MainT2Bean?, BaseViewHolder?>(data) {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    init {
        addItemType(MainT2Bean.VIEW_ITEM, R.layout.recycler_view_item_main_t2)
        addItemType(MainT2Bean.VIEW_TITLE, R.layout.recycler_view_item_title_t2_view)
        addItemType(MainT2Bean.VIEW_TO_PAY_VIP, R.layout.item_title_view_main_to_pay_vip)
        addItemType(MainT2Bean.VIEW_VIP, R.layout.item_title_view_vip)
        setLoadMoreView(CustomLoadMoreView())
    }

    override fun convert(helper: BaseViewHolder?, item: MainT2Bean?) {

        helper?.let {
            item?.let {
                when (item.type) {
                    MainT2Bean.VIEW_ITEM -> helper.setText(R.id.item_main_t2_tv_name, item.post_title)
                    MainT2Bean.VIEW_TITLE -> {
                    }
                    MainT2Bean.VIEW_TO_PAY_VIP -> helper.setText(R.id.item_main_to_pay_vip_tv_name, item.post_title)
                    MainT2Bean.VIEW_VIP -> {
                    }
                    else -> {
                    }
                }
            }

        }
    }


}