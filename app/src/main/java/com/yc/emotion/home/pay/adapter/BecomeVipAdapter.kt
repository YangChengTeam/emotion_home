package com.yc.emotion.home.pay.adapter

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.model.bean.BecomeVipBean
import com.yc.emotion.home.model.bean.GoodsInfo

class BecomeVipAdapter(data: List<BecomeVipBean?>?) : BaseMultiItemQuickAdapter<BecomeVipBean?, BaseViewHolder?>(data) {
    private var mNumber = 0
    fun setNumber(mNumber: Int) {
        this.mNumber = mNumber
        notifyDataSetChanged()
    }

    protected override fun convert(helper: BaseViewHolder, item: BecomeVipBean) {
        if (item != null) {
            when (item.type) {
                BecomeVipBean.VIEW_TITLE, BecomeVipBean.VIEW_VIP_TAG -> {
                }
                BecomeVipBean.VIEW_ITEM -> {
                }
                BecomeVipBean.VIEW_TAIL -> {
                    val recyclerView = helper.getView<RecyclerView>(R.id.pay_item_recyclerView)
                    recyclerView.layoutManager = GridLayoutManager(mContext, 2)
                    val vipItemAdapter = BecomeVipItemAdapter(item.payBeans)
                    recyclerView.adapter = vipItemAdapter
                    vipItemAdapter.onItemClickListener = OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
                        vipItemAdapter.setSelect(position)
                        val doodsBean = vipItemAdapter.getItem(position)
                        if (onPayClickListener != null) {
                            onPayClickListener!!.onPayClick(doodsBean)
                        }
                    }
                    if (mNumber <= 0) {
                        mNumber = 156592
                    }
                    helper.setText(R.id.item_become_vip_tv_pay_num, mNumber.toString())
                            .addOnClickListener(R.id.item_become_vip_rl_btn_wx)
                            .addOnClickListener(R.id.item_become_vip_rl_btn_zfb)
                }
            }
        }
    }

    private var onPayClickListener: OnPayClickListener? = null
    fun setOnPayClickListener(onPayClickListener: OnPayClickListener?) {
        this.onPayClickListener = onPayClickListener
    }

    interface OnPayClickListener {
        fun onPayClick(doodsBean: GoodsInfo?)
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    init {
        addItemType(BecomeVipBean.VIEW_TITLE, R.layout.recycler_view_item_become_vip_title)
        addItemType(BecomeVipBean.VIEW_ITEM, R.layout.recycler_view_item_become_vip_new)
        addItemType(BecomeVipBean.VIEW_TAIL, R.layout.recycler_view_item_become_vip_tail)
        addItemType(BecomeVipBean.VIEW_VIP_TAG, R.layout.recycler_view_item_become_vip_tag)
    }
}