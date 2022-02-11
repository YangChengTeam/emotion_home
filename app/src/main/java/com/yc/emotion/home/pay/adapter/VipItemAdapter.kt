package com.yc.emotion.home.pay.adapter

import android.app.Activity
import android.graphics.Paint
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.GoodsInfo
import com.yc.emotion.home.model.bean.UserInfo
import com.yc.emotion.home.model.util.DoubleToStringUtils
import com.yc.emotion.home.utils.DateUtils
import com.yc.emotion.home.utils.UserInfoHelper
import yc.com.rthttplibrary.util.ScreenUtil

/**
 * Created by wanglin  on 2019/7/2 10:06.
 */
class VipItemAdapter(data: List<GoodsInfo>?) : CommonMoreAdapter<GoodsInfo, BaseViewHolder>(R.layout.vip_item_view_new, data) {


    override fun convert(helper: BaseViewHolder, item: GoodsInfo?) {

        item?.let {


            helper.setText(R.id.tv_vip_name, item.name)
                    .setText(R.id.tv_vip_price, DoubleToStringUtils.doubleStringToString(item.m_price))
                    .setText(R.id.tv_vip_origin_price, "原价" + DoubleToStringUtils.doubleStringToString(item.price))

            val tvOrigin = helper.getView<TextView>(R.id.tv_vip_origin_price)
            tvOrigin.paintFlags = tvOrigin.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            helper.setText(R.id.item_become_vip_tail_tv_pay_des_01, item?.desp)

            helper.itemView.isSelected = item.isSelect
            helper.getView<TextView>(R.id.tv_vip_name).isSelected = item.isSelect

            val screenWidth = ScreenUtil.getWidth(mContext) - ScreenUtil.dip2px(mContext, 38f) * 2
            val layoutParams = helper.itemView.layoutParams as ViewGroup.LayoutParams
            layoutParams.width = screenWidth / 3

            val position = helper.adapterPosition

            if (position == 0) {
                showGuide()
            }

//            if (null != userInfo) {
//                if (userInfo.vip_tips == 1) {//已开通
//                    if (userInfo.vip_type == position + 1) {
//                        helper.setVisible(R.id.tv_expire, true)
//                        helper.setText(R.id.tv_expire, DateUtils.formatTimeToStr(userInfo.vip_end_time.toLong(), "yyyy.MM.dd") + "到期")
//                    } else {
//                        helper.setVisible(R.id.tv_expire, false)
//                    }
//
//                } else {
//                    helper.setVisible(R.id.tv_expire, false)
//                }
//            }


        }
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


//    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//        val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
//        gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                val doodsBean = mData[position]
//                return if (doodsBean.id == 12) {
//                    2
//                } else 1
//            }
//        }
//
//    }
}
