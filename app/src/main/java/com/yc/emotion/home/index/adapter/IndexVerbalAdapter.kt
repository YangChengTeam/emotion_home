package com.yc.emotion.home.index.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.LoveHealDateBean

/**
 * Created by mayn on 2019/6/12.
 * 首页话术适配
 */

/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
 */
class IndexVerbalAdapter(data: List<LoveHealDateBean>?) : BaseMultiItemQuickAdapter<LoveHealDateBean, BaseViewHolder>(data) {


    init {
        addItemType(LoveHealDateBean.ITEM_TITLE, R.layout.recycler_view_item_love_heal_new)
        addItemType(LoveHealDateBean.ITEM_CONTENT, R.layout.recycler_view_item_love_heal)

    }


    override fun convert(helper: BaseViewHolder?, item: LoveHealDateBean?) {

        helper?.let {

            item?.let {
                when (item.type) {
                    LoveHealDateBean.ITEM_TITLE -> helper.setText(R.id.item_love_heal_title_tv_name, item.name)
                    LoveHealDateBean.ITEM_CONTENT -> helper.setText(R.id.item_love_heal_tv_name, item.name)
                    else -> {
                    }
                }
            }

        }


    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?

        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                val loveHealDateBean = getItem(position)
                if (loveHealDateBean != null)
                    if (loveHealDateBean.type == LoveHealDateBean.ITEM_CONTENT) {
                        return 1
                    }
                return 3


            }
        }

    }
}