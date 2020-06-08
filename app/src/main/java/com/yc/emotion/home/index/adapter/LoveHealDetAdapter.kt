package com.yc.emotion.home.index.adapter

import android.text.TextUtils
import androidx.core.content.ContextCompat

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.widget.CustomLoadMoreView
import com.yc.emotion.home.model.bean.LoveHealDetDetailsBean

/**
 * Created by mayn on 2019/5/5.
 */

/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
 */
class LoveHealDetAdapter(data: List<LoveHealDetDetailsBean>?) : BaseMultiItemQuickAdapter<LoveHealDetDetailsBean, BaseViewHolder>(data) {


    init {
        addItemType(LoveHealDetDetailsBean.VIEW_TITLE, R.layout.recycler_view_item_details_bean_tit)
        addItemType(LoveHealDetDetailsBean.VIEW_ITEM, R.layout.recycler_view_item_details_bean)
        setLoadMoreView(CustomLoadMoreView())
    }


    override fun convert(helper: BaseViewHolder, item: LoveHealDetDetailsBean?) {

        item?.let {

            val ansSex = item.ans_sex

            if (item.type == LoveHealDetDetailsBean.VIEW_TITLE) {

                helper.setText(R.id.item_details_bean_tittv_name, item.content)

                if (!TextUtils.isEmpty(ansSex)) {
                    helper.setImageDrawable(R.id.item_details_bean_titiv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_women))

                }
            } else if (item.type == LoveHealDetDetailsBean.VIEW_ITEM) {
                helper.setText(R.id.item_details_bean_tv_name, item.content)
                if (!TextUtils.isEmpty(ansSex)) {

                    when (ansSex) {
                        "1" -> //1男2女0bi'a
                            helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_men))
                        "2" -> helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_women))
                        else -> helper.setImageDrawable(R.id.item_details_bean_iv_sex, ContextCompat.getDrawable(mContext, R.mipmap.icon_dialogue_nothing))
                    }
                }
                helper.addOnClickListener(R.id.item_details_bean_iv_copy)
            }
        }
    }
}

