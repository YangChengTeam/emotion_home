package com.yc.emotion.home.index.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 * Created by suns  on 2019/8/31 09:30.
 */
class EfficientCourseTagAdapter(data: List<String?>?) : CommonMoreAdapter<String?, BaseViewHolder?>(R.layout.item_community_tag, data) {
    private val itemSparseArray: SparseArray<View> = SparseArray()
    private val viewSparseArray: SparseArray<View> = SparseArray()

    override fun convert(helper: BaseViewHolder, item: String?) {

            item?.let {
                val position = helper.adapterPosition
                setItemParams(helper, position)
                helper.itemView.background = ContextCompat.getDrawable(mContext, R.drawable.community_tag_selector)
                helper.setText(R.id.tv_content, item)
                if (position == 0) {
                    helper.itemView.isSelected = true
                    helper.getView<View>(R.id.tv_content).isSelected = true
                }
                itemSparseArray.put(position, helper.itemView)
                viewSparseArray.put(position, helper.getView(R.id.tv_content))
            }
        }



    private fun setItemParams(helper: BaseViewHolder, position: Int) {
        val layoutParams = helper.itemView.layoutParams as MarginLayoutParams
        if (position < 3) {
            layoutParams.topMargin = UIUtil.dip2px(mContext, 10.0)
        }
        when {
            position % 3 == 0 -> {
                layoutParams.rightMargin = UIUtil.dip2px(mContext, 5.0)
                layoutParams.leftMargin = UIUtil.dip2px(mContext, 5.0)
            }
            position % 3 == 1 -> {
                layoutParams.leftMargin = UIUtil.dip2px(mContext, 5.0)
                layoutParams.rightMargin = UIUtil.dip2px(mContext, 5.0)
            }
            else -> {
                layoutParams.leftMargin = UIUtil.dip2px(mContext, 5.0)
                layoutParams.rightMargin = UIUtil.dip2px(mContext, 5.0)
            }
        }
        helper.itemView.layoutParams = layoutParams
    }

    fun resetView() {
        for (i in 0 until itemSparseArray.size()) {
            itemSparseArray[i].isSelected = false
            viewSparseArray[i].isSelected = false
        }
    }

    fun setViewState(position: Int) {
        itemSparseArray[position].isSelected = true
        viewSparseArray[position].isSelected = true
    }

}