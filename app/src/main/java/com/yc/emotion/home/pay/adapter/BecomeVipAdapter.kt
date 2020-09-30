package com.yc.emotion.home.pay.adapter

import android.graphics.Color
import android.graphics.Paint
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.util.DoubleToStringUtils

class BecomeVipAdapter(data: List<GoodsInfo>?) : BaseQuickImproAdapter<GoodsInfo?, BaseViewHolder?>(R.layout.vip_item_view_new, data) {

    private val constraintLayoutSparseArray: SparseArray<ConstraintLayout> = SparseArray()
    private val dividerSparseArray: SparseArray<View> = SparseArray()
    private val paySelSparseArray: SparseArray<ImageView> = SparseArray()

    override fun convert(helper: BaseViewHolder, item: GoodsInfo?) {
        item?.let {

            helper.setText(R.id.item_become_vip_title, item.name)
                    .setText(R.id.tv_price, DoubleToStringUtils.doubleStringToString(it.m_price))
                    .setText(R.id.tv_origin_price, "原价" + DoubleToStringUtils.doubleStringToString(item.price))
                    .setText(R.id.tv_vip_desc, it.desp)
            val tvOrigin = helper.getView<TextView>(R.id.tv_origin_price)
            tvOrigin.paintFlags = tvOrigin.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            val position = helper.adapterPosition

            constraintLayoutSparseArray.put(position, helper.getView(R.id.item_become_vip_root_view))
            dividerSparseArray.put(position, helper.getView(R.id.view_divider))
            paySelSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_iv_pay_sel_01))
            setSelect(0)

        }
    }


    fun setSelect(position: Int) {
        resetState()
        constraintLayoutSparseArray[position].isSelected = true
        dividerSparseArray[position].setBackgroundColor(Color.parseColor("#ffd2bb8f"))
        paySelSparseArray[position].visibility = View.VISIBLE
    }

    private fun resetState() {
        for (i in 0 until constraintLayoutSparseArray.size()) {
            constraintLayoutSparseArray[i].isSelected = false
            dividerSparseArray[i].setBackgroundColor(Color.parseColor("#393939"))
            paySelSparseArray[i].visibility = View.GONE

        }
    }


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */


//    private fun showGuide() {
//        NewbieGuide.with(activity)
//                .setLabel("guide1")
//                .alwaysShow(false)
//                .addGuidePage(GuidePage.newInstance()
//                        .setEverywhereCancelable(false)
//                        .setLayoutRes(R.layout.layout_community_guide)
//                        .setOnLayoutInflatedListener { view, controller ->
//                            view.findViewById<ImageView>(R.id.iv_know).setOnClickListener {
//                                controller.remove()
//
//                            }
//                        })
//                .show()
//    }
}