package com.yc.emotion.home.index.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.mine.domain.bean.LiveInfo

class IndexLiveAdapter(liveInfos: List<LiveInfo>?) : CommonMoreAdapter<LiveInfo, BaseViewHolder>(R.layout.live_item, liveInfos) {
    override fun convert(helper: BaseViewHolder?, item: LiveInfo?) {
        helper?.let {
            item?.let {
                helper.setText(R.id.tv_live_name, item.nickname)
                        .setText(R.id.tv_live_title, item.live_title)
                        .setText(R.id.tv_live_type, item.liveType)
                Glide.with(mContext).asBitmap().load(item.live_cover)
                        .diskCacheStrategy(DiskCacheStrategy.DATA).into(helper.getView(R.id.live_roundImg))
                val state = it.status
                if (state == 1) {
                    //正在直播
                    helper.setGone(R.id.ll_live, true)
                    helper.setGone(R.id.tv_live_num, true)
                    helper.setGone(R.id.tv_live_time, false)
                    Glide.with(mContext).load(R.mipmap.live_gif).into(helper.getView(R.id.iv_live_anim))
//                    helper.setText(R.id.tv_live_num, String.format(mContext.getString(R.string.live_online), item.onlineCount))
                } else {
                    Glide.with(mContext).clear(helper.getView(R.id.iv_live_anim) as ImageView)
                    helper.setGone(R.id.ll_live, false)
                    helper.setGone(R.id.tv_live_num, false)
                    helper.setGone(R.id.tv_live_time, true)
//                    helper.setText(R.id.tv_live_time, String.format(mContext.getString(R.string.live_time, item.liveTime)))
                }
            }
        }
    }
}