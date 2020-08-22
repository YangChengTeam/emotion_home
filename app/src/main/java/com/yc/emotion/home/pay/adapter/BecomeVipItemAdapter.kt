package com.yc.emotion.home.pay.adapter

import android.graphics.Paint
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.util.DoubleToStringUtils

/**
 * Created by wanglin  on 2019/7/2 10:06.
 */
class BecomeVipItemAdapter(data: List<GoodsInfo?>?) : BaseQuickAdapter<GoodsInfo, BaseViewHolder>(R.layout.vip_item_view, data) {
    private val constraintLayoutSparseArray: SparseArray<LinearLayout>
    private val payUnitSparseArray: SparseArray<TextView>
    private val payMonSparseArray: SparseArray<TextView>
    private val descSparseArray: SparseArray<TextView>
    private val imageViewSparseArray: SparseArray<ImageView>
    override fun convert(helper: BaseViewHolder, item: GoodsInfo) {
        helper.setText(R.id.item_become_vip_tail_tv_pay_tit_01, item.name)
                .setText(R.id.item_become_vip_tail_tv_pay_mon_01, DoubleToStringUtils.doubleStringToString(item.m_price))
                .setText(R.id.item_become_vip_tail_tv_original_price_01, "原价" + DoubleToStringUtils.doubleStringToString(item.price))
        val tvOrigin = helper.getView<TextView>(R.id.item_become_vip_tail_tv_original_price_01)
        tvOrigin.paintFlags = tvOrigin.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        helper.setText(R.id.item_become_vip_tail_tv_pay_des_01, item.desp)
        helper.itemView.background = ContextCompat.getDrawable(mContext, R.drawable.vip_item_selector)
        val position = helper.adapterPosition
        constraintLayoutSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_cl_con_01))
        payUnitSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_unit_01))
        payMonSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_mon_01))
        descSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_des_01))
        imageViewSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_iv_pay_sel_01))
        setSelect(0)
    }

    fun setSelect(position: Int) {
        resetState()
        constraintLayoutSparseArray[position].isSelected = true
        payUnitSparseArray[position].isSelected = true
        payMonSparseArray[position].isSelected = true
        descSparseArray[position].isSelected = true
        imageViewSparseArray[position].visibility = View.VISIBLE
    }

    private fun resetState() {
        for (i in 0 until constraintLayoutSparseArray.size()) {
            constraintLayoutSparseArray[i].isSelected = false
            payUnitSparseArray[i].isSelected = false
            payMonSparseArray[i].isSelected = false
            descSparseArray[i].isSelected = false
            imageViewSparseArray[i].visibility = View.GONE
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
        gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val doodsBean = mData[position]
                return if (doodsBean.id == 12) {
                    2
                } else 1
            }
        }
    }

    init {
        constraintLayoutSparseArray = SparseArray()
        payUnitSparseArray = SparseArray()
        payMonSparseArray = SparseArray()
        descSparseArray = SparseArray()
        imageViewSparseArray = SparseArray()
    }
}