package com.yc.emotion.home.index.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.mine.domain.bean.LiveInfo
import com.yc.emotion.home.mine.domain.bean.LiveVideoInfo
import java.text.SimpleDateFormat
import java.util.*

class IndexLiveAdapter(liveInfos: List<LiveVideoInfo>?) : CommonMoreAdapter<LiveVideoInfo, BaseViewHolder>(R.layout.live_item, liveInfos) {
    override fun convert(helper: BaseViewHolder, item: LiveVideoInfo?) {

        item?.let {

            helper.setText(R.id.tv_live_name, item.teacher_name)
                    .setText(R.id.tv_live_title, item.title)
                    .setText(R.id.tv_live_type, item.sub_title)
            Glide.with(mContext).asBitmap().load(item.cover)
                    .diskCacheStrategy(DiskCacheStrategy.DATA).into(helper.getView(R.id.live_roundImg))
            helper.setText(R.id.tv_live_num, String.format(mContext.getString(R.string.live_online), item.view_count))
//                when (it.status) {
//                    1 -> {
//                        //正在直播
//                        helper.setGone(R.id.ll_live, true)
//                                .setGone(R.id.iv_live_anim, true)
//                                .setText(R.id.tv_state, "直播中")
//                        helper.setGone(R.id.tv_live_num, true)
//                        helper.setGone(R.id.tv_live_time, false)
//                        Glide.with(mContext).load(R.mipmap.live_gif).into(helper.getView(R.id.iv_live_anim))
//                        helper.setText(R.id.tv_live_num, String.format(mContext.getString(R.string.live_online), item.people_num))
//                    }
//                    2 -> {
//                        Glide.with(mContext).clear(helper.getView(R.id.iv_live_anim) as ImageView)
//                        helper.setText(R.id.tv_state, "直播预约")
//                                .setGone(R.id.iv_live_anim, false)
//                                .setGone(R.id.tv_live_num, false)
//                                .setText(R.id.tv_live_time, String.format(mContext.getString(R.string.start_end_time),
//                                        convertTime(it.start_time), convertTime(it.end_time)))
//
//                    }
//                    else -> {
//                        Glide.with(mContext).clear(helper.getView(R.id.iv_live_anim) as ImageView)
//                        helper.setText(R.id.tv_state, "直播回放")
//                                .setGone(R.id.iv_live_anim, false)
//                        helper.setGone(R.id.tv_live_num, false)
//                        helper.setGone(R.id.tv_live_time, true)
//                        //                    helper.setText(R.id.tv_live_time, String.format(mContext.getString(R.string.live_time, item.liveTime)))
//                    }
//                }
        }

    }

    private fun convertTime(time: Long): String? {
        val sd = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sd.format(Date(time * 1000))
    }
}