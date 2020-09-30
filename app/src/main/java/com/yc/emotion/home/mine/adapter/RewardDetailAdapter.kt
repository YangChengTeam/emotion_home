package com.yc.emotion.home.mine.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo

/**
 *
 * Created by suns  on 2020/8/24 14:10.
 */
class RewardDetailAdapter(mDatas: List<RewardDetailInfo>?) : BaseQuickImproAdapter<RewardDetailInfo, BaseViewHolder>(R.layout.item_reward_detail, mDatas) {
    override fun convert(helper: BaseViewHolder, item: RewardDetailInfo?) {
        item?.let {
            helper.setText(R.id.tv_reward_detail_title, it.title)
                    .setText(R.id.tv_reward_date, it.date)
                    .setText(R.id.tv_reward_money, it.money)
        }
    }
}