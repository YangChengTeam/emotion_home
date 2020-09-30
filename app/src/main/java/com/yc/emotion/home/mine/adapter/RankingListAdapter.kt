package com.yc.emotion.home.mine.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.BaseQuickImproAdapter
import com.yc.emotion.home.mine.domain.bean.RewardDetailInfo

/**
 *
 * Created by suns  on 2020/8/29 09:22.
 */
class RankingListAdapter(datas: List<RewardDetailInfo>?) : BaseQuickImproAdapter<RewardDetailInfo, BaseViewHolder>(R.layout.item_reward_money, datas) {
    override fun convert(helper: BaseViewHolder, item: RewardDetailInfo?) {
        item?.let {
            helper.setText(R.id.tv_reward_name, it.nick_name)
                    .setText(R.id.tv_reward_date, it.date)
                    .setText(R.id.tv_reward_content, "成功邀请${it.count}人")

            val ivRewardMoney = helper.getView<ImageView>(R.id.iv_reward_money)
            Glide.with(mContext).load(it.face).apply(RequestOptions().circleCrop()).into(ivRewardMoney)
        }
    }
}