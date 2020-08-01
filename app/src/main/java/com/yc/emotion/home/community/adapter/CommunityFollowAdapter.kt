package com.yc.emotion.home.community.adapter

import android.util.Log
import android.util.SparseArray
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.emotion.home.R
import com.yc.emotion.home.base.ui.adapter.CommonMoreAdapter
import com.yc.emotion.home.model.bean.CommunityInfo
import com.yc.emotion.home.utils.DateUtils

/**
 * Created by suns  on 2019/8/28 15:44.
 * 评论跟帖
 */
class CommunityFollowAdapter(data: List<CommunityInfo>?) : CommonMoreAdapter<CommunityInfo, BaseViewHolder>(R.layout.item_community_follow_view, data) {

    private val titleArray: SparseArray<TextView> = SparseArray()

    override fun convert(helper: BaseViewHolder?, item: CommunityInfo?) {

        helper?.let {
            item?.let {

                helper.setText(R.id.tv_name, item.name)
                        .setText(R.id.tv_content, item.content)
                        .setText(R.id.tv_like_num, item.like_num.toString())
                        .addOnClickListener(R.id.ll_like)
                        .addOnClickListener(R.id.iv_like)


                try {
                    var time = DateUtils.formatTimeToStr(item.create_time,"yyyy-MM-dd")

                    if (item.create_time * 1000 >= System.currentTimeMillis() - 60 * 60 * 1000) {
                        time = "刚刚"
                    }

                    helper.setText(R.id.tv_date, time)
                } catch (e: Exception) {
                    Log.e(TAG, "convert: " + e.message)
                }

                helper.setImageResource(R.id.iv_like, if (item.is_dig == 0) R.mipmap.community_like else R.mipmap.community_like_selected)
                helper.setTextColor(R.id.tv_like_num, if (item.is_dig == 0) ContextCompat.getColor(mContext, R.color.gray_999) else ContextCompat.getColor(mContext, R.color.red_crimson))
                Glide.with(mContext).load(item.pic).apply(RequestOptions().circleCrop()
                        .placeholder(R.mipmap.main_icon_default_head).error(R.mipmap.main_icon_default_head))
                        .into(helper.getView(R.id.iv_pic))
                titleArray.put(helper.adapterPosition, helper.getView(R.id.tv_like_num))
            }
        }
    }


    fun getView(position: Int): TextView {
        return titleArray.get(position)
    }
}
