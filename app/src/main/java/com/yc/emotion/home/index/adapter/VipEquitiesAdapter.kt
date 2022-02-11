package com.yc.emotion.home.index.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.index.domain.bean.VipEquitiesInfo
import yc.com.rthttplibrary.util.ScreenUtil

/**
 * Created by suns  on 2021/07/22 09:25 .
 */
class VipEquitiesAdapter(mDatas: MutableList<VipEquitiesInfo>?) :
        BaseQuickAdapter<VipEquitiesInfo, BaseViewHolder>(R.layout.item_vip_equities, mDatas) {
    override fun convert(helper: BaseViewHolder, item: VipEquitiesInfo?) {
        item?.let {
            helper.setImageResource(R.id.iv_icon, it.resId)
                    .setText(R.id.tv_vip_name, item.desc)
        }

        val layoutParams = helper.itemView.layoutParams as ViewGroup.LayoutParams
        val width = ScreenUtil.getWidth(mContext) - ScreenUtil.dip2px(mContext, 25f) * 2
        layoutParams.width = width / 3
    }
}