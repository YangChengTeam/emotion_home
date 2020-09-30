package com.yc.emotion.home.mine.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.mine.domain.bean.DisposeDetailInfo

/**
 *
 * Created by suns  on 2020/8/24 19:43.
 */
class DisposeDetailAdapter(mDatas: List<DisposeDetailInfo>?) : BaseQuickImproAdapter<DisposeDetailInfo, BaseViewHolder>(R.layout.item_dispose_detail, mDatas) {
    override fun convert(helper: BaseViewHolder, item: DisposeDetailInfo?) {
        item?.let {
            helper.setText(R.id.tv_dispose_title, it.title)
                    .setText(R.id.tv_dispose_money, "+¥${it.money.toFloat()}元")
                    .setText(R.id.tv_dispose_num, "提现编号：${it.dispose_num}")
                    .setText(R.id.tv_dispose_date, it.date)
            when (it.state) {
//                成功
                1 -> {
                    helper.setTextColor(R.id.tv_dispose_state, Color.parseColor("#38B62B"))
                            .setText(R.id.tv_dispose_state, "提现成功")
                }
//                等待后台审核
                0 -> {
                    helper.setTextColor(R.id.tv_dispose_state, Color.parseColor("#FF9C27"))
                            .setText(R.id.tv_dispose_state, "等待后台审核")
                }
//                提现失败
                2 -> {
                    helper.setTextColor(R.id.tv_dispose_state, Color.parseColor("#FF2C55"))
                            .setText(R.id.tv_dispose_state, "提现失败")
                            .setGone(R.id.tv_dispose_failure, true)
                            .setText(R.id.tv_dispose_failure, "失败原因：${it.failure}")
                }
                4 -> {
                    helper.setGone(R.id.tv_dispose_state, false)
                            .setGone(R.id.tv_dispose_recall, true)
                }
                else -> {
                }
            }
        }
    }
}