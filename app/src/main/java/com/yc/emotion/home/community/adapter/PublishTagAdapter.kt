package com.yc.emotion.home.community.adapter

import android.support.v4.content.ContextCompat
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.CommunityTagInfo
import com.yc.emotion.home.model.constant.ConstantKey
import com.yc.emotion.home.utils.Preference

import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 * Created by suns  on 2019/8/31 09:30.
 */
class PublishTagAdapter(data: List<CommunityTagInfo>?) : BaseQuickAdapter<CommunityTagInfo, BaseViewHolder>(R.layout.item_community_tag, data) {
    private val itemSparseArray: SparseArray<View> = SparseArray()
    private val viewSparseArray: SparseArray<View> = SparseArray()

    private var pos by Preference(ConstantKey.TAG_POSTION, 0)

    override fun convert(helper: BaseViewHolder, item: CommunityTagInfo) {

        val position = helper.adapterPosition
        setItemParams(helper, position)
        helper.itemView.background = ContextCompat.getDrawable(mContext, R.drawable.community_tag_selector)

        helper.setText(R.id.tv_content, item.title)
        if (position == pos) {
            helper.itemView.isSelected = true
            helper.getView<View>(R.id.tv_content).isSelected = true
        }

        itemSparseArray.put(position, helper.itemView)
        viewSparseArray.put(position, helper.getView(R.id.tv_content))
    }

    private fun setItemParams(helper: BaseViewHolder, position: Int) {
        val layoutParams = helper.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (position < 3) {
            layoutParams.topMargin = UIUtil.dip2px(mContext, 10.0)
        }

        if (position % 3 == 0) {
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5.0)
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5.0)
        } else if (position % 3 == 1) {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5.0)
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5.0)
        } else {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5.0)
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5.0)
        }
        helper.itemView.layoutParams = layoutParams
    }

    fun resetView() {
        for (i in 0 until itemSparseArray.size()) {
            itemSparseArray.get(i).isSelected = false
            viewSparseArray.get(i).isSelected = false
        }
    }

    fun setViewState(position: Int) {
        itemSparseArray.get(position).isSelected = true
        viewSparseArray.get(position).isSelected = true
    }

}
