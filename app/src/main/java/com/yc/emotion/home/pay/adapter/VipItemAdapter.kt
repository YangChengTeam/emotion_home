package com.yc.emotion.home.pay.adapter

import android.app.Activity
import android.graphics.Paint
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.util.DoubleToStringUtils
import com.yc.emotion.home.utils.DateUtils
import com.yc.emotion.home.utils.UserInfoHelper

/**
 * Created by wanglin  on 2019/7/2 10:06.
 */
class VipItemAdapter(data: List<GoodsInfo>?) : CommonMoreAdapter<GoodsInfo, BaseViewHolder>(R.layout.vip_item_view, data) {


    private val constraintLayoutSparseArray: SparseArray<LinearLayout> = SparseArray()
    private val payUnitSparseArray: SparseArray<TextView> = SparseArray()
    private val payMonSparseArray: SparseArray<TextView> = SparseArray()
    private val descSparseArray: SparseArray<TextView> = SparseArray()
    private val imageViewSparseArray: SparseArray<ImageView> = SparseArray()
    private val userInfo: UserInfo? = UserInfoHelper.instance.getUserInfo()

    override fun convert(helper: BaseViewHolder, item: GoodsInfo?) {


            helper.setText(R.id.item_become_vip_tail_tv_pay_tit_01, item?.name)
                    .setText(R.id.item_become_vip_tail_tv_pay_mon_01, DoubleToStringUtils.doubleStringToString(item?.m_price))
                    .setText(R.id.item_become_vip_tail_tv_original_price_01, "原价" + DoubleToStringUtils.doubleStringToString(item?.price))

            val tvOrigin = helper.getView<TextView>(R.id.item_become_vip_tail_tv_original_price_01)
            tvOrigin.paintFlags = tvOrigin.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            helper.setText(R.id.item_become_vip_tail_tv_pay_des_01, item?.desp)


            val position = helper.adapterPosition
            constraintLayoutSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_cl_con_01))
            payUnitSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_unit_01))
            payMonSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_mon_01))
            descSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_des_01))
            imageViewSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_iv_pay_sel_01))
            if (position == 0) {
                showGuide()
            }

            if (null != userInfo) {
                if (userInfo.vip_tips == 1) {//已开通

                    if (userInfo.vip_type == position + 1) {
                        helper.setVisible(R.id.tv_expire, true)
                        helper.setText(R.id.tv_expire, DateUtils.formatTimeToStr(userInfo.vip_end_time.toLong(), "yyyy.MM.dd") + "到期")
                    } else {
                        helper.setVisible(R.id.tv_expire, false)
                    }

                } else {
                    helper.setVisible(R.id.tv_expire, false)
                }
            }

            setSelect(0)


    }

    private fun showGuide() {
        if (null != mContext && mContext is Activity) {

            val activity = mContext as Activity
            NewbieGuide.with(activity)
                    .setLabel("guide3")
                    .alwaysShow(false)
                    .addGuidePage(GuidePage.newInstance()
                            .setEverywhereCancelable(false)
                            .setLayoutRes(R.layout.layout_vip_item_guide)
                            .setOnLayoutInflatedListener { view, controller -> view.findViewById<View>(R.id.iv_know).setOnClickListener { v -> controller.remove() } }
                    ).show()
        }
    }

    private fun setSelect(position: Int) {
        resetState()
        constraintLayoutSparseArray.get(position).isSelected = true
        payUnitSparseArray.get(position).isSelected = true
        payMonSparseArray.get(position).isSelected = true
        descSparseArray.get(position).isSelected = true
        //        imageViewSparseArray.get(position).setVisibility(View.VISIBLE);


    }

    private fun resetState() {
        for (i in 0 until constraintLayoutSparseArray.size()) {
            constraintLayoutSparseArray.get(i).isSelected = false
            payUnitSparseArray.get(i).isSelected = false
            payMonSparseArray.get(i).isSelected = false
            descSparseArray.get(i).isSelected = false
            imageViewSparseArray.get(i).visibility = View.GONE
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val doodsBean = mData[position]
                return if (doodsBean.id == 12) {
                    2
                } else 1
            }
        }

    }
}
